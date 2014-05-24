package com.github.tnerevival.utils;

import com.github.tnerevival.TNE;

public class CompanyUtils {
	
	public static Boolean enabled(String username) {
		if(MISCUtils.multiWorld()) {
			String world = MISCUtils.getWorld(username);
			if(MISCUtils.worldConfigExists("Worlds." + world + ".Company.Enabled")) {
				return TNE.instance.worldConfigurations.getBoolean("Worlds." + world + ".Company.Enabled");
			}
		}
		return TNE.instance.getConfig().getBoolean("Core.Company.Enabled");
	}
}