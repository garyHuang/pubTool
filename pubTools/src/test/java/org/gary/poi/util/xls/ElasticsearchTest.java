package org.gary.poi.util.xls;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.settings.Settings.Builder;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;


public class ElasticsearchTest {
	
	
	public static void main(String[] args) throws Exception {
		testSave();
		
		query() ;
	}
	
	static void query(){
		QueryBuilder qb = QueryBuilders.termQuery("comment_index", "comment_ugc");
		
	}

	static void testSave() throws UnknownHostException {
		TransportClient client = getTransportClient();
		
		Map<String,Object> item = new HashMap<String,Object>();
		item.put("name", "张三");
		item.put("age", 19) ;
		item.put("remark", "你好，我是搭街坊点解啊附近") ;
		item.put("sez", 1) ;
		item.put("birthday", new Date()); 
		
		IndexRequestBuilder responseBuilder = client.prepareIndex("comment_index", "comment_ugc" , "comment_123675") ;
		IndexResponse response = responseBuilder.setSource( item ).execute().actionGet( ); 
		System.out.println( response.getId() );
	}
	
	
	 static TransportClient getTransportClient() throws UnknownHostException {
		Builder builder = Settings.builder().put("cluster.name", "hks"); 
		TransportClient client = TransportClient.builder().settings(builder).build()
				.addTransportAddress(new InetSocketTransportAddress( InetAddress.getByName("120.27.43.49") , 9300));
		return client;
	}
}
