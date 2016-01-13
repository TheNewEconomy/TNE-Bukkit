package com.github.tnerevival.core.objects;

public class PotionObject {
	
	private String name;
	private Double brew;
	private Double use;
	
	public PotionObject(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Double getBrew() {
		return brew;
	}

	public void setBrew(Double brew) {
		this.brew = brew;
	}

	public Double getUse() {
		return use;
	}

	public void setUse(Double use) {
		this.use = use;
	}
}