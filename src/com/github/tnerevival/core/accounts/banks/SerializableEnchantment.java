package com.github.tnerevival.core.accounts.banks;

import java.io.Serializable;

import org.bukkit.enchantments.Enchantment;


public class SerializableEnchantment implements Serializable {
	private static final long serialVersionUID = 1L;
	
	Integer id;
	
	public SerializableEnchantment(Enchantment e) {
		this.id = e.getId();
	}
	
	public Enchantment getEnchantment() {
		return Enchantment.getById(id);
	}
}
