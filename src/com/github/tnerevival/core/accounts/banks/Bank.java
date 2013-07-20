package com.github.tnerevival.core.accounts.banks;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.inventory.ItemStack;

public class Bank implements Serializable {
	
	private static final long serialVersionUID = 1L;
	String owner;
	Integer money;
	List<BankSlot> items = new ArrayList<BankSlot>();
	
	public Bank(String owner) {
		this.owner = owner;
	}
	
	public Bank(String owner, Integer money) {
		this.owner = owner;
		this.money = money;
	}

	/**
	 * @return the owner
	 */
	public String getOwner() {
		return owner;
	}

	/**
	 * @return the money
	 */
	public Integer getMoney() {
		return money;
	}

	/**
	 * @return the items
	 */
	public List<BankSlot> getItems() {
		return items;
	}

	/**
	 * @param items the items to set
	 */
	public void setItems(List<BankSlot> items) {
		this.items = items;
	}
	
	public Boolean hasRoom(Integer amount) {
		Integer freeSlots = 0;
		for(BankSlot slot : items) {
			if(slot.toItemStack().equals(null)) {
				freeSlots++;
			}
		}
		if(freeSlots >= amount) {
			return true;
		}
		return false;
	}
	
	public void addItem(ItemStack item) {
		Integer spent = 0;
		for(BankSlot slot : items) {
			if(spent == 0) {
				if(slot.toItemStack().equals(null)) {
					slot.equals(new BankSlot(item, slot.getSlot()));
					spent++;
				}
			}
		}
	}
}