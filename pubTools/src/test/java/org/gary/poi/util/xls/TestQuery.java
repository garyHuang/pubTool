package org.gary.poi.util.xls;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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
		Map<String, Object> search_params = new HashMap<String, Object>();
		search_params.put("from", 1);
		search_params.put("size", 5);
		search_params.put("keyword", "0002-205\\/55R16");
		
		
		ElasticResponse query = searchUtils.query(0, 1554 , SearchType.DFS_QUERY_THEN_FETCH , new QueryBuilder[]{
				QueryBuilders.queryStringQuery("0002-205\\/55R16")
		}) ;
		
		Set<Object> id2s = new HashSet<Object>();
		for(Map<String,Object> data : query.getDatas()){
			id2s.add( data.get("partid"));
		}
		System.out.println( query.getTotlePage()); 
		System.out.println("---------------" +id2s.size() );
		
	}
}
