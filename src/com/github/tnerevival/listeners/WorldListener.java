package com.github.tnerevival.listeners;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;

import com.github.tnerevival.TNE;
import com.github.tnerevival.utils.AccountUtils;
import com.github.tnerevival.utils.MISCUtils;

public class WorldListener implements Listener {
	
	TNE plugin;
	
	public WorldListener(TNE plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onWorldChange(PlayerChangedWorldEvent event) {
		Player player = event.getPlayer();
		String username = player.getDisplayName();
		String world = player.getWorld().getName();
		
		if(TNE.instance.getConfig().getBoolean("Core.World.EnableChangeFee")) {
			if(!player.hasPermission("tne.world.bypass") && !player.hasPermission("tne.world.*")) {
				if(AccountUtils.hasFunds(username, TNE.instance.getConfig().getDouble("Core.World.ChangeFee"))) {
					AccountUtils.removeFunds(username, TNE.instance.getConfig().getDouble("Core.World.ChangeFee"));
					AccountUtils.initializeWorldData(username, world);
				} else {
					player.teleport(event.getFrom().getSpawnLocation());
					player.sendMessage(ChatColor.DARK_RED + "I'm sorry, but you need at least " + ChatColor.GOLD + MISCUtils.formatBalance(plugin.getConfig().getDouble("Core.Bank.Cost")) + ChatColor.DARK_RED + " to change worlds.");
				}
			} else {
				AccountUtils.initializeWorldData(username, world);
			}
		} else {
			AccountUtils.initializeWorldData(username, world);
		}
	}
}