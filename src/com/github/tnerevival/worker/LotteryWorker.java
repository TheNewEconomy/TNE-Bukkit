package com.github.tnerevival.worker;

import org.bukkit.scheduler.BukkitRunnable;

import com.github.tnerevival.TNE;
import com.github.tnerevival.lottery.Lottery;

public class LotteryWorker extends BukkitRunnable {
	
	private TNE plugin;
	private Lottery lottery;
	
	public LotteryWorker(TNE plugin, String lottery) {
		this.plugin = plugin;
		this.lottery = plugin.manager.lotteries.get(lottery);
	}

	@Override
	public void run() {
		
	}
}