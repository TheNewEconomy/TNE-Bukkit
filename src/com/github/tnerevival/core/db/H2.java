package com.github.tnerevival.core.db;

import java.io.File;
import java.io.IOException;
import java.sql.DriverManager;
import java.sql.SQLException;

public class H2 extends SQLDatabase {

  private String file;
  private String user;
  private String password;

  public H2(String file, String user, String password) {
    this.file = file;
    this.user = user;
    this.password = password;
    connection = null;
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
}