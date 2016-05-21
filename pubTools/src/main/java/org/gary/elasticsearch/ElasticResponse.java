package org.gary.elasticsearch;

import java.util.List; 
import java.util.Map;
import java.util.Vector;

public class ElasticResponse {
	
	protected long totlePage ; 
	
	
	protected List<Map<String,Object>> datas = new Vector<Map<String,Object>>() ;


	public long getTotlePage() {
		return totlePage;
	}


	public void setTotlePage(long totlePage) {
		this.totlePage = totlePage;
	}


	public List<Map<String, Object>> getDatas() {
		return datas;
	}


	public void setDatas(List<Map<String, Object>> datas) {
		this.datas = datas;
	} 
	
	
	
}
