package org.gary.hdfs;

import java.io.OutputStream;
 

 

import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.gary.logs.LogManager;


public class HdfsApi {
	
	protected FileSystem hdfs = null;

	public static class Holder {
		
		protected static HdfsApi hdfsApi = null;
		protected static boolean isInstance = false ;
		public static HdfsApi getHdfs(){
			if(!isInstance){
				hdfsApi = new HdfsApi();
				try {
					hdfsApi.hdfs = FileSystem.get(HadoopApi.getConfig()) ;
				} catch (Exception e) { 
				}
			}
			return hdfsApi ;
		}
	}
	
	
	public static final HdfsApi getInstance() {
		return Holder.getHdfs() ;
	}
	
	public void put(String src , String dst){
		try {
			hdfs.copyFromLocalFile( true , new Path(src), new Path(dst)) ;
			
		} catch (Exception e) {
			LogManager.err( "copy Form Local File error" , e );
		}
	}
	
	
	
	
	public void mkdir(String dst){
		try {
			Path dstPath = new Path(dst) ;
			if(hdfs.exists(dstPath)){
				hdfs.mkdirs( dstPath ) ;
			}
		} catch (Exception e) {
			LogManager.err( "mkdir" , e );
		}
	}
	
	/**
	 * 从hdfs上拷贝文件
	 * */
	public void copyFileToOutput(String dst , OutputStream output){
		FSDataInputStream input = null ;
		try {
			input = hdfs.open(new Path(dst));
			IOUtils.copyBytes(input, output, 4096, true );
		} catch (Exception e) {
			LogManager.err( "copyFileToOutput" , e ); 
		}
	}
}