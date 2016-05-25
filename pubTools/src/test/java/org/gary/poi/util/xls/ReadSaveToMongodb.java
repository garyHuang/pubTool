package org.gary.poi.util.xls;

import java.util.List;
import java.util.Map;

import org.gary.comm.utils.Helper;
import org.gary.comm.utils.TransformUtils;
import org.gary.dbs.DBUtils;
import org.gary.mongodb.MongodbUtils;

import com.mongodb.client.model.Filters;

public class ReadSaveToMongodb {
	public static Integer pageId = 0;
	
	public synchronized static Integer getNextPageId(){
		pageId++;
		return pageId;
	}
	
	public static void bathSave(String tableName){
		MongodbUtils mongodbUtils = new MongodbUtils("hks", "10.45.19.14", 27017  , "hks" , "hks123");  
		while(true){
			List<Map<String, Object>> results = DBUtils.getDBUtil().getResults( "SELECT ProductCode, SupplierCode, CheckPrice, CustomerType1, CustomerType3, CustomerType30, CustomerType29, CustomerType27, CustomerType28, CustomerType26, CustomerType25, CustomerType24, CustomerType23, CustomerType22, CustomerType21, CustomerType20, CustomerType19, CustomerType18, CustomerType17, CustomerType16, CustomerType15, CustomerType14, CustomerType13, CustomerType11, CustomerType12, CustomerType10, CustomerType9, CustomerType8, CustomerType7, CustomerType6, CustomerType5, CustomerType2, CustomerType4 FROM jgzx." + tableName 
					 + " limit "+(getNextPageId() * 1000)+",1000 ") ;  
			if( Helper.isNull(results) ){
				break ; 
			}
			for(Map<String, Object>  result:results){
				String productCode = TransformUtils.toString( result.get("productcode")) ;
				Integer supplierCode = TransformUtils.toInt( result.get("suppliercode")) ;
				mongodbUtils.delete( tableName ,  Filters.and(
						Filters.eq("productcode", productCode ) ,  
						Filters.eq("suppliercode", supplierCode ) ) ) ; 
				System.out.println( productCode + "-" + supplierCode ); 
				mongodbUtils.save(tableName, result); 
			}
			pageId++;
		}
	}
	
	public static void main(String[] args){
		for( int x=0 ;x < 100 ; x++ ){
			new Thread(new Runnable() {
				public void run() {
					bathSave("p1004");
				}
			}).start();
		}
	}
}
