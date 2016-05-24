package org.gary.mongodb;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Vector;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
/***
 * 
 * @author Gary
 * @mail 834865081@qq.com
 * 
 */
public class MongodbUtils {
	
	protected static String _ID = "_id" ;
	/**
	 * mongdb地址
	 * */
	protected String host ; 
	/**
	 * 账号
	 * */
	protected String user ; 
	/**
	 * 密码
	 * */
	protected String pwd  ;
	/***
	 * 端口
	 */
	protected Integer port ; 
	/***
	 * 数据库名称
	 */
	protected String dbName ;
	/***
	 * mongdb客户端
	 */
	protected MongoClient mongoClient = null ;
	/***
	 * mongdb 数据库
	 */
	protected MongoDatabase mongoDatabase = null ;
	/***
	 * mongdb 集合
	 */
	protected MongoCollection<Document> mongoColl = null ;
	/**
	 * 创建一个mongodb客户端
	 * */
	public MongodbUtils(String dbName,String host,Integer port){
		this.dbName = dbName ;
		this.host = host ; 
		this.port = port ;
		mongoClient = new MongoClient(host , port );
		mongoDatabase = mongoClient.getDatabase( dbName ) ;  
	}
	/**
	 * 创建一个mongodb客户端 用户名 密码
	 * */
	public MongodbUtils(String dbName,String host,Integer port, String user , String pwd){
		this.host = host ; 
		this.dbName = dbName ;
		this.port = port ;
		this.user = user ; 
		this.pwd = pwd ; 
		MongoCredential credential = MongoCredential.createCredential( user, dbName ,  pwd.toCharArray() ) ; 
		mongoClient = new MongoClient(Arrays.asList(new ServerAddress( host ,port)), Arrays.asList(credential) ) ;  
		
		mongoDatabase = mongoClient.getDatabase( dbName ) ; 
	}
	/**
	 * 保存单个文档到mongdb
	 * */
	public void save(String tName , Map<String,Object> objs){
		Document document = new Document();
		for(String key:objs.keySet()){
			Object val = objs.get( key ) ;
			if(null != val){
				document.append(key , val );
			}
		}
		getMongoColl(tName); 
		mongoColl.insertOne( document );
	}
	/**
	 * 保存单个多个文档到mongdb
	 * */
	public void saves(String tName , List<Map<String,Object>>objs){
		List<Document> docs = new Vector<Document>();
		for(Map<String,Object> obj : objs ){
			Document doc = new Document();
			doc.putAll( obj );
			docs.add( doc );
		}
		getMongoColl(tName); 
		mongoColl.insertMany( docs ) ;
	}
	
	/**
	 * 通用查询方法
	 * */
	public List<Map<String,Object>> query(String tName , Bson bson){
		getMongoColl( tName ); 
		List<Map<String,Object>> results = new Vector<Map<String,Object>>();
		FindIterable<Document> datas = null ;
		if( bson != null){
			datas = mongoColl.find( bson ) ;
		}else{
			datas = mongoColl.find( ) ; 
		}
		MongoCursor<Document> iterator = datas.iterator() ;
		while( iterator.hasNext() ){
			Document next = iterator.next() ;
			Map<String,Object> itemMap = new HashMap<String, Object>();
			for(Entry<String, Object> entry : next.entrySet()){
				itemMap.put(entry.getKey() , entry.getValue() );
			}
			results.add( itemMap );
		}
		return results ; 
	}
	/**
	 * 修改记录
	 * */
	public void update(String tName,Map<String,Object> data , String updateKey){
		BasicDBObject filter = new BasicDBObject();
		if(_ID.equalsIgnoreCase(updateKey)){
			filter.append(updateKey, new ObjectId(String.valueOf(data.get(updateKey))) );
		}else{
			filter.append(updateKey, data.get(updateKey) );
		}
		BasicDBObject newDocument = new BasicDBObject();
		BasicDBObject update = new BasicDBObject();
		for(String key:data.keySet()){
			if(!updateKey.equals(key)){
				update.append(key, data.get( key ) ); 
			}
		}
		newDocument.append("$set", update ); 
		
		getMongoColl(tName); 
		mongoColl.updateMany( filter, newDocument ) ; 
	}
	
	protected void getMongoColl(String tName) {
		mongoColl = mongoDatabase.getCollection( tName ) ;
	}
	
}
