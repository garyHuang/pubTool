package org.gary.poi.util.xls;

import java.util.List;
import java.util.Map;

import org.gary.dbs.DBUtils;
import org.gary.mongodb.MongodbUtils;

public class ReadToMongodb {
	
	
	public static void main(String[] args) {
		int pageId = 1 ;	
		String sql = "SELECT epc_parts_detailed.status,epc_parts_detailed.id, epc_parts_detailed.OriginProperty, epc_parts_detailed.materialcode, epc_parts_detailed.materialName, epc_parts_detailed.OECode, epc_parts_detailed.brandcode, epc_parts_detailed.origins, epc_parts_detailed.maintencemonth, epc_parts_detailed.maintencerange, epc_parts_detailed.picUrl, epc_parts_detailed.remark, epc_parts_detailed.STATUS, epc_parts_detailed.carBrand, epc_parts_detailed.specifications, epc_parts_detailed.ecpunit, epc_parts_detailed.partbandid, epc_parts_detailed.OriginsCode, epc_parts_detailed.four_s_price FROM epc_parts_detailed  limit ?,?" ;
		int pageSize = 1 ; 
		MongodbUtils mongodbUtils = new MongodbUtils("hks", "115.28.184.227", 27017  , "hks" , "hks123");
		List<Map<String, Object>> objs = DBUtils.getDBUtil().getResults(sql, pageId , pageSize);
		mongodbUtils.saves("parts", objs );
	}
}
