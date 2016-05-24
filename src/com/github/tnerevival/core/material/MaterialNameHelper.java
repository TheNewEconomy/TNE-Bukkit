package com.github.tnerevival.core.material;

import org.bukkit.Material;

public class MaterialNameHelper {
	
	Material material;
	String minecraftName;
	String[] commonNames;
	
	public MaterialNameHelper(Material material, String name) {
		this.material = material;
		this.minecraftName = name;
		this.commonNames = new String[0];
	}
	
	public MaterialNameHelper(Material material, String name, String[] commonNames) {
		this.material = material;
		this.minecraftName = name;
		this.commonNames = commonNames;
	}
	
	public boolean validName(String name) {
		if(this.minecraftName.equalsIgnoreCase(name)) return true;
		for(String s : commonNames) {
			if(s.equalsIgnoreCase(name)) return true;
		}
		return false;
	}
}