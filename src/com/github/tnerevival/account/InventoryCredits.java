package com.github.tnerevival.account;

import com.github.tnerevival.TNE;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class InventoryCredits {

  private HashMap<String, HashMap<String, Long>> credits = new HashMap<String, HashMap<String, Long>>();

  public Long getTimeLeft(Player player, String inventory) {
    String world = (TNE.configurations.getBoolean("Objects.Inventories.PerWorld", "objects")) ? player.getWorld().getName() : TNE.instance.defaultWorld;
    return getTimeLeft(world, inventory);
  }

  public void setTime(Player player, String inventory, long time) {
    String world = (TNE.configurations.getBoolean("Objects.Inventories.PerWorld", "objects")) ? player.getWorld().getName() : TNE.instance.defaultWorld;
    setTime(world, inventory, time);
  }

  private Long getTimeLeft(String world, String inventory) {
    return (credits.get(world).get(inventory) != null) ? credits.get(world).get(inventory) : 0L;
  }

  private void setTime(String world, String inventory, long time) {
    HashMap<String, Long> worldCredits = (credits.get(world) != null) ? credits.get(world) : new HashMap<String, Long>();
    worldCredits.put(inventory, time);
    credits.put(world, worldCredits);
  }
}