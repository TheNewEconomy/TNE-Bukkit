package com.github.tnerevival.core.transaction;

import com.github.tnerevival.serializable.SerializableItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Daniel on 8/20/2016.
 */
public class TransactionCost {

  private List<SerializableItemStack> items;
  private double amount;

  public TransactionCost(double amount) {
    this(amount, new ArrayList<SerializableItemStack>());
  }

  public TransactionCost(double amount, List<SerializableItemStack> items) {
    this.amount = amount;
    this.items = items;
  }

  public double getAmount() {
    return amount;
  }

  public void setAmount(double amount) {
    this.amount = amount;
  }

  public List<SerializableItemStack> getItems() {
    return items;
  }

  public void setItems(List<SerializableItemStack> items) {
    this.items = items;
  }
}