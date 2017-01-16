package com.github.tnerevival.core.transaction;

import com.github.tnerevival.TNE;
import com.github.tnerevival.core.currency.Currency;
import com.github.tnerevival.serializable.SerializableItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Daniel on 8/20/2016.
 */
public class TransactionCost {

  private List<SerializableItemStack> items;
  private double amount;
  private Currency currency;

  public TransactionCost(double amount) {
    this(amount, TNE.instance.manager.currencyManager.get(TNE.instance.defaultWorld));
  }

  public TransactionCost(double amount, Currency currency) {
    this(amount, currency, new ArrayList<SerializableItemStack>());
  }

  public TransactionCost(double amount, Currency currency, List<SerializableItemStack> items) {
    this.amount = amount;
    this.currency = currency;
    this.items = items;
  }

  public double getAmount() {
    return amount;
  }

  public void setAmount(double amount) {
    this.amount = amount;
  }

  public Currency getCurrency() {
    return currency;
  }

  public void setCurrency(Currency currency) {
    this.currency = currency;
  }

  public List<SerializableItemStack> getItems() {
    return items;
  }

  public void setItems(List<SerializableItemStack> items) {
    this.items = items;
  }
}