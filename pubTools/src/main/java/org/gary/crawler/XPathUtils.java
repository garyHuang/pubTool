package org.gary.crawler;

import java.util.List;

import org.gary.logs.LogManager;


import us.codecraft.webmagic.selector.Html;

public class XPathUtils {
	
	public static void main(String[] args) throws Exception {
		
		String htmlStr = HttpUtils.requestGet("http://neihanshequ.com/", "UTF-8");
		Html html = new Html( htmlStr ) ; 
		//List<String> titles = html.xpath( "//div[@class='post_item_body']/h3/a/text()" ).all() ;
		List<String> titles = html.xpath( "//span[@class='name']/text()" ).all() ;
		
		//List<String> bodys = html.xpath( "//div[@class='post_item_body']/p[@class='post_item_su	mmary']/text()" ).all() ;
		List<String> bodys = html.xpath( "//h1[@class='title']/p/html()" ).all() ;
		List<String> diggs = html.xpath( "//li[@class='digg-wrapper']/span[@class='digg']/html()" ).all() ;
		List<String> burys = html.xpath( "//li[@class='bury-wrapper']/span[@class='bury']/html()" ).all() ;
		List<String> repins = html.xpath( "//li[@class='repin-wrapper']/span[@class='repin']/html()" ).all() ;
		
		int size = titles.size();
		for(int x=0; x<size; x++ ){
			String title = titles.get( x ) ; 
			String body = bodys.get( x );
			String digg = diggs.get( x );
			String bury = burys.get( x );
			String repin = repins.get( x );
			LogManager.info("\n" +  title    + "-->" + digg   + "-->" + bury + "-->" + repin + "-->" + body) ; 
		}
	}
	
}
