package com.github.tnerevival.core.db;

/**
 * 
 * @author Daniel Vidmar aka creatorfromhell
 *
 */
public abstract class Database {

  public abstract Boolean connected();
  public abstract void connect();
  public abstract Object connection();
  public abstract void close();
}