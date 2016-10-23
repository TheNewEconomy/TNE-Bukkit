package com.github.tnerevival.core.db.flat;

import java.io.Serializable;
import java.util.HashMap;

public class Entry implements Serializable {

  private static final long serialVersionUID = 1L;
  HashMap<String, Object> data = new HashMap<String, Object>();

  String name;

  public Entry(String name) {
    this.name = name;
  }

  /**
   * @return the name
   */
  public String getName() {
    return name;
  }

  /**
   * @param name the name to set
   */
  public void setName(String name) {
    this.name = name;
  }

  public void addData(String identifier, Object dataField) {
    data.put(identifier, dataField);
  }

  public HashMap<String, Object> getData() {
    return data;
  }

  public Object getData(String identifier) {
    return data.get(identifier);
  }
}