package com.github.tnerevival.worker;

import org.bukkit.scheduler.BukkitRunnable;

import com.github.tnerevival.TNE;

public class AuctionWorker extends BukkitRunnable {
	
	private TNE plugin;
	
	public AuctionWorker(TNE plugin) {
		this.plugin = plugin;
	}

	@Override
	public void run() {
		plugin.auctions.work();
	}
}