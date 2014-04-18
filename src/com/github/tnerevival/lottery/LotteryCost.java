package com.github.tnerevival.lottery;

import java.util.ArrayList;
import java.util.List;

import com.github.tnerevival.serializable.SerializableItemStack;

public class LotteryCost {
	List<SerializableItemStack> items = new ArrayList<SerializableItemStack>();
	
	Integer cost;
	
	public LotteryCost() {
		
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
}