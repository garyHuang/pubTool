package org.gary.poi.util.xls;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.gary.crawler.HttpCommon;
import org.gary.mongodb.MongodbUtils;

import us.codecraft.webmagic.selector.Html;

public class TestWirteToMongdb {
	
	
	
	public static void main(String[] args) {
		SecureRandom random = new SecureRandom();
		HttpCommon common = new HttpCommon();
		int pageId = 2 ;
		MongodbUtils mongodbUtils = new MongodbUtils("qb", "115.28.184.227", 27017 );
		String nextUrl =  "http://neihanshequ.com/" ;
		while( pageId < 1000){
			pageId++;
			String htmlStr = common.requestGet( nextUrl , "UTF-8");
			Html html = new Html( htmlStr ) ; 
			List<String> bodys = html.xpath( "//h1[@class='title']/p/html()" ).all() ;
			List<String> diggs = html.xpath( "//li[@class='digg-wrapper']/span[@class='digg']/html()" ).all() ;
			List<String> burys = html.xpath( "//li[@class='bury-wrapper']/span[@class='bury']/html()" ).all() ;
			List<String> repins = html.xpath( "//li[@class='repin-wrapper']/span[@class='repin']/html()" ).all() ; 
			int size = bodys.size() ;
			System.out.println( bodys );
			for(int x=0;x<size;x++){
				Map<String,Object> qb = new HashMap<String,Object>();
				qb.put("body", bodys.get(x));
				qb.put("digg", diggs.get(x));
				qb.put("bury", burys.get(x));
				qb.put("repin", repins.get(x));
				mongodbUtils.save("joke" , qb );
				System.out.println( qb );
			}
			try {
				Thread.sleep( 5000 + random.nextInt( 5000 ) );
			} catch (Exception e) {
			}
		}
	}
}
