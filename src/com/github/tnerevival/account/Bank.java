package com.github.tnerevival.account;

import com.github.tnerevival.serializable.SerializableItemStack;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Bank implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	List<SerializableItemStack> items = new ArrayList<SerializableItemStack>();
	UUID id;
	String pin;
	Integer size;
	Double gold;
	
	public Bank(UUID uid, Integer size) {
		this.id = uid;
		this.pin = "none";
		this.size = size;
		this.gold = 0.0;
	}
	
	public Bank(UUID uid, Integer size, Double gold) {
		this.id = uid;
		this.pin = "none";
		this.size = size;
		this.gold = gold;
	}

	/**
	 * @return the items
	 */
	public List<SerializableItemStack> getItems() {
		return items;
	}

	/**
	 * @param items the items to set
	 */
	public void setItems(List<SerializableItemStack> items) {
		this.items = items;
	}

	/**
	 * @return the size
	 */
	public Integer getSize() {
		return size;
	}

	/**
	 * @param size the size to set
	 */
	public void setSize(Integer size) {
		this.size = size;
	}

	/**
	 * @return the gold
	 */
	public Double getGold() {
		return gold;
	}

	/**
	 * @param gold the gold to set
	 */
	public void setGold(Double gold) {
		this.gold = gold;
	}
	
	public String itemsToString() {
		if(!items.isEmpty()) {
			String toReturn = "";
			
			int count = 0;
			for(SerializableItemStack item : items) {
				if(item != null) {
					if(count != 0) {
						toReturn += "*";
					}
					toReturn += item.toString();
					count++;
				}
			}
			return toReturn;
		}
		return "TNENOSTRINGVALUE";
	}
	
	public String toString() {
		return id.toString() + ":" + pin + ":" + size + ":" + gold + ":" + itemsToString();
	}
}