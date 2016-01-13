package com.github.tnerevival.core.objects;

import java.util.HashMap;

public class TNECommandObject {

	private HashMap<String, TNECommandObject> subCommands = new HashMap<String, TNECommandObject>();
	
	private String name;
	private double cost;
	
	public TNECommandObject(String name, double cost) {
		this.name = name;
		this.cost = cost;
	}
	
	public String getIdentifier() {
		return name;
	}

	public double getCost() {
		return cost;
	}
	
	public TNECommandObject findSub(String name) {
		return subCommands.get(name);
	}
	
	public double subCost(String name) {
		if(findSub(name) != null) {
			return subCommands.get(name).getCost();
		}
		return 0.0;
	}
	
	public void addSub(TNECommandObject sub) {
		subCommands.put(sub.getIdentifier(), sub);
	}
}