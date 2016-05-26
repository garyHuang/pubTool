package org.gary.elasticsearch.hks;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.gary.comm.utils.Helper;
import org.gary.comm.utils.TransformUtils;
import org.gary.dbs.DBUtils;
import org.gary.elasticsearch.ElasticSearchUtils;
import org.gary.poi.util.xls.ElasticSearchSingle;
import org.gary.sqls.SimpleCondition;
import org.gary.sqls.SqlUtil;

public class GIndex {
	
	
	/**
	 * 行号唯一的主键
	 * */
	public static final String KEY_PK_ID = "row_id" ;
	/**
	 * 产品类型，1为供应商产品
	 * */
	public static final String KEY_PRODUCT_TYPE = "producttype" ; 
	/**
	 * 产品类型，1为供应商产品
	 * */
	public static final String KEY_SID = "sid" ; 
	/**
	 * 关系表code
	 * */
	public static final String KEY_RELATION_CODE = "relation_code" ; 
	public static final String KEY_RELATION_PICNO = "relation_picno" ; 
	public static final String KEY_RELATION_OECODE= "relation_oecode" ; 
	/**
	 * 配件搜索关键字
	 * */
	public static final String KEYWORD = "keyword" ; 
	/**
	 * 年款关联
	 * */
	public static final String KEY_RELATION_CARTYPECODE= "relation_cartypecode" ; 
	/**
	 * 分组id关键字
	 * */
	public static final String KEY_RELATION_GROUPID= "relation_groupid" ; 
	/**
	 * 部位关键字
	 * */
	public static final String KEY_RELATION_POSTION= "relation_postion" ; 
	/**
	 * 关联车型品牌
	 * */
	public static final String KEY_RELATION_BRANDID= "relation_brandid" ; 

	protected static final String HKS_SQL = "SELECT epc_parts_detailed.id, epc_parts_detailed.OriginProperty, epc_parts_detailed.materialcode, epc_parts_detailed.materialName, epc_parts_detailed.OECode, epc_parts_detailed.brandcode, epc_parts_detailed.origins, epc_parts_detailed.maintencemonth, epc_parts_detailed.maintencerange, epc_parts_detailed.picUrl, epc_parts_detailed.remark, epc_parts_detailed.STATUS, epc_parts_detailed.carBrand, epc_parts_detailed.specifications, epc_parts_detailed.ecpunit, epc_parts_detailed.partbandid, epc_parts_detailed.OriginsCode, epc_parts_detailed.four_s_price FROM epc_parts_detailed where epc_parts_detailed.status = 1" ;
	
	public static void addHksShopIndexs(String[]materialCodes) {
		ElasticSearchUtils searchUtils = 
				ElasticSearchSingle.getSearchUtils() ;
		StringBuffer sqlBuffer = new StringBuffer(HKS_SQL);
		
		List<Object> params = SqlUtil.appendSql(sqlBuffer, new SimpleCondition("materialcode" ,
				SimpleCondition.Comparison.IN , materialCodes));
		List<Map<String, Object>> datas = DBUtils.getDBUtil().getResults( sqlBuffer.toString() , 
				params.toArray() ) ; 
		for(Map<String, Object> data:datas){
			addCommParam(data);
			searchUtils.save( data );
		}
	}
	 
	protected static void addCommParam(Map<String, Object> data){
		List<Map<String, Object>> oeCodeCartypes = getOeCodeCartypes( TransformUtils.toString(data.get("OECode")));
		data.put( KEY_SID , TransformUtils.toString(data.get("sid")) );  
		Set<String>codes = new HashSet<String>();
		Set<String>cartypeCodes = new HashSet<String>();
		Set<String>brandIds = new HashSet<String>();
		Set<String>keywords = new HashSet<String>();
		Set<String>groupids = new HashSet<String>();
		Set<String>postions = new HashSet<String>();
		Set<String>picnos = new HashSet<String>();
		Set<String>oecodes = new HashSet<String>();
		
		if(!Helper.isNull(oeCodeCartypes)){
			for(Map<String, Object> oeCodeCartype:oeCodeCartypes){
				String code =  TransformUtils.toString(oeCodeCartype.get("code")) ;
				String groupid =  TransformUtils.toString(oeCodeCartype.get("groupid")) ;
				String postion =  TransformUtils.toString(oeCodeCartype.get("postion")) ;
				String picno =  TransformUtils.toString(oeCodeCartype.get("picno")) ;
				oecodes.add( TransformUtils.toString(oeCodeCartype.get("oecode"))  );
				codes.add( code );
				picnos.add( picno ) ; 
				groupids.add( groupid );
				postions.add( postion ); 
				String c3Id = TransformUtils.toString( oeCodeCartype.get("c3Id") ) ; 
				cartypeCodes.add( c3Id );
	
				String brandId = TransformUtils.toString(oeCodeCartype.get("brandId")); 
				brandIds.add( brandId );
				keywords.add( TransformUtils.toString(oeCodeCartype.get("brandname")) ); 
			}
			data.put( KEY_RELATION_CODE , codes ); 
			data.put( KEY_RELATION_GROUPID , groupids ) ;
			data.put( KEY_RELATION_PICNO , picnos ) ; 
			data.put( KEY_RELATION_OECODE , oecodes ) ; 
			data.put( KEY_RELATION_POSTION , postions ) ;
			data.put( KEY_RELATION_CARTYPECODE ,cartypeCodes ); 
			if(!Helper.isNull(brandIds)){
				data.put( KEY_RELATION_BRANDID ,brandIds ); 
			}
		}
		String codeStr = "" ;
		if(!Helper.isNull(codes)){
			codeStr = Helper.mergeString( 
					DBUtils.getDBUtil().getResultToList( "SELECT CONCAT(autopartname , englishname , commonname) enname FROM epc_partinfo WHERE CODE IN (" +
							 Helper.mergeString(codes) +") " ), ",") ;
		}
		data.put( KEYWORD , Helper.mergeString(keywords, ",") + "," + TransformUtils.toString(data.get("materialcode"))
				 + "," + TransformUtils.toString(data.get("materialname"))
				 + "," +  codeStr
		);
	}
	
	
	/**
	 * 获取 好快省编码 车型编码，品牌id 方法，根据oe号获取
	 * */
	protected static List<Map<String, Object>> getOeCodeCartypes(String oecode){
		/*查询关系表的cartypecode和code*/
		String queryCodeSql = "SELECT b.cartypecode, b.code , p.groupid,p.postion,p.picno FROM epc_relation b JOIN epc_partinfo p ON p.code = b.code WHERE OECode=?" ;
		List<Map<String, Object>> results = DBUtils.getDBUtil().getResults(queryCodeSql , oecode ) ; 
		if(Helper.isNull(results)){
			return null ; 
		}
		String mergeString = Helper.mergeString(Helper.toArray(results, "cartypecode", Integer.class) ); 
		String sql = "SELECT a.c1Id , a.c2Id , a.c3Id , a.groupCode , a.brandname FROM v_carinfo a WHERE a.c3Id in(" + mergeString + ")" ; 
		Map<String, Map<String, Object>> resultToMap = DBUtils.getDBUtil().getResultToMap( sql , "c3Id" ) ;
		for(Map<String, Object> result:results){
			String cartypecode = TransformUtils.toString(result.get("cartypecode"));
			Map<String, Object> tempMap = resultToMap.get(cartypecode);
			if(null != tempMap){
				result.putAll( tempMap ) ;  
			}
		}
		return results ; 
	}
}
