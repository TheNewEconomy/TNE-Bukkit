package com.github.tnerevival.core.objects;

import java.util.HashMap;

public class TNEInventoryObject {

	private HashMap<String, TNEAccessPackage> packages = new HashMap<String, TNEAccessPackage>();
	
	private String name;
	private boolean enabled;
	private boolean timed;
	private double cost;
	
	public TNEInventoryObject(String name, boolean enabled, boolean timed, double cost) {
		this.name = name;
		this.enabled = enabled;
		this.timed = timed;
		this.cost = cost;
	}
	
	public String getIdentifier() {
		return name;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public boolean isTimed() {
		return timed;
	}

	public double getCost() {
		return cost;
	}
	
	public void addPackage(TNEAccessPackage access) {
		packages.put(access.getName(), access);
	}
	
	public TNEAccessPackage findPackage(String name) {
		return null;
		
	}
	
	public TNEAccessPackage findPackage(long time) {
		return null;
		
	}
	
	public TNEAccessPackage findPackage(double cost) {
		return null;
		
	}
	
	public TNEAccessPackage[] getPackages() {
		return (TNEAccessPackage[])packages.values().toArray();
	}
}