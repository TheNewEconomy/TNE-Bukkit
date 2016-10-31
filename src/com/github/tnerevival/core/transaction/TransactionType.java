package com.github.tnerevival.core.transaction;

/**
 * Created by Daniel on 8/15/2016.
 */
public enum TransactionType {
  MONEY_SET("MONEY_SET"),
  MONEY_INQUIRY("MONEY_INQUIRY"),
  MONEY_REMOVE("MONEY_REMOVE"),
  MONEY_GIVE("MONEY_GIVE"),
  MONEY_PAY("MONEY_PAY"),
  BANK_INQUIRY("BANK_INQUIRY"),
  BANK_WITHDRAWAL("BANK_WITHDRAWAL"),
  BANK_DEPOSIT("BANK_DEPOSIT");

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