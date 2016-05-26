package org.gary.poi.util.xls;

import org.gary.elasticsearch.ElasticSearchUtils;

public class ElasticSearchSingle {

	public static class Hloder{
		static ElasticSearchUtils SEARCHUTILS = ElasticSearchUtils
				.getElasticSearch("hks", "10.163.101.230", 9300,
						"hksdata", "part", "id" );  
	}
	

	public static ElasticSearchUtils getSearchUtils(){
		
		return Hloder.SEARCHUTILS;
	}
}
