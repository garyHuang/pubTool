package org.gary.poi.util.xls;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
/***
 * 
 * @author Gary
 * @mail 834865081@qq.com
 * 
 */
import org.gary.mongodb.MongodbUtils;

public class TestMongoDb {
	static MongodbUtils mongodbUtils = new MongodbUtils( "gary" , "115.28.184.227", 27017); 
	static String tName = "gary" ;
	public static void main(String[] args) {
		// testSave(); 
		//testSaves();  
		testUpdate();
	}
	
	static void testUpdate() {
		Map<String,Object> data2 = new HashMap<String, Object>();
		data2.put("name", "name") ;
		data2.put("age", 25 ) ;
		data2.put("sex", 1 ) ;
		mongodbUtils.update(tName, data2, "age");
	}

	static void testSaves(){
		List<Map<String,Object>>datas= new Vector<Map<String,Object>>();
		Map<String,Object> data1 = new HashMap<String, Object>();
		data1.put("name", "name") ;
		data1.put("age", 18) ;
		data1.put("sex", 1) ;
		datas.add( data1 );
		Map<String,Object> data2 = new HashMap<String, Object>();
		data2.put("name", "name2") ;
		data2.put("age", 19) ;
		data2.put("sex", 0) ;
		datas.add( data2 );
		mongodbUtils.saves( "person" , datas ) ;
	}
	
	static void testSave(){
		Map<String,Object> data1 = new HashMap<String, Object>();
		data1.put("name", "name") ;
		data1.put("age", 18) ;
		data1.put("sex", 1) ;
		mongodbUtils.save(tName, data1 );
	}
}
