package com.github.tnerevival.core.accounts.banks;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class BankSlot implements Serializable {
	private static final long serialVersionUID = 1L;
	
	List<Integer> enchantments = new ArrayList<Integer>();
	List<String> lore = new ArrayList<String>();
	
	Integer id;
	Integer slot;
	Integer amount;
	Short damage;
	String name;
	
	public BankSlot(int id, int slot) {
		this.id = id;
		this.slot = slot;
		this.amount = 1;
		this.damage = 0;
	}
	
	public BankSlot(int id, int slot, int amount) {
		this.id = id;
		this.amount = amount;
		this.damage = 0;
	}
	
	public BankSlot(int id, int slot, int amount, Short damage) {
		this.id = id;
		this.amount = amount;
		this.damage = damage;
	}

	/**
	 * @return the enchantments
	 */
	public List<Integer> getEnchantments() {
		return enchantments;
	}

	/**
	 * @param enchantments the enchantments to set
	 */
	public void setEnchantments(List<Integer> enchantments) {
		this.enchantments = enchantments;
	}

	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * @return the amount
	 */
	public Integer getAmount() {
		return amount;
	}

	/**
	 * @param amount the amount to set
	 */
	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	/**
	 * @return the damage
	 */
	public Short getDamage() {
		return damage;
	}

	/**
	 * @param damage the damage to set
	 */
	public void setDamage(Short damage) {
		this.damage = damage;
	}
}