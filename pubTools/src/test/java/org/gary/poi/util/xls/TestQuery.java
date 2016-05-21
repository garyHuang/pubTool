package org.gary.poi.util.xls;

import java.util.Map;

import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.gary.elasticsearch.ElasticResponse;
import org.gary.elasticsearch.ElasticSearchUtils;


public class TestQuery {
	
	
	public static void main(String[] args) {
		ElasticSearchUtils searchUtils = ElasticSearchUtils
				.getElasticSearch("hks", "10.163.101.230", 9300,
						"haokuaisheng", "part", "partid");
		
		//QueryBuilders.fuzzyQuery ("keyword", "制动")
		
		ElasticResponse query = searchUtils.query(0, 10 , SearchType.DFS_QUERY_THEN_FETCH , new QueryBuilder[]{
				QueryBuilders.queryStringQuery("换档控制壳体托架总")
		}) ;
		
		
		for(Map<String,Object> data : query.getDatas()){
			System.out.println( data );
		}
		System.out.println( query.getTotlePage()); 
	}
}
