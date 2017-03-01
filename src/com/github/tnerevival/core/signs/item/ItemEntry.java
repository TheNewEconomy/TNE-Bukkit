package com.github.tnerevival.core.signs.item;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.math.BigDecimal;

/**
 * Created by creatorfromhell on 2/26/2017.
 * All rights reserved.
 **/
public class ItemEntry {

  private int order;
  private ItemStack item = new ItemStack(Material.AIR);
  private ItemStack trade = new ItemStack(Material.AIR);
  private BigDecimal sell = new BigDecimal(-1.0);
  private BigDecimal buy = new BigDecimal(-1.0);
  public boolean admin = false;

  public ItemEntry(int order, ItemStack item) {
    this.order = order;
    this.item = item;
  }

  public ItemEntry reorder(int order) {
    ItemEntry entry = new ItemEntry(order, item);
    entry.setTrade(trade);
    entry.setSell(sell);
    entry.setBuy(buy);
    return entry;
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

  public boolean isAdmin() {
    return admin;
  }

  public void setAdmin(boolean admin) {
    this.admin = admin;
  }
}