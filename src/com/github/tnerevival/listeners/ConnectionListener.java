package com.github.tnerevival.listeners;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.github.tnerevival.TNE;
import com.github.tnerevival.account.Account;
import com.github.tnerevival.core.Message;
import com.github.tnerevival.utils.AccountUtils;
import com.github.tnerevival.utils.MISCUtils;

public class ConnectionListener implements Listener {
	
	TNE plugin;
	
	public ConnectionListener(TNE plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		if(!plugin.manager.accounts.containsKey(MISCUtils.getID(player))) {
			AccountUtils.createAccount(MISCUtils.getID(player));
		}
		
		if(player.hasPermission("tne.admin") && !TNE.updater.latest()) {
			player.sendMessage(ChatColor.RED + "[TNE] Outdated! The current build is " + TNE.updater.getCurrentBuild());
		}
		
		Account account = AccountUtils.getAccount(MISCUtils.getID(player));
		
		String node = "Messages.Account.Confirm";
		if(account.getPin().equalsIgnoreCase("TNENOSTRINGVALUE")) {
			node = "Messages.Account.Set";
		}
		
		Message message = new Message(node);
		player.sendMessage(message.translate());
	}
	
	@EventHandler
	public void onLeave(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		
		TNE.instance.manager.confirmed.remove(MISCUtils.getID(player));
	}
	
	@EventHandler
	public void onDeath(PlayerDeathEvent event) {
		if(TNE.configurations.getBoolean("Core.Death.Lose")) {
			AccountUtils.setFunds(MISCUtils.getID(event.getEntity()), 0.0);
		}
	}
}