package com.github.tnerevival.core.accounts.banks;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class BankSlot implements Serializable {
	private static final long serialVersionUID = 1L;
	
	HashMap<SerializableEnchantment, Integer> enchantments = new HashMap<SerializableEnchantment, Integer>();
	List<String> lore = new ArrayList<String>();
	
	Integer id;
	Integer slot;
	Integer amount;
	Short damage;
	String name;
	
	public BankSlot(Integer slot) {
		this.id = null;
		this.slot = slot;
	}
	public BankSlot(ItemStack i, Integer slot) {
		this.id = i.getTypeId();
		this.slot = slot;
		this.amount = i.getAmount();
		this.damage = i.getDurability();
		if(i.getItemMeta().hasDisplayName()) {
			this.name = i.getItemMeta().getDisplayName();
		}
		if(i.getItemMeta().hasLore()) {
			this.lore = i.getItemMeta().getLore();
		}
		this.enchantments = getEnchantments(i);
	}

	/**
	 * @return the slot
	 */
	public Integer getSlot() {
		return slot;
	}

	/**
	 * @param slot the slot to set
	 */
	public void setSlot(Integer slot) {
		this.slot = slot;
	}

	HashMap<SerializableEnchantment, Integer> getEnchantments(ItemStack i) {
		Map<Enchantment, Integer> enchantments = i.getEnchantments();
		HashMap<SerializableEnchantment, Integer> serializedEnchantments = new HashMap<SerializableEnchantment, Integer>();
		for(Enchantment e : enchantments.keySet()) {
			serializedEnchantments.put(new SerializableEnchantment(e), enchantments.get(e));
		}
		return serializedEnchantments;
	}
	
	public ItemStack toItemStack() {
		if(this.id != null) {
			ItemStack stack = new ItemStack(this.id, this.amount, this.damage);
			if(!this.enchantments.isEmpty()) {
				HashMap<Enchantment, Integer> enchants = new HashMap<Enchantment, Integer>();
				for(SerializableEnchantment e : this.enchantments.keySet()) {
					enchants.put(e.getEnchantment(), this.enchantments.get(e));
				}
				stack.addUnsafeEnchantments(enchants);
			}
			ItemMeta stackMeta = stack.getItemMeta();
			if(!this.lore.isEmpty()) {
				stackMeta.setLore(this.lore);
			}
			if(this.name != null && !this.name.isEmpty()) {
				stackMeta.setDisplayName(this.name);
			}
			return stack;
		} else {
			return null;
		}
	}
}