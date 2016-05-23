package org.gary.hdfs;

import org.apache.hadoop.conf.Configuration;

public class HadoopApi {
	public static class Holer{
		public static Configuration CON = new Configuration();
	}
	public static Configuration getConfig(){
		return Holer.CON;
	}
}
