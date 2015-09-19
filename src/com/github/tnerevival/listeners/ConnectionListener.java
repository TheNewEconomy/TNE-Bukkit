package com.github.tnerevival.listeners;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import com.github.tnerevival.TNE;
import com.github.tnerevival.utils.AccountUtils;

public class ConnectionListener implements Listener {
	
	TNE plugin;
	
	public ConnectionListener(TNE plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		if(!plugin.manager.accounts.containsKey(player.getUniqueId())) {
			AccountUtils.createAccount(player.getUniqueId());
		}
		
		if(player.hasPermission("tne.admin") && !TNE.updater.latest()) {
			player.sendMessage(ChatColor.RED + "[TNE] Outdated! The current build is " + TNE.updater.getCurrentBuild());
		}
	}
	
	@EventHandler
	public void onDeath(PlayerDeathEvent event) {
		if(TNE.configurations.getBoolean("Core.Death.Lose")) {
			AccountUtils.setFunds(event.getEntity().getUniqueId(), 0.0);
		}
	}
}