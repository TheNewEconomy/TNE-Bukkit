package net.tnemc.core.common.menu;

import net.tnemc.core.TNE;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


/**
 * Created by creatorfromhell.
 *
 * The New Plugin Core Minecraft Server Plugin
 *
 * All rights reserved.
 *
 * Some Details about what is acceptable use of this software:
 *
 * This project accepts user contributions.
 *
 * Direct redistribution of this software is not allowed without written permission. However,
 * compiling this project into your software to utilize it as a library is acceptable as long
 * as it's not used for commercial purposes.
 *
 * Commercial usage is allowed if a commercial usage license is bought and verification of the
 * purchase is able to be provided by both parties.
 *
 * By contributing to this software you agree that your rights to your contribution are handed
 * over to the owner of the project.
 */

/**
 * A class used for storing data in a key-value pair without explicitly declaring value type.
 */
public class ConvertableData {

  private String identifier;
  private Object value;

  public ConvertableData(String identifier, Object value) {
    this.identifier = identifier;
    this.value = value;
  }

  public String getIdentifier() {
    return identifier;
  }

  public void setIdentifier(String identifier) {
    this.identifier = identifier;
  }

  public Object getValue() {
    return value;
  }

  public void setValue(Object value) {
    this.value = value;
  }

  public Integer getInteger() {
    return (Integer)value;
  }

  public Short getShort() {
    return (Short)value;
  }

  public String getString() {
    return (String)value;
  }

  public Double getDouble() {
    return (Double)value;
  }

  public Long getLong() {
    return (Long)value;
  }

  public BigDecimal getDecimal() {
    return (BigDecimal)value;
  }

  public BigInteger getBigInteger() {
    return (BigInteger)value;
  }

  public UUID getUUID() {
    return (UUID)value;
  }

  public Boolean getBoolean() {
    return (Boolean)value;
  }

  public <T> List<T> getList(Class<? extends T> type) {
    List<T> cast = new ArrayList<>();
    for(Object obj : (List)value) {
      try {
        cast.add(type.cast(obj));
      } catch(ClassCastException ignore) {
        TNE.logger().warning("Attempted to incorrectly cast object to type: " + type.getName());
      }
    }
    return cast;
  }
}