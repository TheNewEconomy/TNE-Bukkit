package com.github.tnerevival.utils;

import org.bukkit.Bukkit;

import com.github.tnerevival.TNE;

public class PlayerUtils {

	public static String getWorld(String username) {
		if(MISCUtils.multiWorld()) {
			return Bukkit.getPlayer(username).getWorld().getName();
		}
		return TNE.instance.defaultWorld;
	}
}