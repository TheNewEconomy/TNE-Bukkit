package com.github.tnerevival.core.db;

import com.github.tnerevival.core.db.flat.FlatFileConnection;

import java.io.File;

/**
 * 
 * @author Daniel Vidmar aka creatorfromhell
 *
 */
public class FlatFile extends Database {

  private String file;
  private FlatFileConnection connection;

  public FlatFile(String directory, String file) {
    this(directory + File.separator + file);
  }

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