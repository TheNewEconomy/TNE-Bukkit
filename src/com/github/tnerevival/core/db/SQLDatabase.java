package com.github.tnerevival.core.db;

import java.sql.*;
import java.util.TreeMap;

public abstract class SQLDatabase extends Database {

  private TreeMap<Integer, SQLResult> results = new TreeMap<>();
  protected Connection connection;

  @Override
  public abstract void connect();

  @Override
  public Boolean connected() {
    return connection != null;
  }

  @Override
  public Connection connection() {
    if(!connected()) {
      connect();
    }
    return connection;
  }

  public int executeQuery(String query) {
    if(!connected()) {
      connect();
    }
    try {
      Statement statement = connection().createStatement();
      return addResult(statement, statement.executeQuery(query));
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return -1;
  }

  public int executePreparedQuery(String query, Object[] variables) {
    if(!connected()) {
      connect();
    }
    try {
      PreparedStatement statement = connection().prepareStatement(query);
      for(int i = 0; i < variables.length; i++) {
        statement.setObject((i + 1), variables[i]);
      }
      return addResult(statement, statement.executeQuery());
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return -1;
  }

  public void executeUpdate(String query) {
    if(!connected()) {
      connect();
    }
    try {
      Statement statement = connection().createStatement();
      statement.executeUpdate(query);
      statement.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public void executePreparedUpdate(String query, Object[] variables) {
    if(!connected()) {
      connect();
    }
    try {
      PreparedStatement prepared = connection().prepareStatement(query);

      for(int i = 0; i < variables.length; i++) {
        prepared.setObject((i + 1), variables[i]);
      }
      prepared.executeUpdate();
      prepared.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public int addResult(Statement statement, ResultSet resultSet) {
    SQLResult result = new SQLResult(results.lastKey() + 1, statement, resultSet);
    results.put(result.getId(), result);
    return result.getId();
  }

  public ResultSet results(int id) {
    return results.get(id).getResult();
  }

  public void closeResult(int id) {
    results.get(id).close();
  }

  @Override
  public void close() {
    if(connected()) {
      try {

        for(SQLResult result : results.values()) {
          result.close();
        }
        connection.close();
        connection = null;
      } catch (SQLException e) {
        System.out.println("There was an error closing the MySQL Connection.");
        e.printStackTrace();
      }
    }
  }

  public static Integer boolToDB(boolean value) {
    return (value)? 1 : 0;
  }

  public static Boolean boolFromDB(int value) {
    return (value == 1);
  }
}