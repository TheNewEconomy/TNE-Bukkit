package com.github.tnerevival.core.transaction;

import com.github.tnerevival.TNE;
import com.github.tnerevival.core.currency.Currency;
import com.github.tnerevival.serializable.SerializableItemStack;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Daniel on 8/20/2016.
 */
public class TransactionCost {

  private List<SerializableItemStack> items;
  private BigDecimal amount;
  private Currency currency;

  public TransactionCost(BigDecimal amount) {
    this(amount, TNE.instance().manager.currencyManager.get(TNE.instance().defaultWorld));
  }

  public TransactionCost(BigDecimal amount, Currency currency) {
    this(amount, currency, new ArrayList<SerializableItemStack>());
  }

  public TransactionCost(BigDecimal amount, Currency currency, List<SerializableItemStack> items) {
    this.amount = amount;
    this.currency = currency;
    this.items = items;
  }

  public BigDecimal getAmount() {
    return amount;
  }

  public void setAmount(BigDecimal amount) {
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