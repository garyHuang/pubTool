package org.gary.poi.util.xls;

import java.net.InetAddress;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;


public class ElasticsearchTest {
	
	
	public static void main(String[] args) throws Exception {
		TransportClient client = TransportClient.builder().build()
				.addTransportAddress(new InetSocketTransportAddress( InetAddress.getByName("120.27.43.49") , 9300));
		
		System.out.println( client.admin() );
	}
}
