package com.github.tnerevival.core.signs.item;

import org.bukkit.inventory.ItemStack;

import java.math.BigDecimal;

/**
 * Created by creatorfromhell on 2/26/2017.
 * All rights reserved.
 **/
public class ItemEntry {

  private int order;
  private ItemStack item;
  private ItemStack trade;
  private BigDecimal sell;
  private BigDecimal buy;

  public ItemEntry(int order, ItemStack item) {
    this.order = order;
    this.item = item;
  }

  public int getOrder() {
    return order;
  }

  public void setOrder(int order) {
    this.order = order;
  }

  public ItemStack getItem() {
    return item;
  }

  public void setItem(ItemStack item) {
    this.item = item;
  }

  public ItemStack getTrade() {
    return trade;
  }

  public void setTrade(ItemStack trade) {
    this.trade = trade;
  }

  public BigDecimal getSell() {
    return sell;
  }

  public void setSell(BigDecimal sell) {
    this.sell = sell;
  }

  public BigDecimal getBuy() {
    return buy;
  }

  public void setBuy(BigDecimal buy) {
    this.buy = buy;
  }
}