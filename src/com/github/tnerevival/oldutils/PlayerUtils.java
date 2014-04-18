package com.github.tnerevival.oldutils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class PlayerUtils {
	
	public static String getWorld(String username) {
		Player player = Bukkit.getPlayer(username);
		return player.getWorld().getName();
	}
}