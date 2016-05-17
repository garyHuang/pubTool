package org.gary.crawler;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Vector;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.gary.comm.utils.UrlUtil;
import org.gary.io.utils.StreamUtils;
import org.gary.logs.LogManager;

import com.alibaba.fastjson.JSON;

public class HttpCommon {
	/**
	 * cookie name键值对
	 * */
	public static class CookieNameValuePair{
		
		private String name ; 
		
		private String value ; 
		
		private String domain;
		

		private String expires;
		
		private String path ;
		
		public String trim(String value){
			if(value == null){
				return "" ;
			}
			return value.trim() ;
		}
		
		public CookieNameValuePair(String name, String value) {
			this.name = name;
			this.value = value;
		}

		public CookieNameValuePair(String cookie) {
			String[] cookieSplits = cookie.split(";");
			for(String cookieSplit:cookieSplits){
				String[] heads = split( cookieSplit ) ;
				if(null != heads){
					String head0 = trim(heads[0]); 
					String head1 = trim(heads[1]);
					if(Holder.RESPONSE_COOKIE_PATH.equalsIgnoreCase( head0 ) ){
						this.path = head1; 
					}else if(Holder.RESPONSE_COOKIE_DOMAIN.equalsIgnoreCase( head0 )){
						this.domain = head1 ;
					}else if(Holder.RESPONSE_COOKIE_EXPIRES.equalsIgnoreCase(head0)){
						this.expires = head1 ; 
					}else{
						this.name = head0 ;
						this.value = head1 ;
					}
				}
			}
		}
		
		protected String[] split(String key){
			if(null != key){
				int indexOf = key.indexOf( "=" ); 
				if(indexOf>-1){
					String head = key.substring( 0 , indexOf ) ;
					String end = key.substring( indexOf + 1 ) ;
					return new String[]{head , end };
				}
			}
			return null ;
		}

		public String getName() {
			return name;
		}

		public String getDomain() {
			return domain;
		}

		public String getExpires() {
			return expires;
		}
		public String getValue() {
			return value;
		}
		
		public String getPath() {
			return path;
		}
		
		public String toEq(){
			return String.format("%s=%s", this.name , this.value );
		}
	}
	
	public static class Holder{
		protected final static Header HEADER_USER_AGENT = new BasicHeader("user-agent","Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2700.0 Safari/537.36");
		protected final static Header HEADER_CONNECTION = new BasicHeader("connection", "keep-alive");
		protected final static Header HEADER_CACHE_CONTROL = new BasicHeader("cache-control", "max-age=0");
		protected final static Header HEADER_ACCEPT = new BasicHeader("accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
		protected final static Header HEADER_ACCEPT_ENCODING = new BasicHeader("accept-encoding", "gzip, deflate, sdch");
		protected final static Header HEADER_ACCEPT_LANGUAGE = new BasicHeader("accept-language", "zh-CN,zh;q=0.8,en;q=0.6"); 
		protected final static String RESPONSE_COOKIE = "Set-Cookie" ;
		protected final static String REQUEST_COOKIE = "Cookie" ;
		protected final static String RESPONSE_COOKIE_DOMAIN = "Domain" ;
		protected final static String RESPONSE_COOKIE_EXPIRES = "expires" ;
		protected final static String RESPONSE_COOKIE_PATH = "Path" ;
	}
	
	public static HttpCommon getCommon() {
		return new HttpCommon();
	}
	
	public List<CookieNameValuePair> cookies = new Vector<CookieNameValuePair>();
	
	protected String host; 

	public String requestGet(String url , String charSet){
		CloseableHttpClient httpclient = HttpClients.createDefault();  
		CloseableHttpResponse response = null ;
		try {
			host = UrlUtil.getHost( url ) ;
			HttpGet httpget = new HttpGet( url );  
			httpget.setHeader( Holder.HEADER_USER_AGENT ) ;
			httpget.setHeader( Holder.HEADER_CONNECTION ) ;
			httpget.setHeader( Holder.HEADER_CACHE_CONTROL ) ;
			httpget.setHeader( Holder.HEADER_ACCEPT ) ;
			httpget.setHeader( Holder.HEADER_ACCEPT_ENCODING);
			httpget.setHeader( Holder.HEADER_ACCEPT_LANGUAGE); 
			StringBuffer buffer = new StringBuffer(); 
			for(CookieNameValuePair nameValuePair:cookies){
				if(buffer.length() > 0){
					buffer.append(";");
				}
				buffer.append(nameValuePair.toEq());
			}
			if(buffer.length() > 0){
				httpget.setHeader( new BasicHeader(Holder.REQUEST_COOKIE, buffer.toString() ) ); 
			}
			response = httpclient.execute( httpget ) ;
			Header[] allHeaders = response.getAllHeaders();
			cookies.clear(); 
			for(Header header : allHeaders){
				String name = header.getName( );
				if( name.indexOf(Holder.RESPONSE_COOKIE) > -1){
					cookies.add( new CookieNameValuePair( header.getValue() ) );
				}
			}
			if(response.getStatusLine().getStatusCode() == 200){
				HttpEntity entity = response.getEntity() ;
				return inputStreamToString( entity , charSet );
			}
		} catch (Exception e) {
			LogManager.err("requestGet:" + url , e) ;
		}finally{
			try {
				response.close();
				httpclient.close();
			} catch (Exception e) {
			}
		}
		return "" ;
	}
	
	
	/**
	 * 将inputStream转换成为，String字符串
	 * */
	private String inputStreamToString(HttpEntity entity , String charSet)
			throws Exception{
		InputStream is = null ; 
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			is = entity.getContent() ;
			byte[]bytes = new byte[52428800] ;
			int buffer = -1;
			
			while( (buffer = is.read(bytes)) != -1){
				baos.write(bytes, 0, buffer) ;
			}
			return new String(baos.toByteArray() , charSet ) ;
		}finally{
			StreamUtils.closeInput( is );
			StreamUtils.closeOutput( baos );
		}
	}
	
	
	public List<CookieNameValuePair> getCookies() {
		return cookies;
	}
	
	public void addCookie(CookieNameValuePair cookie) {
		this.cookies.add( cookie );
	}
	
	public static void main(String[] args) {
		HttpCommon common = HttpCommon.getCommon();
		common.addCookie(new CookieNameValuePair("JSESSIONID" , "C5ED915DAF2E32AFFE42E0AD3FAF987B"));
		String requestGet = common.requestGet("http://localhost:8080/test01", "UTF-8" );
		System.out.println( requestGet );
		
		System.out.println("------------------------");
		System.out.println( JSON.toJSON(common.cookies));
	}
}
