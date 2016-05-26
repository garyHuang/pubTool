package org.gary.elasticsearch.hks;

import java.util.List;
import java.util.Map;

import org.gary.comm.utils.Helper;
import org.gary.comm.utils.TransformUtils;
import org.gary.dbs.DBUtils;
import org.gary.logs.LogManager;

public class SynParts {
	/**
	 * 查询总的数据量
	 * */
	protected static String PARTS_QUERY_COUNT_SQL = "select COUNT(1) FROM epc_parts_detailed WHERE STATUS = 1" ;
	/**
	 * 查询好快省配件
	 * */
	protected static String PARTS_QUERY_SQL = "select materialcode FROM epc_parts_detailed WHERE STATUS = 1 limit ?,?" ;
	
	protected Integer pageId  = 0; 
	
	protected Integer totlePage ;
	
	protected Integer pageSize = 1000 ;
	
	public static SynParts getSynProducts(){
		SynParts products = new SynParts();
		products.getPartsCount(); 
		return products; 
	}
	
	public Integer getPartsCount(){
		totlePage = TransformUtils.toInt( DBUtils.getDBUtil().getObject( PARTS_QUERY_COUNT_SQL ) ) ;
		return totlePage; 
	}
	
	public synchronized String[] getNextPartsMaterialcode(){
		pageId++;
		int fristRow = (pageId-1) * pageSize;
		List<Map<String, Object>> results = DBUtils.getDBUtil().getResults(PARTS_QUERY_SQL , fristRow , pageSize) ; 
		LogManager.info("正在执行当前页面:" + pageId + " threadId:" + Thread.currentThread().getId() );
		return Helper.toArray(results, "materialcode" , String.class ) ; 
	}

	public Integer getPageId() {
		return pageId;
	}

	public void setPageId(Integer pageId) {
		this.pageId = pageId;
	}
}
