package com.github.tnerevival.core.transaction;

import com.github.tnerevival.TNE;

public class Transaction {
  private String initiator;
  private String recipient;
  private double amount;
  private TransactionType type;
  private String world;

  public Transaction(String initiator, String recipient, double amount) {
    this(initiator, recipient, amount, TransactionType.MONEY_GIVE, TNE.instance.defaultWorld);
  }

  public Transaction(String initiator, String recipient, double amount, TransactionType type) {
    this(initiator, recipient, amount, type, TNE.instance.defaultWorld);
  }

  public Transaction(String initiator, String recipient, double amount, TransactionType type, String world) {
    this.initiator = initiator;
    this.recipient = recipient;
    this.amount = amount;
    this.type = type;
    this.world = world;
  }

  public boolean perform() {
    return(!handleInitiator() || !handleRecipient());
  }

  private boolean handleInitiator() {
    return true;
  }

  private boolean handleRecipient() {
    return true;
  }

  public String getInitiator() {
    return initiator;
  }

  public String getRecipient() {
    return recipient;
  }

  public void setRecipient(String recipient) {
    this.recipient = recipient;
  }

  public double getAmount() {
    return amount;
  }

  public void setAmount(double amount) {
    this.amount = amount;
  }

  public TransactionType getType() {
    return type;
  }

  public void setType(TransactionType type) {
    this.type = type;
  }

  public String getWorld() {
    return world;
  }

  public void setWorld(String world) {
    this.world = world;
  }
}