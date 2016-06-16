package org.gary.elasticsearch;

import java.net.InetAddress;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.settings.Settings.Builder;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms.Bucket;
import org.elasticsearch.search.aggregations.bucket.terms.TermsBuilder;
import org.elasticsearch.search.sort.SortBuilder;
import org.gary.comm.utils.KeyValue;
import org.gary.comm.utils.TransformUtils;

public class ElasticSearchUtils {
	
	protected TransportClient client ; 
	
	protected String index ; 
	
	protected String type ; 
	
	protected String idKey ;
	/**
	 * 创建TransportClient
	 * @param clusterName 集群名称
	 * @param host 地址
	 * @param port es端口 , 末日为9300
	 * */
	TransportClient getTransportClient(String clusterName , String host , Integer port) throws  Exception {
		Builder builder = Settings.builder().put("cluster.name",  clusterName ) ;
		TransportClient client = TransportClient
				.builder()
				.settings(builder)
				.build()
				.addTransportAddress(
						new InetSocketTransportAddress(InetAddress
								.getByName( host ), port));
		return client ; 
	}
	/**
	 * 创建TransportClient
	 * @param clusterName 集群名称
	 * @param host 地址
	 * @param port es端口 , 末日为9300
	 * @param index 索引
	 * @param type 类型
	 * @param idKey 主名称
	 * */
	public static ElasticSearchUtils getElasticSearch(String clusterName
			 , String host , Integer port , String index , String type , String idKey){
		try {
			ElasticSearchUtils search = new ElasticSearchUtils();
			search.client = search.getTransportClient(clusterName, host, port) ;
			search.idKey = idKey ;
			search.index = index ; 
			search.type = type ; 
			return search ; 
		} catch(Exception e ) {
			throw new RuntimeException( e.getMessage() , e )  ;
		}
	}
	/**
	 * 分页查询方法
	 * */
	public ElasticResponse query(int from ,int size , SearchType searchType , SortBuilder sortBuilder
			, QueryBuilder[]builders){
		SearchRequestBuilder requestBuilder = client.prepareSearch( index )
		.setTypes( type ) .setSearchType(searchType).setFrom(from)
		.setSize(size) ;
		for(QueryBuilder builder:builders){
			requestBuilder.setQuery(builder);
		}
		if(null != sortBuilder){
			requestBuilder.addSort( sortBuilder );
		}
		SearchResponse actionGet = requestBuilder.setExplain(true).execute().actionGet();
		ElasticResponse elasticResponse = new ElasticResponse();
		SearchHits searchHits =  actionGet.getHits() ;
		elasticResponse.setTotlePage( searchHits.getTotalHits() );
		SearchHit[] hits = searchHits.getHits();
		for (SearchHit hit : hits) {
			elasticResponse.getDatas().add( hit.getSource() );
		}
		return elasticResponse;
	}
	
	
	/**
	 * 分页查询方法
	 * */
	public ElasticResponse queryGroup(int from ,int size ,   QueryBuilder[]builders){
		SearchRequestBuilder requestBuilder = client.prepareSearch( index )
		.setTypes( type ) .setSearchType(SearchType.QUERY_THEN_FETCH ).setFrom(from)
		.setSize(size) ;
		for(QueryBuilder builder:builders){
			requestBuilder.setQuery(builder);
		}
		SearchResponse actionGet = requestBuilder.setExplain(true).execute().actionGet();
		ElasticResponse elasticResponse = new ElasticResponse();
		SearchHits searchHits =  actionGet.getHits() ;
		elasticResponse.setTotlePage( searchHits.getTotalHits() );
		SearchHit[] hits = searchHits.getHits();
		for (SearchHit hit : hits) {
			elasticResponse.getDatas().add( hit.getSource() );
		}
		return elasticResponse;
	}
	/**
	 * 保存数据到集群环境中
	 * */
	public String save( Map<String, Object> data  ){
		Object keyObj = data.get(idKey ) ; 
		String strKey = null == keyObj ? "" : keyObj.toString() ;
		
		IndexRequestBuilder responseBuilder = client.prepareIndex(
				index , type , strKey );
		IndexResponse response = responseBuilder.setSource( data ).execute()
				.actionGet() ;
	  return response.getId() ; 
	}
	
	public  Map<String , Long > getGroup(QueryBuilder[]builders , 
			String groupKey ){
		Map<String ,  Long > resultMap = new KeyValue< Long>(); 
		TermsBuilder  aggBuilder = AggregationBuilders.terms("agg").field( groupKey ) ; 
		SearchRequestBuilder requestBuilder = client.prepareSearch( index )
				.setTypes( type ) .setSearchType(SearchType.QUERY_THEN_FETCH ).setFrom( 0 )
				.setSize( 200 ) ;
		requestBuilder.addAggregation(  aggBuilder  ) ; 
		SearchResponse sr = requestBuilder.execute().actionGet();
		Map<String, Aggregation> aggMap = sr.getAggregations().asMap() ; 
		if(null == aggMap){
			return resultMap ; 
		}
		StringTerms aggTerms = (StringTerms) aggMap.get("oecodeAgg") ;
		if(null == aggTerms){
			return resultMap ; 
		}
		List<Bucket> buckets = aggTerms.getBuckets();
		if(null == buckets){
			return resultMap;
		}
		Iterator<Bucket> partIdBucketIt = buckets.iterator();
		while(partIdBucketIt.hasNext()){
			Bucket bucket = partIdBucketIt.next() ; 
			resultMap.put(TransformUtils.toString(bucket.getKey() ) , bucket.getDocCount());
		}
		return resultMap ;  
	}
	
	
	public TransportClient getClient() {
		return client;
	}
}
