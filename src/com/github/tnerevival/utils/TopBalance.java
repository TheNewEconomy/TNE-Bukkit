package com.github.tnerevival.utils;

import java.util.UUID;

/**
 * Created by creatorfromhell on 2/20/2017.
 * All rights reserved.
 **/
public class TopBalance {

  private UUID id;
  private double balance;

  public TopBalance(UUID id, double balance) {
    this.id = id;
    this.balance = balance;
  }

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public double getBalance() {
    return balance;
  }

  public void setBalance(double balance) {
    this.balance = balance;
  }
}