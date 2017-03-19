package com.github.tnerevival.account;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by creatorfromhell on 2/17/2017.
 * All rights reserved.
 **/
public class BankBalance implements Serializable {
  private String currency;
  private BigDecimal balance;
  private long lastInterest;

  public BankBalance(String currency, BigDecimal balance) {
    this.currency = currency;
    this.balance = balance;
    this.lastInterest = new Date().getTime();
  }

  public String getCurrency() {
    return currency;
  }

  public void setCurrency(String currency) {
    this.currency = currency;
  }

  public BigDecimal getBalance() {
    return balance;
  }

  public void setBalance(BigDecimal balance) {
    this.balance = balance;
  }

  public long getLastInterest() {
    return lastInterest;
  }

  public void setLastInterest(long lastInterest) {
    this.lastInterest = lastInterest;
  }
}