package org.gary.poi.util.xls;

import java.util.Arrays;
import java.util.Map;

import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.gary.elasticsearch.ElasticResponse;
import org.gary.elasticsearch.ElasticSearchUtils;

public class TestQuery001 {

	
	public static void main(String[] args) {
		
		ElasticSearchUtils searchUtils = ElasticSearchSingle.getSearchUtils(); 
		
		ElasticResponse query = searchUtils.query( 2, 2000 , SearchType.DEFAULT, new QueryBuilder[]{
				QueryBuilders.boolQuery().must(
						QueryBuilders.termsQuery( "relation_code" , 
								Arrays.asList( "3500-031", "3500-042" ))) 
		}) ;
		for(Map<String,Object> data : query.getDatas()){
			System.out.println( data );
		}
		System.out.println( query.getTotlePage() );
	}
}
