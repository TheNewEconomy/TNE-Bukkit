package com.github.tnerevival.core.db;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class H2 extends SQLDatabase {
	
	private String file;
	private String user;
	private String password;

	private Connection connection;
	
	private Statement statement;
	private PreparedStatement preparedStatement;
	
	private ResultSet result;
	private ResultSet secondary;
	
	public H2(String file, String user, String password) {
		this.file = file;
		this.user = user;
		this.password = password;
		connection = null;
		statement = null;
		preparedStatement = null;
		result = null;
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
			Class.forName("org.h2.Driver");
			connection = DriverManager.getConnection("jdbc:h2:" + file + ";mode=MySQL", user, password);
		} catch (SQLException e) {
			System.out.println("Unable to connect to H2.");
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			System.out.println("Unable to find JBDC File.");
			e.printStackTrace();
		}
	}

	@Override
	public Connection connection() {
		if(!connected()) {
			connect();
		}
		return connection;
	}

	@Override
	public void executeQuery(String query) {
		if(!connected()) {
			connect();
		}
		try {
			statement = connection().createStatement();
			result = statement.executeQuery(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void executePreparedQuery(String query, Object[] variables, boolean overwrite) {
		if(!connected()) {
			connect();
		}
		try {
			preparedStatement = connection().prepareStatement(query);
			
			for(int i = 0; i < variables.length; i++) {
				preparedStatement.setObject((i + 1), variables[i]);
			}
			if(overwrite) {
				result = preparedStatement.executeQuery();
			} else {
				secondary = preparedStatement.executeQuery();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void executeUpdate(String query) {
		if(!connected()) {
			connect();
		}
		try {
			statement = connection().createStatement();
			statement.executeUpdate(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void executePreparedUpdate(String query, Object[] variables) {
		if(!connected()) {
			connect();
		}
		try {
			preparedStatement = connection().prepareStatement(query);
			
			for(int i = 0; i < variables.length; i++) {
				preparedStatement.setObject((i + 1), variables[i]);
			}
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public ResultSet results() {
		return result;
	}

	public ResultSet secondary() {
		return secondary;
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