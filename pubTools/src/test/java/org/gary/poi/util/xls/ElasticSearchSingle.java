package org.gary.poi.util.xls;

import org.gary.elasticsearch.ElasticSearchUtils;

public class ElasticSearchSingle {

	public static class Hloder{
		static ElasticSearchUtils SEARCHUTILS = ElasticSearchUtils
				.getElasticSearch("hks", "hksdata01", 9300,
						"hksesdata", "part", "id" );  
	}
	

	public static ElasticSearchUtils getSearchUtils(){
		
		return Hloder.SEARCHUTILS;
	}
}
