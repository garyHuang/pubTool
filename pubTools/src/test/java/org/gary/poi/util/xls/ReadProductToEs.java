package org.gary.poi.util.xls;

import java.util.*;

import org.gary.dbs.DBUtils;
import org.gary.elasticsearch.ElasticSearchUtils;

public class ReadProductToEs {

	static int pageId = 1 ;
	
	static synchronized int getNextPage(){
		System.out.println( Thread.currentThread() + "--" + pageId ); 
		return ++pageId;
	}
	static String[]keywords = "postion,lastBrandName,groupname,materialName,carBrandName,brandname,OECode,autopartname,origins,bname,chexiName,commonname".split(",") ;
	
	static class SCPage extends Thread{
		@Override
		public void run() {
			ElasticSearchUtils searchUtils = ElasticSearchUtils
					.getElasticSearch("hks", "10.163.101.230", 9300,
							"haokuaisheng", "part", "itemid" ); 
			while(true){
				String queryPageSql = "SELECT id , materialcode FROM epc_parts_detailed  WHERE STATUS = 1 ";
				List<Map<String, Object>> idList = DBUtils.getDBUtil().getResults(
						queryPageSql + "limit " + (getNextPage() * 1000)  + ",1000");
				for (Map<String, Object> id : idList) {
					String sql = "SELECT epc_parts_detailed.id partid, epc_parts_detailed.OriginProperty, epc_parts_detailed.materialcode, epc_parts_detailed.materialName, epc_parts_detailed.OECode, epc_parts_detailed.brandcode, epc_parts_detailed.origins, epc_parts_detailed.maintencemonth, epc_parts_detailed.maintencerange, epc_parts_detailed.picUrl, epc_parts_detailed.remark, epc_parts_detailed.status, epc_parts_detailed.carBrand, epc_parts_detailed.specifications, epc_parts_detailed.ecpunit, epc_parts_detailed.partbandid, epc_parts_detailed.OriginsCode, epc_parts_detailed.uOrD, epc_parts_detailed.four_s_price, epc_parts_detailed.volume, epc_parts_detailed.weight, epc_parts_detailed.criterion, epc_parts_brand.bname, epc_partinfo.code, epc_partinfo.commonname, epc_partinfo.groupid, epc_partinfo.englishname, epc_partinfo.autopartname, epc_partinfo.postion, epc_partinfo.picno, epc_partinfo.postion, epc_partinfo.badlevel, epc_partinfo.unit, epc_partgroup.groupcode, epc_partgroup.groupname, epc_partgroup.hcode, epc_partgroup.pcode, v_carinfo.c1Id, v_carinfo.c2Id, v_carinfo.c3Id, v_carinfo.groupCode, v_carinfo.carBrandName, v_carinfo.lastBrandName, v_carinfo.brandname, v_carinfo.chexiName FROM epc_parts_detailed LEFT JOIN epc_relation ON epc_parts_detailed.OECode = epc_relation.OECode LEFT JOIN epc_parts_brand ON epc_parts_brand.bcode = epc_parts_detailed.OriginsCode LEFT JOIN epc_partinfo ON epc_partinfo.code = epc_relation.code LEFT JOIN epc_partgroup ON epc_partgroup.groupcode = epc_partinfo.groupid LEFT JOIN v_carinfo ON v_carinfo.c3Id ="
							+ " epc_relation.cartypecode WHERE epc_parts_detailed.status = 1 and epc_parts_detailed.id = " + id.get("id") ;
					List<Map<String, Object>> results = DBUtils.getDBUtil().getResults(sql);
					int rowIndex = 0 ;
					for (Map<String, Object> result : results) {
						result.put("rowIndex", ++rowIndex );
						result.put("itemid", generateIndex(Integer.valueOf( result.get("partid").toString() ), rowIndex )); 
						StringBuffer buffer = new StringBuffer();
						for(String key:keywords){
							Object value = result.get( key );
							if(null == value){
								continue ; 
							}
							if(buffer.length() > 0){
								buffer.append(",");
							}
							buffer.append( value ) ;
						}
						result.put("keyword", buffer);
						searchUtils.save(result);
					}
				}
			}
		}
	}
	
	public static long generateIndex(int productId, int sid) {
		long longId = (long) productId ;
		long longSId = (long) sid ;
		return (long) (longId + longSId * 0x10) + Integer.MAX_VALUE ;
	}
	
	public static void main(String[] args) {
		for(int x=0;x<16;x++){
			new SCPage().start();
		}
	}
}
