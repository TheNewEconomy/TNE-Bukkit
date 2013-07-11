package com.github.tnerevival.core.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.github.tnerevival.TheNewEconomy;

public class PlayerListener implements Listener {
	TheNewEconomy plugin;
	
	public PlayerListener(TheNewEconomy plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
	}
}