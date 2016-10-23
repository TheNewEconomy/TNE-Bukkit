package com.github.tnerevival.worker;

import org.bukkit.scheduler.BukkitRunnable;

import com.github.tnerevival.TNE;
import com.github.tnerevival.core.Statistics;

public class StatisticsWorker extends BukkitRunnable {

  @SuppressWarnings("unused")
  private TNE plugin;

  public StatisticsWorker(TNE plugin) {
    this.plugin = plugin;
  }

  @Override
  public void run() {
    Statistics.send();
  }
}