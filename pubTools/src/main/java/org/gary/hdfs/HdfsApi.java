package org.gary.hdfs;

import java.io.IOException;

import org.apache.hadoop.fs.FileSystem;
import org.gary.logs.LogManager;



public class HdfsApi {
	
	protected static FileSystem fs = null ;
	
	public void createFS(){
		try {
			fs = FileSystem.get(HadoopApi.getConfig()) ;
		} catch (IOException e) {
			LogManager.err( "创建fs出现错误" , e);
		}
	}
	
	
}
