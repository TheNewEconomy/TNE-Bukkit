package com.github.tnerevival.core.transaction;

/**
 * Created by Daniel on 8/15/2016.
 */
public enum TransactionType {
  MONEY_SET("MONEY_SET"),
  MONEY_INQUIRY("MONEY_SET"),
  MONEY_REMOVE("MONEY_SET"),
  MONEY_GIVE("MONEY_SET"),
  MONEY_PAY("MONEY_SET"),
  BANK_INQUIRY("MONEY_SET"),
  BANK_WITHDRAWAL("MONEY_SET"),
  BANK_DEPOSIT("MONEY_SET");

  String id;

  TransactionType(String id) {
    this.id = id;
  }

  public String getID() {
    return id;
  }

  public static TransactionType fromID(String id) {
    for(TransactionType type : values()) {
      if(type.getID().equalsIgnoreCase(id)) {
        return type;
      }
    }
    return MONEY_SET;
  }
}