package com.github.tnerevival.core.material;

import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;

public class MaterialHelper {
	
	public static List<MaterialNameHelper> validNames = new ArrayList<>();
	
	static {
		
	}
	
	
	public static Material getMaterial(String search) {
		for(MaterialNameHelper helper : validNames) {
			if(helper.validName(search)) {
				return helper.material;
			}
		}
		return Material.AIR;
	}
}