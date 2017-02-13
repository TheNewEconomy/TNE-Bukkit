package com.github.tnerevival.core.event.qualityoflife;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 * Created by creatorfromhell on 2/13/2017.
 * All rights reserved.
 **/
public class InventorySlotChangeEvent extends Event implements Cancellable {
  private static final HandlerList handlers = new HandlerList();
  private boolean cancelled = false;

  private Player player;
  private String world;
  private Inventory inventory;
  private int slot;
  private ItemStack previous;
  private ItemStack current;

  public InventorySlotChangeEvent(Player player, String world, Inventory inventory, int slot, ItemStack previous, ItemStack current) {
    this.player = player;
    this.world = world;
    this.inventory = inventory;
    this.slot = slot;
    this.previous = previous;
    this.current = current;
  }

  public Player getPlayer() {
    return player;
  }

  public void setPlayer(Player player) {
    this.player = player;
  }

  public String getWorld() {
    return world;
  }

  public void setWorld(String world) {
    this.world = world;
  }

  public Inventory getInventory() {
    return inventory;
  }

  public void setInventory(Inventory inventory) {
    this.inventory = inventory;
  }

  public int getSlot() {
    return slot;
  }

  public void setSlot(int slot) {
    this.slot = slot;
  }

  public ItemStack getPrevious() {
    return previous;
  }

  public void setPrevious(ItemStack previous) {
    this.previous = previous;
  }

  public ItemStack getCurrent() {
    return current;
  }

  public void setCurrent(ItemStack current) {
    this.current = current;
  }

  @Override
  public HandlerList getHandlers() {
    return handlers;
  }

  @Override
  public boolean isCancelled() {
    return cancelled;
  }

  @Override
  public void setCancelled(boolean cancelled) {
    this.cancelled = cancelled;
  }
}