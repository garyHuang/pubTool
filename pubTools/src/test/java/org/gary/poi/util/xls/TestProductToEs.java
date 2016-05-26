package org.gary.poi.util.xls;

import java.util.Iterator;
import java.util.Map;

import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms.Bucket;
import org.elasticsearch.search.aggregations.bucket.terms.TermsBuilder;
import org.gary.comm.utils.KeyValue;
import org.gary.elasticsearch.ElasticSearchUtils;

public class TestProductToEs {
 
	
	public static long generateIndex(int productId, int sid) {
		long longId = (long) productId ;
		long longSId = (long) sid ;
		return (long) (longId + longSId * 0x10) + Integer.MAX_VALUE ;
	}
	
	public static void main(String[] args) {
		ElasticSearchUtils searchUtils = ElasticSearchUtils
				.getElasticSearch("hks", "10.163.101.230", 9300,
						"hksdata", "part", "itemid" );  
		for(int x=0;x<10;x++){
			Map<String,Object> data = new KeyValue<Object>();
			data.put("itemid", x+1);
			data.put("oecode", new String[]{"123" + x
					,  "odxd"
			}); 
			searchUtils.save(data);
		}
		SearchRequestBuilder srb = searchUtils.getClient().prepareSearch("hksdata");  
		srb.setFrom( 1 ) ;
		srb.setTypes("part" );
		srb.setSearchType(SearchType.QUERY_THEN_FETCH ); 
		
		TermsBuilder oecodeBuilder = AggregationBuilders.terms("oecodeAgg").field("relation_oecode");
		oecodeBuilder.order(Terms.Order.count( false )); 
		oecodeBuilder.size( 1000 ); 
		srb.addAggregation( oecodeBuilder ) ; 
		SearchResponse sr = srb.execute().actionGet();
		Map<String, Aggregation> aggMap = sr.getAggregations().asMap() ; 
		StringTerms stringTerms = (StringTerms)aggMap.get("oecodeAgg") ;
		Iterator<Terms.Bucket> bucketIterator = stringTerms.getBuckets().iterator();
		while(bucketIterator.hasNext()){
			Bucket next = bucketIterator.next();
			System.out.println( next.getKey() + "--->" + next.getDocCount() ) ;
		}
	}
}
