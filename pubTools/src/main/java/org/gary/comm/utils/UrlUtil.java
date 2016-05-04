package org.gary.comm.utils;


public class UrlUtil {
	
	public static String getHost(String url){
		String host = "" ;
		int index = url.indexOf("://");
		if(index>-1){
			url = url.substring( index + 3) ; 
			int indexOf = url.indexOf("/");
			if(indexOf>-1){
				host = url.substring(0 , indexOf) ;
			}else{
				host = url ;
			}
		}
		return host ;
	}
	
	public static void main(String[] args) {
		org.gary.logs.LogManager.info( getHost("http://news.sohu.com/20160504/n447500580.shtml"));
	}
}
