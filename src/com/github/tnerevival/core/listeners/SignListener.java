package com.github.tnerevival.core.listeners;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

import com.github.tnerevival.TheNewEconomy;
import com.github.tnerevival.Utils;

public class SignListener implements Listener {
	TheNewEconomy plugin;
	
	public SignListener(TheNewEconomy plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onChange(SignChangeEvent event) {
		if(event.getLine(0).contains("[shop]")) {
			if(event.getPlayer().hasPermission("tne.shop") || event.getPlayer().isOp()) {
				if(event.getLine(1) != "" && event.getLine(1) != null) {
					Material mat = Material.getMaterial(Integer.valueOf(event.getLine(1)));
					event.setLine(1, Utils.formatMaterialName(mat));
				}
			} else {
				event.getPlayer().sendMessage(ChatColor.DARK_RED + "I'm sorry, but you do not have permission to do that.");
				event.setCancelled(true);
			}
		} else if(event.getLine(0).contains("[bank]")) {
			if(event.getPlayer().hasPermission("tne.bank")) {
				
			} else {
				event.getPlayer().sendMessage(ChatColor.DARK_RED + "I'm sorry, but you do not have permission to do that.");
				event.setCancelled(true);
			}
		} else if(event.getLine(0).contains("[shop2]")) {
			event.setLine(1, Utils.formatMaterialNameWithSpace(Material.getMaterial(Integer.valueOf(event.getLine(1)))));
		} else if(event.getLine(0).contains("[test]")) {
			event.setLine(1, Utils.deformatMaterialName(event.getLine(1)));
		} else if(event.getLine(0).contains("[test2]")) {
			event.setLine(1, Utils.deformatMaterialNameWithSpace(event.getLine(1)));
		}
	}
}