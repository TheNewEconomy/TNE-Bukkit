package com.github.tnerevival.core.material;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;

public class MaterialHelper {
	
	public static List<MaterialNameHelper> validNames = new ArrayList<MaterialNameHelper>();
	
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