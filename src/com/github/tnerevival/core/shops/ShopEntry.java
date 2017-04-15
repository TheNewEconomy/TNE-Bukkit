package com.github.tnerevival.core.shops;

import com.github.tnerevival.serializable.SerializableItemStack;
import com.github.tnerevival.utils.MISCUtils;
import org.bukkit.Material;

import java.io.Serializable;
import java.math.BigDecimal;

public class ShopEntry implements Serializable {
  private static final long serialVersionUID = 1L;

  private SerializableItemStack item = null;
  private boolean buy = true;
  private boolean unlimited = false;
  private int stock = 0;
  private int maxstock = 0;
  private BigDecimal cost = BigDecimal.ZERO;
  private SerializableItemStack trade = null;

  public ShopEntry(SerializableItemStack item, BigDecimal cost, int stock, boolean buy) {
    this.item = item;
    this.cost = cost;
    this.stock = stock;
    this.buy = buy;
  }

  public ShopEntry(SerializableItemStack item, BigDecimal cost, int stock, boolean buy, boolean unlimited) {
    this.item = item;
    this.cost = cost;
    this.stock = stock;
    this.buy = buy;
    this.unlimited = unlimited;
  }

  public ShopEntry(SerializableItemStack item, BigDecimal cost, int stock, boolean buy, boolean unlimited, SerializableItemStack trade) {
    this.item = item;
    this.cost = cost;
    this.stock = stock;
    this.buy = buy;
    this.unlimited = unlimited;
    this.trade = trade;
  }

  public SerializableItemStack getItem() {
    return item;
  }

  public void setItem(SerializableItemStack item) {
    this.item = item;
  }

  public boolean isBuy() {
    return buy;
  }

  public void setBuy(boolean buy) {
    this.buy = buy;
  }

  public boolean isUnlimited() {
    return unlimited;
  }

  public void setUnlimited(boolean unlimited) {
    this.unlimited = unlimited;
  }

  public int getStock() {
    return this.stock;
  }

  public void setStock(int stock) {
    this.stock = stock;
  }

  public int getMaxstock() {
    return maxstock;
  }

  public void setMaxstock(int maxstock) {
    this.maxstock = maxstock;
  }

  public BigDecimal getCost() {
    return cost;
  }

  public void setCost(BigDecimal cost) {
    this.cost = cost;
  }

  public SerializableItemStack getTrade() {
    return trade;
  }

  public void setTrade(SerializableItemStack trade) {
    this.trade = trade;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();

    builder.append(item.toString());
    builder.append("*" + cost.toPlainString());
    builder.append("*" + stock);
    builder.append("*" + buy);
    builder.append("*" + unlimited);
    builder.append("*" + maxstock);
    if(trade != null) {
      builder.append("*" + trade.toString());
    }
    return builder.toString();
  }

  public static ShopEntry fromString(String parse) {
    String[] parsed = parse.split("\\*");

    MISCUtils.debug(parsed[0]);

    if(parsed.length >= 6) {
      ShopEntry entry =  new ShopEntry(SerializableItemStack.fromString(parsed[0]), new BigDecimal(parsed[1]), Integer.valueOf(parsed[2]), Boolean.valueOf(parsed[3]), Boolean.valueOf(parsed[4]));
      entry.setMaxstock(Integer.valueOf(parsed[5]));
      if (parsed.length == 7) {
        entry.setTrade(SerializableItemStack.fromString(parsed[6]));
      }

      return entry;
    }
    return null;
  }

  @Override
  public boolean equals(Object obj) {
    if(obj == null) return false;
    if(getClass() != obj.getClass()) return false;

    final ShopEntry entry = (ShopEntry)obj;
    if(!item.getName().equals(entry.getItem().getName())) return false;
    if(Short.compare(item.getDamage(), entry.getItem().getDamage()) != 0) return false;
    if(Integer.compare(item.getAmount(), entry.getItem().getAmount()) != 0) return false;
    if(Boolean.compare(buy, entry.isBuy()) != 0) return false;
    if(Boolean.compare(unlimited, entry.isUnlimited()) != 0) return false;
    if(cost.compareTo(entry.getCost()) != 0) return false;
    if(trade != null && !trade.getName().equals(Material.AIR.toString())) {
      if(!trade.getName().equals(entry.getTrade().getName())) return false;
      if(Short.compare(trade.getDamage(), entry.getTrade().getDamage()) != 0) return false;
      if(Integer.compare(trade.getAmount(), entry.getTrade().getAmount()) != 0) return false;
    }

    return true;
  }
}