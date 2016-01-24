package com.github.tnerevival.lottery;

import java.util.ArrayList;
import java.util.List;

import com.github.tnerevival.serializable.SerializableItemStack;

public class LotteryData {
	private List<SerializableItemStack> items = new ArrayList<SerializableItemStack>();
	
	private Integer gold;
	private Integer experience;
	private float health;
	private float hunger;
	
	public LotteryData() {
		this.gold = 0;
		this.experience = 0;
		this.health = 0;
		this.hunger = 0;
	}

	public List<SerializableItemStack> getItems() {
		return items;
	}

	public void setItems(List<SerializableItemStack> items) {
		this.items = items;
	}

	public Integer getGold() {
		return gold;
	}

	public void setGold(Integer gold) {
		this.gold = gold;
	}

	public Integer getExperience() {
		return experience;
	}

	public void setExperience(Integer experience) {
		this.experience = experience;
	}

	public float getHealth() {
		return health;
	}

	public void setHealth(float health) {
		this.health = health;
	}

	public float getHunger() {
		return hunger;
	}

	public void setHunger(float hunger) {
		this.hunger = hunger;
	}
}