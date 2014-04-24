package com.github.tnerevival.core.db;

import java.sql.Connection;
import java.sql.ResultSet;

public abstract class SQLDatabase extends Database {

	@Override
	public abstract void connect();

	@Override
	public abstract Connection connection();
	
	public abstract void executeQuery(String query);
	
	public abstract void executePreparedQuery(String query, Object[] variables);
	
	public abstract void executeUpdate(String query);
	
	public abstract void executePreparedUpdate(String query, Object[] variables);
	
	public abstract ResultSet results();
}