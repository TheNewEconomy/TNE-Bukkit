package com.github.tnerevival.core.event.qualityoflife;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by creatorfromhell on 3/7/2017.
 * All rights reserved.
 **/
public class InventorySlotChangedEvent extends Event {
  private static final HandlerList handlers = new HandlerList();
  private Map<Integer, ItemStack> old = new HashMap<>();
  private Map<Integer, ItemStack> updated = new HashMap<>();

  private Inventory inventory;
  private Player player;

  public InventorySlotChangedEvent(Inventory inventory, Player player) {
    this.inventory = inventory;
    this.player = player;
  }

  @Override
  public HandlerList getHandlers() {
    return handlers;
  }

  public Map<Integer, ItemStack> getOld() {
    return old;
  }

  public void setOld(Map<Integer, ItemStack> old) {
    this.old = old;
  }

  public Map<Integer, ItemStack> getUpdated() {
    return updated;
  }

  public void setUpdated(Map<Integer, ItemStack> updated) {
    this.updated = updated;
  }

  public Inventory getInventory() {
    return inventory;
  }

  public void setInventory(Inventory inventory) {
    this.inventory = inventory;
  }

  public Player getPlayer() {
    return player;
  }

  public void setPlayer(Player player) {
    this.player = player;
  }
}