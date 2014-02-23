package com.github.tnerevival.core.currency;

import java.util.HashMap;


public class Currency {
	
	HashMap<String, Tier> tiers = new HashMap<String, Tier>();
	
	String name;
	Boolean worldDefault;
	Boolean digital;
	
	public Currency(String name) {
		this. name = name;
		this.worldDefault = false;
		this.digital = true;
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
}