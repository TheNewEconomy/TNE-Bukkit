package com.github.tnerevival.core.shops;

import com.github.tnerevival.serializable.SerializableItemStack;
import com.github.tnerevival.utils.MISCUtils;

import java.io.Serializable;

public class ShopEntry implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private SerializableItemStack item = null;
	private int stock = 0;
	private double cost = 0.0;
	private SerializableItemStack trade = null;
	
	public ShopEntry(SerializableItemStack item, double cost, int stock) {
		this.item = item;
		this.cost = cost;
    this.stock = stock;
	}
	
	public ShopEntry(SerializableItemStack item, SerializableItemStack trade) {
		this.item = item;
		this.trade = trade;
	}
	
	public ShopEntry(SerializableItemStack item, double cost, int stock, SerializableItemStack trade) {
		this.item = item;
		this.cost = cost;
    this.stock = stock;
		this.trade = trade;
	}

	public SerializableItemStack getItem() {
		return item;
	}

	public void setItem(SerializableItemStack item) {
		this.item = item;
	}

	public int getStock() {
	  return this.stock;
  }

  public void setStock(int stock) {
    this.stock = stock;
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
    builder.append("*" + stock);
    if(trade != null) {
      builder.append(trade.toString());
    }
    return builder.toString();
  }

  public static ShopEntry fromString(String parse) {
		MISCUtils.debug(parse);
    String[] parsed = parse.split("\\*");

		MISCUtils.debug(parsed[0]);
		MISCUtils.debug(parsed[1]);
		MISCUtils.debug(parsed[2]);
    if(parsed.length == 4) {
      return new ShopEntry(SerializableItemStack.fromString(parsed[0]), Double.valueOf(parsed[1]), Integer.valueOf(parsed[2]), SerializableItemStack.fromString(parsed[3]));
    }
    return new ShopEntry(SerializableItemStack.fromString(parsed[0]), Double.valueOf(parsed[1]), Integer.valueOf(parsed[2]));
  }
}