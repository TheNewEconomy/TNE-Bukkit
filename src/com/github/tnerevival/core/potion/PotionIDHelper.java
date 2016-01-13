package com.github.tnerevival.core.potion;


public class PotionIDHelper {
	
	private String name;
	private Integer durability;
	
	public PotionIDHelper(String name, Integer durability) {
		this.name = name;
		this.durability = durability;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getDurability() {
		return durability;
	}

	public void setDurability(Integer durability) {
		this.durability = durability;
	}
}