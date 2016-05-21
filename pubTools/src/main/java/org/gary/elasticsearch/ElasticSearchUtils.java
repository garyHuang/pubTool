package org.gary.elasticsearch;

import java.net.InetAddress;
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
	public ElasticResponse query(int from ,int size , SearchType searchType , QueryBuilder[]builders){
		SearchRequestBuilder requestBuilder = client.prepareSearch( index )
		.setTypes( type ) .setSearchType(searchType).setFrom(from)
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
	
}
