package test.template.utils;

import java.sql.Connection;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import test.template.context.GlobalContext;

/**
 * Backup Example:
 * 1. Connection cn = DBUtils.getConnection();
 * 2. String backupId = DBRestoreUtils.backup(cn, "SELECT name, age FROM users WHERE id=1");
 * 3. LocalContext.put("backupId", backupId);
 * 
 * Restore Example:
 * 1. Connection cn = DBUtils.getConnection();
 * 2. String backupId = (String)LocalContext.get("backupId");
 * 3. DBRestoreUtils.restore(cn, backupId);
 * 4. DBRestoreUtils.deleteBackup(backupId);
 */
public class DBRestoreUtils {
	protected static Logger LOG = LoggerFactory.getLogger(DBRestoreUtils.class);

	public static String backup(Connection cn, String querySql) throws Exception {
		List<Map<String, String>> data = DBUtils.query(cn, querySql);
		String backupId = UUID.randomUUID().toString();
		GlobalContext.put(backupId + "_data", data);
		GlobalContext.put(backupId + "_sql", querySql);
		LOG.info("Backup " + data.size() + " records.");
		return backupId;
	}
	
	public static void deleteBackup(String backupId) throws Exception {
		GlobalContext.remove(backupId + "_data");
		GlobalContext.remove(backupId + "_sql");
	}

	
	@SuppressWarnings("unchecked")
	public static boolean restore(Connection cn, String backupId) throws Exception {
		List<Map<String, String>> data = (List<Map<String, String>>) GlobalContext.get(backupId + "_data");
		String querySql = (String) GlobalContext.get(backupId + "_sql");
		
		if (data == null || querySql == null || data.isEmpty() || querySql.isEmpty()) {
			LOG.info("Backup data not found.");
			return false;
		}
		
		for (Map<String, String> line : data) {
			int count = DBUtils.update(cn, buildUpdateSql(line, querySql));
			LOG.info("Restore " + count + " records.");
		}
		
		return false;
	}

	private static String buildUpdateSql(Map<String, String> line, String querySql) {
		String whereCriteria = querySql.substring(querySql.toUpperCase().indexOf("WHERE"));
		String table = getTableName(querySql);

		StringBuilder sb = new StringBuilder();
		sb.append("UPDATE " + table + " ");
		sb.append("SET ");
		for (String key : line.keySet()) {
			String value = line.get(key);
			sb.append(key);
			sb.append("=");
			if (value == null) {
				sb.append("NULL");
				sb.append(", ");
			} else {
				if (isTimeStamp(value)) {
					sb.append("TIMESTAMP");
				}
				sb.append("'");
				sb.append(value);
				sb.append("', ");
			}
		}
		sb.deleteCharAt(sb.lastIndexOf(",")); //delete last ","
		sb.append(whereCriteria);
		LOG.info("Execute restore update: " + sb.toString());
		return sb.toString();
	}

	//Oracle Time stamp 
	private static boolean isTimeStamp(String value) {
		return value.matches("\\d{4}-\\d{2}-\\d{2}\\s\\d{2}:\\d{2}:\\d{2}.*");

	}

	//querySql: SELECT A, B FROM table1 WHERE A=1...
	private static String getTableName(String querySql) {
		int start = querySql.toUpperCase().indexOf("FROM") + 4;
		int end = querySql.toUpperCase().indexOf("WHERE") - 1;
		return querySql.substring(start, end).trim();
	}
}
