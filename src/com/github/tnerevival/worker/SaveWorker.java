package com.github.tnerevival.worker;

import org.bukkit.scheduler.BukkitRunnable;

import com.github.tnerevival.TNE;

public class SaveWorker extends BukkitRunnable {

  private TNE plugin;

  public SaveWorker(TNE plugin) {
    this.plugin = plugin;
  }

  @Override
  public void run() {
    plugin.saveManager.save();
  }
}