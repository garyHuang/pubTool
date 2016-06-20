package org.gary.poi.util.xls;

import java.util.Iterator;
import java.util.Map;

import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms.Bucket;
import org.elasticsearch.search.aggregations.bucket.terms.TermsBuilder;
import org.gary.elasticsearch.ElasticSearchUtils;

public class TestQueryGroup {
	
	public static void main(String[] args) {
		ElasticSearchUtils searchUtils = ElasticSearchSingle.getSearchUtils(); 
		SearchRequestBuilder srb = searchUtils.getClient().prepareSearch("hksesdata");  
		srb.setTypes("part"); 
		srb.setSearchType( SearchType.QUERY_AND_FETCH ); 
		srb.setQuery( QueryBuilders.matchAllQuery() );
		TermsBuilder  partidTermsBuilder = AggregationBuilders.terms("oecodeAgg").field("relation_postion") ;
		partidTermsBuilder.size( 100 );
		srb.addAggregation( partidTermsBuilder ) ;
		
		SearchResponse sr = srb.execute().actionGet();
		
		Map<String, Aggregation> aggMap = sr.getAggregations().asMap() ; 
		StringTerms partidTerms = (StringTerms) aggMap.get("oecodeAgg") ; 
		Iterator<Bucket> partIdBucketIt = partidTerms.getBuckets().iterator();
		while(partIdBucketIt.hasNext()){
			Bucket bucket = partIdBucketIt.next() ; 
			System.out.println( bucket.getKeyAsString() + "--->" + bucket.getDocCount() ); 
		}
		System.out.println( partidTerms.getBuckets().size() );
		System.out.println( sr.getTookInMillis() );
	}
}
