package com.github.tnerevival.core.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.github.tnerevival.TheNewEconomy;
import com.github.tnerevival.core.accounts.Account;

public class PlayerListener implements Listener {
	TheNewEconomy plugin;
	
	public PlayerListener(TheNewEconomy plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		if(!TheNewEconomy.instance.eco.accounts.containsKey(event.getPlayer().getName())) {
			Account account = new Account(event.getPlayer().getName());
			TheNewEconomy.instance.eco.accounts.put(event.getPlayer().getName(), account);
		}
	}
}