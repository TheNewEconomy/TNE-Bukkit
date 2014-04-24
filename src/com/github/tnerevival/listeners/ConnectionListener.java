package com.github.tnerevival.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import com.github.tnerevival.TNE;
import com.github.tnerevival.account.Account;
import com.github.tnerevival.utils.AccountUtils;

public class ConnectionListener implements Listener {
	
	TNE plugin;
	
	public ConnectionListener(TNE plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		String username = player.getDisplayName();
		if(!plugin.manager.accounts.containsKey(username)) {
			Account account = new Account(username);
			plugin.manager.accounts.put(username, account);
			AccountUtils.addFunds(username, AccountUtils.getInitialBalance(TNE.instance.defaultWorld));
		}
	}
	
	@EventHandler
	public void onDeath(PlayerDeathEvent event) {
		if(TNE.instance.getConfig().getBoolean("Core.Death.Lose")) {
			String username = event.getEntity().getDisplayName();
			
			AccountUtils.setFunds(username, 0.0);
		}
	}
}