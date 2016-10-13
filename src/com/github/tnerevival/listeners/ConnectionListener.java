package com.github.tnerevival.listeners;

import com.github.tnerevival.core.version.ReleaseType;
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
		MISCUtils.debug(MISCUtils.getID(player) + "");
		if(!plugin.manager.accounts.containsKey(MISCUtils.getID(player))) {
			AccountUtils.createAccount(MISCUtils.getID(player));
		}
		if(player.hasPermission("tne.admin") && !TNE.updater.getRelease().equals(ReleaseType.LATEST)) {
			String message = ChatColor.RED + "[TNE] Outdated! The current build is " + TNE.updater.getLatestBuild();
			if(TNE.updater.getRelease().equals(ReleaseType.PRERELEASE)) {
				message = ChatColor.GREEN + "[TNE] Prerelease! Thank you for testing TNE Build: " + TNE.updater.getCurrentBuild() + ".";
			}
			player.sendMessage(message);
		}
		
		Account account = AccountUtils.getAccount(MISCUtils.getID(player));

		if(TNE.instance.manager.enabled(MISCUtils.getID(player), MISCUtils.getWorld(player))) {
		  if(!TNE.instance.manager.confirmed(MISCUtils.getID(player), MISCUtils.getWorld(player))) {
        String node = "Messages.Account.Confirm";
        if (account.getPin().equalsIgnoreCase("TNENOSTRINGVALUE")) {
          node = "Messages.Account.Set";
        }

        Message message = new Message(node);
        message.translate(MISCUtils.getWorld(player), player);
      }
    }
	}
	
	@EventHandler
	public void onLeave(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		
		TNE.instance.manager.confirmed.remove(MISCUtils.getID(player));
	}
	
	@EventHandler
	public void onDeath(PlayerDeathEvent event) {
		if(TNE.instance.api.getBoolean("Core.Death.Lose", MISCUtils.getWorld(event.getEntity()), MISCUtils.getID(event.getEntity()))) {
			AccountUtils.setFunds(MISCUtils.getID(event.getEntity()), 0.0);
		}
	}
}