package com.github.tnerevival.core.db;

import java.io.File;

import com.github.tnerevival.core.db.flat.FlatFileConnection;

/**
 * 
 * @author Daniel Vidmar aka creatorfromhell
 *
 */
public class FlatFile extends Database {

  private String file;
  private FlatFileConnection connection;

  public FlatFile(String file) {
    this.file = file;
    connection = new FlatFileConnection(file);
  }

  @Override
  public Boolean connected() {
    return true;
  }

  @Override
  public void connect() {
    if(connection == null) {
      connection = new FlatFileConnection(file);
    }
  }

  @Override
  public FlatFileConnection connection() {
    if(connection == null || !connected()) {
      connect();
    }
    return connection;
  }

  @Override
  public void close() {
    connection.close();
  }

  public File getFile() {
    return new File(this.file);
  }
}