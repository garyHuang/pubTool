package org.gary.poi.util.xls;

import java.util.Map;

import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.SortBuilders;
import org.gary.elasticsearch.ElasticResponse;
import org.gary.elasticsearch.ElasticSearchUtils;

public class TestQuery {

	public static void main(String[] args) {
		ElasticSearchUtils searchUtils = ElasticSearchSingle.getSearchUtils() ;
		//QueryBuilders.queryStringQuery("奥迪")
		// QueryBuilders.prefixQuery("materialcode", "1039006")
		// QueryBuilders.matchQuery("keyword","1039006")
		//QueryBuilders.prefixQuery(  "materialcode" , "1" )
		
		ElasticResponse query = searchUtils.query(0 , 30,
				SearchType.DFS_QUERY_THEN_FETCH,
				SortBuilders.fieldSort("p1001_customertype2"),
				new QueryBuilder[] { 
			QueryBuilders.prefixQuery(  "materialcode" , "103900" )
				});
		
		for (Map<String, Object> data : query.getDatas()) {
			System.out.println(data.get("id") + "-->" + data.get("materialcode") + "--->" + data.get("materialname") );
		}

	}
}
