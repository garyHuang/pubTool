package org.gary.dbs;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.apache.commons.lang3.ArrayUtils;
import org.gary.comm.utils.KeyValue;
import org.gary.jdbcs.AlibabaJdbc;
import org.gary.logs.LogManager;

import com.mchange.v1.db.sql.ConnectionUtils;

/**
 * <br>
 * <b>功能：</b>数据库操作工具<br>
 * <b>作者：</b>黄飞<br>
 * <b>日期：</b> 2014-3-5 <br>
 * <b>版权所有：<b>版权所有(C) 2014，QQ <br>
 */
public class DBUtils implements Serializable {

	public static final String DB_DRIVER = "driver";

	public static final String DB_PASSWORD = "password";

	public static final String DB_URL = "url";
	public static final String DB_USERNAME = "username";
	private static final long serialVersionUID = 7614285289075969240L;

	public static DBUtils getDBUtil() {
		DBUtils utils = new DBUtils();
		return utils;
	}

	Connection conn = null;
	PreparedStatement ps = null;
	ResultSet rs = null;

	/**
	 * 关闭资源
	 */
	public void close(Connection conn, PreparedStatement ps, ResultSet rs) {
		if (rs != null) {
			try {
				rs.close();
			} catch (Exception e) {
			}
		}
		if (ps != null) {
			try {
				ps.close();
			} catch (Exception e) {
			}
		}
		if (conn != null) {
			try {
				ConnectionUtils.attemptClose(conn);
			} catch (Exception e) {
			}
		}
	}

	private void createPs(String sql) throws SQLException {
		conn = getConnection();
		ps = conn.prepareStatement(sql);
	}

	public Connection getConnection() {
		try {
			/*
			 * Class.forName( resourceBundle.getString(DB_DRIVER)) ; return
			 * DriverManager.getConnection(resourceBundle.getString(DB_URL ) ,
			 * resourceBundle.getString(DB_USERNAME) ,
			 * resourceBundle.getString(DB_PASSWORD )) ;
			 */
			return AlibabaJdbc.getConnection();

		} catch (Exception e) {
			LogManager.err(e);
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 查询返回所有结果集 最多返回1000条数据
	 */
	public List<Map<String, Object>> getResults(String sql, Object... param) {
		List<Map<String, Object>> results = new Vector<Map<String, Object>>();
		try {
			createPs(sql);
			setParam(ps, param);
			rs = ps.executeQuery();
			ResultSetMetaData rsmd = rs.getMetaData();
			String[] columns = null;
			int columnCount = rsmd.getColumnCount();
			for (int x = 0; x < columnCount; x++) {
				columns = ArrayUtils.add(columns, rsmd.getColumnLabel(x + 1));
			}
			while (rs.next()) {
				Map<String, Object> map = new KeyValue<Object>(); 
				for (int x = 0; x < columnCount; x++) {
					map.put(columns[x], rs.getObject(x + 1));
				}
				results.add(map);
			}
		} catch (Exception e) {
			LogManager.err(e);
			throw new RuntimeException(e);
		} finally {
			close(conn, ps, rs);
		}
		return results;
	}

	/**
	 * 插入表，如果该表是存在自动增长的列，则返回 自动增长的的数据
	 */
	public Integer insertAutoIncrementTable(String sql, Object... param) {
		try {
			conn = getConnection();
			LogManager.info("sql:" + sql);
			/* 执行预处理语句，设置一个自增长列 */
			ps = conn.prepareStatement(sql, 1);

			setParam(ps, param);
			ps.executeUpdate();
			rs = ps.getGeneratedKeys();
			while (rs.next()) {
				return rs.getInt(1);
			}
			return 0;
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			close(conn, ps, rs);
		}
	}

	/**
	 * 给预处理设置参数
	 */
	private void setParam(PreparedStatement ps, Object... param)
			throws SQLException {
		for (int x = 0; x < param.length; x++) {
			ps.setObject(x + 1, param[x]);
		}
	}
	
	
	
}
