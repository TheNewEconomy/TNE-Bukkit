package com.github.tnerevival.core.objects;

public class BlockObject {
	
	private String name;
	private Double cost;
	private Double value;
	private Double crafting;
	private Double place;
	private Double mine;
	private Double smelt;
	
	public BlockObject(String name) {
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

	public Double getCrafting() {
		return crafting;
	}

	public void setCrafting(Double crafting) {
		this.crafting = crafting;
	}

	public Double getPlace() {
		return place;
	}

	public void setPlace(Double place) {
		this.place = place;
	}

	public Double getMine() {
		return mine;
	}

	public void setMine(Double mine) {
		this.mine = mine;
	}

	public Double getSmelt() {
		return smelt;
	}

	public void setSmelt(Double smelt) {
		this.smelt = smelt;
	}
}