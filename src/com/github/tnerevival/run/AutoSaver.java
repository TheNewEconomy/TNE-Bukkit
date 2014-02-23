package com.github.tnerevival.run;

import org.bukkit.scheduler.BukkitRunnable;

import com.github.tnerevival.TNE;

public class AutoSaver extends BukkitRunnable {
	
	private TNE plugin;
	
	public AutoSaver(TNE plugin) {
		this.plugin = plugin;
	}

	@Override
	public void run() {
		plugin.manager.saveManager.save();
	}
	
	public void startTask(Long time) {
		this.runTaskTimer(plugin, 0L, time);
	}
	
	public void cancelTask() {
		this.cancel();
	}
}