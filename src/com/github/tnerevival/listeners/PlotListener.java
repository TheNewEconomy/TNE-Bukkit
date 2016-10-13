package com.github.tnerevival.listeners;

import com.github.tnerevival.TNE;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

/**
 * Created by Daniel on 10/12/2016.
 */
public class PlotListener implements Listener {

  TNE plugin;

  public PlotListener(TNE plugin) {
    this.plugin = plugin;
  }

  @EventHandler
  public void onMove(PlayerMoveEvent event) {

  }
}