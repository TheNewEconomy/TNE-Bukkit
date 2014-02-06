package com.github.tnerevival.serializable;

import java.io.Serializable;

import org.bukkit.enchantments.Enchantment;


public class SerializableEnchantment implements Serializable {
	private static final long serialVersionUID = 1L;
	
	String name;
	
	public SerializableEnchantment(Enchantment e) {
		this.name = e.getName();
	}
	
	public Enchantment getEnchantment() {
		return Enchantment.getByName(name);
	}
}
