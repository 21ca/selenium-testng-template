package test.template.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import test.template.common.Config;

public class DBUtils {

	public static final Connection getConnection() throws Exception {
		return getConnection(null);
	}

	public static final Connection getConnection(String prefix) throws Exception {
		if (prefix == null) {
			prefix = "";
		}

		String driver = Config.getProperty(prefix + "jdbc_driver");
		if (driver == null) {
			driver = Config.getProperty("jdbc_driver"); // Use the default
															// driver
		}
		String url = Config.getProperty(prefix + "jdbc_url");
		String user = Config.getProperty(prefix + "jdbc_user");
		String password = Config.getProperty(prefix + "jdbc_password");
		return getConnection(driver, url, user, password);
	}

	public static final Connection getConnection(String driver, String url, String user, String password)
			throws Exception {
		Class.forName(driver);
		return DriverManager.getConnection(url, user, password);
	}

	public static final List<Map<String, String>> query(String sql) throws Exception {
		return query(getConnection(), sql);
	}

	public static final List<Map<String, String>> query(Connection cn, String sql) throws Exception {
		List<Map<String, String>> result = new ArrayList<Map<String, String>>();
		Statement stm = cn.createStatement();
		ResultSet rs = stm.executeQuery(sql);
		ResultSetMetaData rsMetaData = rs.getMetaData();
		while (rs.next()) {
			Map<String, String> row = new HashMap<String, String>();
			int cols = rsMetaData.getColumnCount();

			for (int colIdx = 1; colIdx <= cols; colIdx++) {
				row.put(rsMetaData.getColumnLabel(colIdx), rs.getString(colIdx));
			}
			result.add(row);
		}

		close(rs);
		close(stm);
		return result;
	}
	
	public static final int update(Connection cn, String sql) throws Exception {
		Statement stm = cn.createStatement();
		int rows = stm.executeUpdate(sql);
		close(stm);
		return rows;
	}

	/*
	 * public static final int update(Connection cn, String sql) throws
	 * Exception { Statement stm = cn.createStatement(); int result =
	 * stm.executeUpdate(sql); close(stm); return result; }
	 */

	public static void close(Connection cn) {
		try {
			cn.close();
		} catch (Exception e) {
		}
	}

	public static void close(Statement stm) {
		try {
			stm.close();
		} catch (Exception e) {
		}
	}

	public static void close(ResultSet rs) {
		try {
			rs.close();
		} catch (Exception e) {
		}
	}
}
