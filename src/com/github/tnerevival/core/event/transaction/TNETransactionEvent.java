package com.github.tnerevival.core.event.transaction;

import com.github.tnerevival.core.transaction.Transaction;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Created by Daniel on 8/15/2016.
 */
public class TNETransactionEvent extends Event {

  private Transaction transaction;

  public TNETransactionEvent(Transaction transaction) {
    this.transaction = transaction;
  }

  public Transaction getTransaction() {
    return transaction;
  }

  public void setTransaction(Transaction transaction) {
    this.transaction = transaction;
  }

  @Override
  public HandlerList getHandlers() {
    return null;
  }
}