package com.github.tnerevival.core.db;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * 
 * @author Daniel Vidmar aka creatorfromhell
 *
 */
public class SQLite extends Database {
	
	private String file;
	private Connection connection;
	
	public SQLite(String file) {
		this.file = file;
		connection = null;
	}

	@Override
	public Boolean connected() {
		return connection != null;
	}

	@Override
	public void connect() {
		File db = new File(file);
		if(!db.exists()) {
			try {
				db.createNewFile();
			} catch(IOException e) {
				System.out.println("Unable to create database file.");
				e.printStackTrace();
			}
		}
		try {
			Class.forName("org.sqlite.JDBC");
			connection = DriverManager.getConnection("jdbc:sqlite:" + file);
		} catch (SQLException e) {
			System.out.println("Unable to connect to MySQL.");
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			System.out.println("Unable to find JBDC File.");
			e.printStackTrace();
		}
	}

	@Override
	public Object connection() {
		return connection;
	}

	@Override
	public void close() {
		if(connected()) {
			try {
				connection.close();
				connection = null;
			} catch (SQLException e) {
				System.out.println("There was an error closing the MySQL Connection.");
				e.printStackTrace();
			}
		}
	}
}