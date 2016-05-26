package org.gary.poi.util.xls;

import java.util.Iterator;
import java.util.Map;

import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.LongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms.Bucket;
import org.elasticsearch.search.aggregations.bucket.terms.TermsBuilder;
import org.gary.elasticsearch.ElasticSearchUtils;

public class TestQueryGroup {
	
	public static void main(String[] args) {
		ElasticSearchUtils searchUtils = ElasticSearchUtils
				.getElasticSearch("hks", "10.163.101.230", 9300,
						"haokuaisheng", "part", "partid");
		
		SearchRequestBuilder srb = searchUtils.getClient().prepareSearch("haokuaisheng");  
		srb.setFrom( 1 ) ;
		srb.setTypes("part"); 
		srb.setSearchType(SearchType.QUERY_THEN_FETCH );
		
		TermsBuilder partidTermsBuilder = AggregationBuilders.terms("partidAgg").field("partid");
		partidTermsBuilder.size( 10 );
		partidTermsBuilder.shardSize( 55);
		//TermsBuilder rowIndexTermsBuilder = AggregationBuilders.terms("rowIndexAgg").field("rowIndex");
		//partidTermsBuilder.subAggregation(rowIndexTermsBuilder);
		srb.addAggregation( partidTermsBuilder ) ;
		SearchResponse sr = srb.execute().actionGet();
		Map<String, Aggregation> aggMap = sr.getAggregations().asMap() ; 
		LongTerms partidTerms = (LongTerms) aggMap.get("partidAgg") ; 
		Iterator<Terms.Bucket> partIdBucketIt = partidTerms.getBuckets().iterator();
		while(partIdBucketIt.hasNext()){
			Bucket bucket = partIdBucketIt.next() ; 
			System.out.println( bucket.getKey() + "--->" + bucket.getDocCount() ); 
		}
		System.out.println( partidTerms.getBuckets().size() );
	}
}
