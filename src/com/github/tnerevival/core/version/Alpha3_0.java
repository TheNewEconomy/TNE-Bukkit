package com.github.tnerevival.core.version;

import java.io.File;
import java.io.IOException;

import com.github.tnerevival.core.db.MySQL;
import com.github.tnerevival.core.db.SQLite;

public class Alpha3_0 extends Version {

	@Override
	public double versionNumber() {
		return 3.0;
	}

	@Override
	public void update(double version, String type) {
		String table = prefix + "_ECOIDS";
		if(type.equalsIgnoreCase("mysql")) {
			db = new MySQL(mysqlHost, mysqlPort, mysqlDatabase, mysqlUser, mysqlPassword);
			
			mysql().executeUpdate("CREATE TABLE IF NOT EXISTS " + table + " (" +
					   "username VARCHAR(20)," +
					   "uuid VARCHAR(36)" +
					   ");");
			
			table = prefix + "_CREDITS";
			mysql().executeUpdate("CREATE TABLE IF NOT EXISTS " + table + " (" +
					   "uuid VARCHAR(36)," +
					   "credits_string VARCHAR(120)" +
					   ");");
		} else if(type.equalsIgnoreCase("sqlite")) {
			File sqliteDB = new File(sqliteFile);
			if(!sqliteDB.exists()) {
				try {
					sqliteDB.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			db = new SQLite(sqliteFile);
			
			sqlite().executeUpdate("CREATE TABLE IF NOT EXISTS " + table + " (" +
					   "username CHAR(20)," +
					   "uuid CHAR(36)" +
					   ");");

			table = prefix + "_CREDITS";
			sqlite().executeUpdate("CREATE TABLE IF NOT EXISTS " + table + " (" +
					   "uuid CHAR(36)," +
					   "credits_string CHAR(120)" +
					   ");");
		}
	}

	@Override
	public void loadFlat(File file) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void saveFlat(File file) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void loadMySQL() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void saveMySQL() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void loadSQLite() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void saveSQLite() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void loadYAML() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void saveYAML() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void createTables(String type) {
		String table = prefix + "_INFO";
		
		if(type.equalsIgnoreCase("mysql")) {
			db = new MySQL(mysqlHost, mysqlPort, mysqlDatabase, mysqlUser, mysqlPassword);
			
			mysql().executeUpdate("CREATE TABLE IF NOT EXISTS " + table + " (" +
					   "id INTEGER NOT NULL," +
					   "version VARCHAR(10)" +
					   ");");
			mysql().executeUpdate("INSERT INTO " + table + " (id, version) VALUES(1, " + versionNumber() + ");");

			table = prefix + "_USERS";
			mysql().executeUpdate("CREATE TABLE IF NOT EXISTS " + table + " (" +
					   			"uuid VARCHAR(36) NOT NULL," +
								"balances LONGTEXT," +
								"joinedDate VARCHAR(60)," +
								"accountnumber INTEGER," +
								"company VARCHAR(60)," +
								"accountstatus VARCHAR(60)," +
								"overflow LONGTEXT" +
								");");
			
			table = prefix + "_BANKS";
			mysql().executeUpdate("CREATE TABLE IF NOT EXISTS " + table + " (" +
								   "uuid VARCHAR(36) NOT NULL," +
								   "world VARCHAR(50) NOT NULL," +
								   "bank LONGTEXT" +
								   ");");
			mysql().close();
		} else {
			File sqliteDB = new File(sqliteFile);
			if(!sqliteDB.exists()) {
				try {
					sqliteDB.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			db = new SQLite(sqliteFile);
			
			sqlite().executeUpdate("CREATE TABLE IF NOT EXISTS " + table + " (" +
					   "id INTEGER NOT NULL," +
					   "version CHAR(10)" +
					   ");");
			sqlite().executeUpdate("INSERT INTO " + table + " (id, version) VALUES(1, " + versionNumber() + ");");

			table = prefix + "_USERS";
			sqlite().executeUpdate("CREATE TABLE IF NOT EXISTS " + table + " (" +
								"uuid CHAR(36) NOT NULL," +
								"balances LONGTEXT," +
								"joinedDate CHAR(60)," +
								"accountnumber INTEGER," +
								"company CHAR(60)," +
								"accountstatus CHAR(60)," +
								"overflow LONGTEXT" +
								");");
			
			table = prefix + "_BANKS";
			sqlite().executeUpdate("CREATE TABLE IF NOT EXISTS " + table + " (" +
								   "uuid CHAR(36) NOT NULL," +
								   "world CHAR(50) NOT NULL," +
								   "bank LONGTEXT" +
								   ");");
			sqlite().close();
		}
	}

}