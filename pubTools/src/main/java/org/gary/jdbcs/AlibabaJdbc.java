package org.gary.jdbcs;

import java.sql.Connection;
import java.util.ResourceBundle;

import org.gary.logs.LogManager;

import com.alibaba.druid.pool.DruidDataSource;

public class AlibabaJdbc {
	
	protected static class Holer{
		static DruidDataSource dataSource = new DruidDataSource() ;
		private static final ResourceBundle bundle =  ResourceBundle.getBundle("alibaba-jdbc") ;
		static{
			dataSource.setDriverClassName(bundle.getString("driverClassName"));
			dataSource.setUrl(bundle.getString("jdbcUrl"));
			dataSource.setUsername(bundle.getString("username"));
			dataSource.setPassword(bundle.getString("password"));
			dataSource.setInitialSize( Integer.parseInt(bundle.getString("initialSize").trim())) ;
			dataSource.setValidationQuery( bundle.getString("validationQuery"));
			dataSource.setQueryTimeout( 60000 ) ; 
			try {
				dataSource.init() ;
			} catch (Exception e) {
				LogManager.err("init jdbc", e) ; 
			}
		}
	}
	
	public static Connection getConnection() throws Exception{
		return   Holer.dataSource.getConnection() ;
	}
}
