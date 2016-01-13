package com.github.tnerevival.worker;

import org.bukkit.scheduler.BukkitRunnable;

import com.github.tnerevival.TNE;

public class InventoryTimeWorker extends BukkitRunnable {
	
	private TNE plugin;
	public String username;
	
	public InventoryTimeWorker(TNE plugin, String username) {
		this.plugin = plugin;
		this.username = username;
	}

	@Override
	public void run() {
		
	}
}