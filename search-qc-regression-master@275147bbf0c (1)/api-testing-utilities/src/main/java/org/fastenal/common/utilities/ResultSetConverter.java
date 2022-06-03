package org.fastenal.common.utilities;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

public class ResultSetConverter {
	
	private ResultSetConverter() {
		
	}
	
	private static final Logger LOGGER = LogManager.getLogger(ResultSetConverter.class.getName());
	public static JSONArray convert(ResultSet rs) throws SQLException {
		LOGGER.debug("Converting resultset to JSONArray object");
		JSONArray json = new JSONArray();
		ResultSetMetaData rsmd = rs.getMetaData();	

		while (rs.next()) {
			int numColumns = rsmd.getColumnCount();
			JSONObject obj = new JSONObject();

			for (int i = 1; i < numColumns + 1; i++) {
				String columnName = rsmd.getColumnName(i);

				if (rsmd.getColumnType(i) == java.sql.Types.ARRAY) {
					obj.put(columnName, rs.getArray(columnName));
				} else if (rsmd.getColumnType(i) == java.sql.Types.BIGINT) {
					obj.put(columnName, rs.getInt(columnName));
				} else if (rsmd.getColumnType(i) == java.sql.Types.BOOLEAN) {
					obj.put(columnName, rs.getBoolean(columnName));
				} else if (rsmd.getColumnType(i) == java.sql.Types.BLOB) {
					obj.put(columnName, rs.getBlob(columnName));
				} else if (rsmd.getColumnType(i) == java.sql.Types.DOUBLE) {
					obj.put(columnName, rs.getDouble(columnName));
				} else if (rsmd.getColumnType(i) == java.sql.Types.FLOAT) {
					obj.put(columnName, rs.getFloat(columnName));
				} else if (rsmd.getColumnType(i) == java.sql.Types.INTEGER) {
					obj.put(columnName, rs.getInt(columnName));
				} else if (rsmd.getColumnType(i) == java.sql.Types.NVARCHAR) {
					obj.put(columnName, rs.getNString(columnName));
				} else if (rsmd.getColumnType(i) == java.sql.Types.VARCHAR) {
					obj.put(columnName, rs.getString(columnName));
				} else if (rsmd.getColumnType(i) == java.sql.Types.TINYINT) {
					obj.put(columnName, rs.getInt(columnName));
				} else if (rsmd.getColumnType(i) == java.sql.Types.SMALLINT) {
					obj.put(columnName, rs.getInt(columnName));
				} else if (rsmd.getColumnType(i) == java.sql.Types.DATE) {
					obj.put(columnName, rs.getDate(columnName));
				} else if (rsmd.getColumnType(i) == java.sql.Types.TIMESTAMP) {
					obj.put(columnName, rs.getTimestamp(columnName));
				} else {
					obj.put(columnName, rs.getObject(columnName));
				}
			}

			json.put(obj);
		}
		return json;
	}
}
