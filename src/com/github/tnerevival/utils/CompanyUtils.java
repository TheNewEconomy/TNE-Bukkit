package com.github.tnerevival.utils;

import java.util.UUID;

import com.github.tnerevival.TNE;

public class CompanyUtils {
	
	public static Boolean enabled(UUID id) {
		if(MISCUtils.multiWorld()) {
			String world = MISCUtils.getWorld(id);
			if(MISCUtils.worldConfigExists("Worlds." + world + ".Company.Enabled")) {
				return TNE.instance.worldConfigurations.getBoolean("Worlds." + world + ".Company.Enabled");
			}
		}
		return TNE.configurations.getBoolean("Core.Company.Enabled");
	}
}