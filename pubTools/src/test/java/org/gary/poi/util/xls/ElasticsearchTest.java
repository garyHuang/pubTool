package org.gary.poi.util.xls;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.elasticsearch.action.delete.DeleteRequestBuilder;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.settings.Settings.Builder;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;

public class ElasticsearchTest {

	public static void main(String[] args) throws Exception {
		testUpdate() ;
		testSave();

		query();
	}

	static void query() throws Exception {
		TransportClient client = getTransportClient();
		SearchResponse response = client.prepareSearch("comment_index")
				.setTypes("comment_ugc")
				.setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
				.setQuery(QueryBuilders.termQuery("remark", "你"))
				.setQuery(QueryBuilders.termQuery("sez", "1"))
				.setFrom(0).setSize(60).setExplain(true).execute().actionGet();
		SearchHits searchHits = response.getHits();
		System.out.println(searchHits.getTotalHits());
		SearchHit[] hits = searchHits.getHits();
		for (SearchHit hit : hits) {
			Map<String, Object> source = hit.getSource();
			for (Map.Entry<String, Object> entry : source.entrySet()) {
				System.out.println(entry.getKey() + "-->" + entry.getValue());
			}
		}
	}

	static void testSave() throws UnknownHostException {
		TransportClient client = getTransportClient(); 
		Map<String, Object> item = new HashMap<String, Object>();
		item.put("name", "张三");
		item.put("age", 19);
		item.put("remark", "你好，我是奥迪");
		item.put("sez", 1);
		item.put("birthday", new Date());

		IndexRequestBuilder responseBuilder = client.prepareIndex(
				"comment_index", "comment_ugc", "comment_123688");
		IndexResponse response = responseBuilder.setSource(item).execute()
				.actionGet();
		System.out.println(response.getId());
	}
	
	
	static  void testUpdate () throws Exception{
		TransportClient client = getTransportClient();
		DeleteRequestBuilder prepareDelete = client.prepareDelete( "comment_index", "comment_ugc", "comment_123677" ) ;
		DeleteResponse actionGet = prepareDelete.execute().actionGet();
		System.out.println( actionGet.getId() ); 
	}

	static TransportClient getTransportClient() throws UnknownHostException {
		Builder builder = Settings.builder().put("cluster.name", "hks");
		TransportClient client = TransportClient
				.builder()
				.settings(builder)
				.build()
				.addTransportAddress(
						new InetSocketTransportAddress(InetAddress
								.getByName("10.163.101.230"), 9300));
		return client;
	}
	
	
}
