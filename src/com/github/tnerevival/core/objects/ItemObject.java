package com.github.tnerevival.core.objects;

public class ItemObject {
	
	private String name;
	private Double cost;
	private Double value;
	private Double use;
	private Double crafting;
	private Double enchant;
	private Double smelt;
	
	public ItemObject(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Double getCost() {
		return cost;
	}

	public void setCost(Double cost) {
		this.cost = cost;
	}

	public Double getValue() {
		return value;
	}

	public void setValue(Double value) {
		this.value = value;
	}

	public Double getUse() {
		return use;
	}

	public void setUse(Double use) {
		this.use = use;
	}

	public Double getCrafting() {
		return crafting;
	}

	public void setCrafting(Double crafting) {
		this.crafting = crafting;
	}

	public Double getEnchant() {
		return enchant;
	}

	public void setEnchant(Double enchant) {
		this.enchant = enchant;
	}

	public Double getSmelt() {
		return smelt;
	}

	public void setSmelt(Double smelt) {
		this.smelt = smelt;
	}
}