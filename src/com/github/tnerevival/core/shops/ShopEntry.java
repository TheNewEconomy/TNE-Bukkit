package com.github.tnerevival.core.shops;

import com.github.tnerevival.serializable.SerializableItemStack;

import java.io.Serializable;

public class ShopEntry implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private SerializableItemStack item = null;
	private double cost = 0.0;
	private SerializableItemStack trade = null;
	
	public ShopEntry(SerializableItemStack item, double cost) {
		this.item = item;
		this.cost = cost;
	}
	
	public ShopEntry(SerializableItemStack item, SerializableItemStack trade) {
		this.item = item;
		this.trade = trade;
	}
	
	public ShopEntry(SerializableItemStack item, double cost, SerializableItemStack trade) {
		this.item = item;
		this.cost = cost;
		this.trade = trade;
	}

	public SerializableItemStack getItem() {
		return item;
	}

	public void setItem(SerializableItemStack item) {
		this.item = item;
	}

	public double getCost() {
		return cost;
	}

	public void setCost(double cost) {
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
    builder.append("*" + cost);
    if(trade != null) {
      builder.append(trade.toString());
    }
    return builder.toString();
  }

  public static ShopEntry fromString(String parse) {
    String[] parsed = parse.split("\\*");

    if(parsed.length == 3) {
      return new ShopEntry(SerializableItemStack.fromString(parsed[0]), Double.valueOf(parsed[1]), SerializableItemStack.fromString(parsed[2]));
    }
    return new ShopEntry(SerializableItemStack.fromString(parsed[0]), Double.valueOf(parsed[1]));
  }
}