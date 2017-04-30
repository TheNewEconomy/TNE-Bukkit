package com.github.tnerevival.core.configurations.file;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by creatorfromhell on 4/30/2017.
 * All rights reserved.
 **/
public class ConfigurationValue {
  private Object value;

  public ConfigurationValue(Object value) {
    this.value = value;
  }

  public Float getFloat() {
    return (Float)value;
  }

  public boolean isFloat() {
    return value instanceof Float;
  }

  public Double getDouble() {
    return (Double)value;
  }

  public boolean isDouble() {
    return value instanceof Double;
  }

  public BigDecimal getBigDecimal() {
    return(BigDecimal)value;
  }

  public boolean isBigDecimal() {
    return value instanceof BigDecimal;
  }

  public Byte getByte() {
    return (Byte)value;
  }

  public boolean isByte() {
    return value instanceof Byte;
  }

  public Short getShort() {
    return (Short)value;
  }

  public boolean isShort() {
    return value instanceof Short;
  }

  public Integer getInteger() {
    return (Integer)value;
  }

  public boolean isInteger() {
    return value instanceof Integer;
  }

  public Long getLong() {
    return (Long)value;
  }

  public boolean isLong() {
    return value instanceof Long;
  }

  public Boolean getBoolean() {
    return (Boolean)value;
  }

  public boolean isBoolean() {
    return value instanceof Boolean;
  }

  public String getString() {
    return (String)value;
  }

  public boolean isString() {
    return value instanceof String;
  }

  public List<?> getList() {
    return ((value instanceof List)? (List)value : null);
  }

  public boolean isList() {
    return value instanceof List;
  }
}