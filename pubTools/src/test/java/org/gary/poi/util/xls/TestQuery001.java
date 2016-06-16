package org.gary.poi.util.xls;

import java.util.Map;

import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.gary.elasticsearch.ElasticResponse;
import org.gary.elasticsearch.ElasticSearchUtils;

public class TestQuery001 {

	
	public static void main(String[] args) {
		
		ElasticSearchUtils searchUtils = ElasticSearchSingle.getSearchUtils(); 
		QueryStringQueryBuilder builder = new QueryStringQueryBuilder("\"1104-005\"");
		ElasticResponse query = searchUtils.query( 0 , 2000 , SearchType.DEFAULT , null , new QueryBuilder[]{
				builder
		}) ;
		for(Map<String,Object> data : query.getDatas()){
			System.out.println( data );
		}
		System.out.println( query.getTotlePage() );
	}
}
