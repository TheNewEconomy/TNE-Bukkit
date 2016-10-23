package com.github.tnerevival.account;

public enum AccountStatus {

  NORMAL("Normal", true, true),
  LOCKED("Locked", false, false),
  BANK_LOCKED("BankLocked", true, false),
  BALANCE_LOCKED("BalanceLocked", false, true);

  private String name;
  private Boolean balance;
  private Boolean bank;

  AccountStatus(String name, Boolean balance, Boolean bank) {
    this.name = name;
    this.balance = balance;
    this.bank = bank;
  }

  public static AccountStatus fromName(String name) {
    for(AccountStatus status : values()) {
      if(status.getName().equalsIgnoreCase(name)) {
        return status;
      }
    }
    return NORMAL;
  }

  public String getName() {
    return name;
  }

  public Boolean getBalance() {
    return balance;
  }

  public Boolean getBank() {
    return bank;
  }
}