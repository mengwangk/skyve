package org.skyve.wildcat.tools.backup;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import javax.jcr.Node;
import javax.jcr.Session;

import org.apache.jackrabbit.core.SessionImpl;
import org.apache.jackrabbit.core.data.GarbageCollector;
import org.skyve.metadata.model.Attribute.AttributeType;
import org.skyve.wildcat.content.ContentUtil;
import org.skyve.wildcat.persistence.AbstractPersistence;
import org.skyve.wildcat.persistence.SQLImpl;
import org.skyve.wildcat.util.UtilImpl;

public class Truncate {
	private static void truncate(Collection<Table> tables, String customerName) throws Exception {
		StringBuilder sql = new StringBuilder(128);

		AbstractPersistence persistence = AbstractPersistence.get();
		try {
			persistence.begin();

			// update foreign keys to null
			for (Table table : tables) { 
				if (table instanceof JoinTable) {
					continue;
				}
				sql.setLength(0);
				sql.append("update ").append(table.name).append(" set ");
				for (String fieldName : table.fields.keySet()) {
					if (fieldName.endsWith("_id")) {
						sql.append(fieldName).append(" = null,");
					}
				}
				if (sql.charAt(sql.length() - 1) == ',') {
					sql.setLength(sql.length() - 1); // remove the comma

					BackupUtil.secureSQL(sql, table, customerName);
					persistence.executeInsecureSQLDML(new SQLImpl(sql.toString()));
				}
			}

			// delete rows from joining tables
			for (Table table : tables) {
				if (table instanceof JoinTable) {
					sql.setLength(0);
					sql.append("delete from ").append(table.name);
					BackupUtil.secureSQL(sql, table, customerName);
					UtilImpl.LOGGER.info("delete table " + table.name);
					persistence.executeInsecureSQLDML(new SQLImpl(sql.toString()));
				}
			}
			
			// delete rows from non-joining the tables
			for (Table table : tables) {
				if (table instanceof JoinTable) {
					continue;
				}
				sql.setLength(0);
				sql.append("delete from ").append(table.name);
				BackupUtil.secureSQL(sql, table, customerName);
				UtilImpl.LOGGER.info("delete table " + table.name);
				persistence.executeInsecureSQLDML(new SQLImpl(sql.toString()));
			}
		}
		finally {
			persistence.commit(true);
		}

		Session jcrSession = ContentUtil.getFullSession(customerName);
		try {
			Node rootNode = jcrSession.getRootNode();
			if (rootNode.hasNode(customerName)) {
				Node customerNode = rootNode.getNode(customerName);
				customerNode.remove();
				jcrSession.save();

				GarbageCollector gc = ((SessionImpl) jcrSession).createDataStoreGarbageCollector();
				gc.scan();
				gc.stopScan();
				gc.deleteUnused();
			}
		}
		finally {
			if (jcrSession != null) {
				jcrSession.logout();
			}
		}
	}

	private static Collection<Table> getTables(String schema)
	throws SQLException {
		Collection<Table> result = new ArrayList<>();

		try (Connection c = AbstractPersistence.get().getConnection()) {
			DatabaseMetaData dmd = c.getMetaData();
			try (ResultSet tableResultSet = dmd.getTables(c.getCatalog(), schema, "%", null)) {
				while (tableResultSet.next()) {
					String tableName = tableResultSet.getString("TABLE_NAME");
					tableName = tableName.toLowerCase();
					// ignore content tables as they will be truncated through the CMS
					if (tableName.startsWith("content_")) {
						continue;
					}
					
					Table table = new Table(tableName);
					try (ResultSet columnResultSet = dmd.getColumns(c.getCatalog(), schema, tableName, null)) {
						while (columnResultSet.next()) {
							String columnName = columnResultSet.getString("COLUMN_NAME");
							if (columnName.toLowerCase().endsWith("_id")) {
								table.fields.put(columnName, AttributeType.text);
							}
						}
					}
					
					// remove rows from joining tables
					if ((table.fields.size() == 2) && 
							table.fields.containsKey("owner_id") && 
							table.fields.containsKey("element_id")) {
						String ownerTableName = null;
						try (ResultSet foreignKeyResultSet = dmd.getImportedKeys(c.getCatalog(), schema, tableName)) {
							while (foreignKeyResultSet.next()) {
								String foreignKeyColumn = foreignKeyResultSet.getString("FKCOLUMN_NAME");
								if ("owner_id".equals(foreignKeyColumn.toLowerCase())) {
									ownerTableName = foreignKeyResultSet.getString("PKTABLE_NAME");
									ownerTableName = ownerTableName.toLowerCase();
								}
							}
						}
						if (ownerTableName == null) { // is null when the foreign key is not defined
							table = null; // skip this table
						}
						else {
							table = new JoinTable(tableName, ownerTableName);
						}
					}
					
					if (table != null) {
						result.add(table);
					}
				}
			}
		}

		return result;
	}
	
	public static void main(String[] args) throws Exception {
		if (args.length != 8) {
			System.err.println("args are <customerName> <content directory> <DB dialect> <DB driver> <DB URL> <DB username> <DB password> <DB schema>");
			System.exit(1);
		}
		BackupUtil.initialize(args[0], args[1], args[2], args[3], args[4], args[5], args[6]);
//		Collection<Table> tables = BackupUtil.getTables();
		Collection<Table> tables = getTables(args[7]);
		truncate(tables, args[0]);
	}
}
