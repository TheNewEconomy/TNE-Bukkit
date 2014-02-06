package com.github.tnerevival.serializable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class SerializableItemStack implements Serializable {
	private static final long serialVersionUID = 1L;
	
	HashMap<SerializableEnchantment, Integer> enchantments = new HashMap<SerializableEnchantment, Integer>();
	List<String> lore = new ArrayList<String>();
	
	String name;
	Integer slot;
	Integer amount;
	Short damage;
	String customName;
	
	public SerializableItemStack(Integer slot) {
		this.name = "none";
		this.slot = slot;
		this.customName = "none";
	}
	
	public SerializableItemStack(Integer slot, ItemStack i) {
		this.name = i.getType().name();
		this.slot = slot;
		this.amount = i.getAmount();
		this.damage = i.getDurability();
		this.customName = "none";
		if(i.getItemMeta().hasDisplayName()) {
			this.customName = i.getItemMeta().getDisplayName();
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
		if(!this.name.equalsIgnoreCase("none")) {
			ItemStack stack = new ItemStack(Material.getMaterial(name), this.amount, this.damage);
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
			if(!this.customName.equals(null) && !this.customName.equals("none") && !this.customName.isEmpty()) {
				stackMeta.setDisplayName(this.customName);
			}
			stack.setItemMeta(stackMeta);
			return stack;
		}
		return null;
	}
}