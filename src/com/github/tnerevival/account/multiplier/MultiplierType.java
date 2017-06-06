package com.github.tnerevival.account.multiplier;

/**
 * Created by creatorfromhell on 6/5/2017.
 * All rights reserved.
 **/
public enum MultiplierType {

  TIMED("TIMED"),
  DAY_OF_WEEK("DAY_OF_WEEK"),
  DATE("DATE"),
  MONTH("MONTH");

  private String identifier;

  MultiplierType(String identifier) {
    this.identifier = identifier;
  }

  public String getIdentifier() {
    return this.identifier;
  }
}