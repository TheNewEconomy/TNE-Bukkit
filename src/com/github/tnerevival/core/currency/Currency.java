package com.github.tnerevival.core.currency;

import java.util.HashMap;


public class Currency {
	
	HashMap<String, Tier> tiers = new HashMap<String, Tier>();
	
	String name;
	String plural;
	String abbreviation;
	Double value;
	Boolean worldDefault;
	Boolean digital;
	Double balance;
	
	public Currency(String name) {
		this.name = name;
		this.plural = name + "s";
		this.abbreviation = "";
		this.worldDefault = false;
		this.digital = true;
		this.balance = 0.0;
	}
	
	public Tier getTier(String name) {
		return tiers.get(name);
	}
	
	public void setTier(Tier tier) {
		this.tiers.put(tier.getName(), tier);
	}

	/**
	 * @return the tiers
	 */
	public HashMap<String, Tier> getTiers() {
		return tiers;
	}

	/**
	 * @param tiers the tiers to set
	 */
	public void setTiers(HashMap<String, Tier> tiers) {
		this.tiers = tiers;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the plural
	 */
	public String getPlural() {
		return plural;
	}

	/**
	 * @param plural the plural to set
	 */
	public void setPlural(String plural) {
		this.plural = plural;
	}

	/**
	 * @return the abbreviation
	 */
	public String getAbbreviation() {
		return abbreviation;
	}

	/**
	 * @param abbreviation the abbreviation to set
	 */
	public void setAbbreviation(String abbreviation) {
		this.abbreviation = abbreviation;
	}
	
	/**
	 * @return the value
	 */
	public Double getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(Double value) {
		this.value = value;
	}

	/**
	 * @return the worldDefault
	 */
	public Boolean getWorldDefault() {
		return worldDefault;
	}

	/**
	 * @param worldDefault the worldDefault to set
	 */
	public void setWorldDefault(Boolean worldDefault) {
		this.worldDefault = worldDefault;
	}

	/**
	 * @return the digital
	 */
	public Boolean getDigital() {
		return digital;
	}

	/**
	 * @param digital the digital to set
	 */
	public void setDigital(Boolean digital) {
		this.digital = digital;
	}

	/**
	 * @return the balance
	 */
	public Double getBalance() {
		return balance;
	}

	/**
	 * @param balance the balance to set
	 */
	public void setBalance(Double balance) {
		this.balance = balance;
	}
}