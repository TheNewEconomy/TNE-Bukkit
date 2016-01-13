package com.github.tnerevival.core.potion;

import org.bukkit.Material;

public class PotionRecipeHelper {
	
	private String name;
	private Material ingredient;
	private Integer base;
	
	public PotionRecipeHelper(String name, Material ingredient, Integer base) {
		this.name = name;
		this.ingredient = ingredient;
		this.base = base;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Material getIngredient() {
		return ingredient;
	}

	public void setIngredient(Material ingredient) {
		this.ingredient = ingredient;
	}

	public Integer getBase() {
		return base;
	}

	public void setBase(Integer base) {
		this.base = base;
	}
}