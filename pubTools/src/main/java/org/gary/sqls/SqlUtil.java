package org.gary.sqls;

import java.util.List;
import java.util.Vector;

import org.gary.comm.utils.Helper;
import org.gary.sqls.SimpleCondition.Comparison;

public class SqlUtil {
	
	public static String getPageSql(String sql , int pageId , int pageSize){
		sql += " limit " + (pageId-1) * pageSize + "," + pageSize ;
		return sql ; 
	}
	
	public static List<Object> appendSql(StringBuffer sqlBuffer,
			SimpleCondition... conditions) {
		List<Object>params = new Vector<Object>();
		for(SimpleCondition condition:conditions){
			
			
			if(condition.getComparison() == Comparison.IN){
				int length = Helper.getArrayLength(  condition.getCompareValue() ); 
				if(length > 0){
					if(sqlBuffer.indexOf("where") == -1){
						sqlBuffer.append(" where ");
					}else{
						sqlBuffer.append(" and ");
					}
					sqlBuffer.append(condition.getField()) ;
					sqlBuffer.append( condition.getComparison().getComparison() );
					sqlBuffer.append("(") ;
					
					for(int x=0;x<length;x++){
						if(x > 0){
							sqlBuffer.append(",");
						}
						sqlBuffer.append("?") ;
						params.add( Helper.getArray(condition.getCompareValue(), x) ) ;
					}
					sqlBuffer.append(")") ;
				}
			}else if(Comparison.EMPTY == condition.getComparison()){
				if(sqlBuffer.indexOf("where") == -1){
					sqlBuffer.append(" where ");
				}else{
					sqlBuffer.append(" and ");
				}
				sqlBuffer.append( condition.getField() );
				if(!Helper.isNull(condition.getCompareValue())){
					int length = Helper.getArrayLength(  condition.getCompareValue() ); 
					if(0 == length){
						params.add( condition.getCompareValue() ) ;
					}else{
						for(int x=0;x<length;x++){
							params.add( Helper.getArray(condition.getCompareValue(), x) ) ;
						}
					}
				}
			}else{
				if(sqlBuffer.indexOf("where") == -1){
					sqlBuffer.append(" where ");
				}else{
					sqlBuffer.append(" and ");
				}
				sqlBuffer.append(condition.getField())
				.append(" ").append( condition.getComparison().getComparison() ).append(" ?") ; 
				params.add( condition.getCompareValue() ) ;
			}
		}
		return params;
	}
}
