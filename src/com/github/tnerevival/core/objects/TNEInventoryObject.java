package com.github.tnerevival.core.objects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
		for(String s : packages.keySet()) {
			if(s.equalsIgnoreCase(name)) {
				return packages.get(s);
			}
		}
		return null;
	}
	
	public TNEAccessPackage findPackage(long time) {
		for(TNEAccessPackage access : packages.values()) {
			if(access.getTime() == time) {
				return access;
			}
		}
		return null;
		
	}
	
	public TNEAccessPackage findPackage(double cost) {
		for(TNEAccessPackage access : packages.values()) {
			if(access.getCost() == cost) {
				return access;
			}
		}
		return null;
		
	}
	
	public List<TNEAccessPackage> getPackages() {
		List<TNEAccessPackage> packs = new ArrayList<TNEAccessPackage>();

		for(TNEAccessPackage access : packages.values()) {
			packs.add(access);
		}
		return packs;
	}
}