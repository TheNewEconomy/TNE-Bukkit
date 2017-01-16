package com.github.tnerevival.core.event.object;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

/**
 * Created by Daniel on 10/12/2016.
 */
public class TNEObjectInteractionEvent extends Event implements Cancellable {
  private static final HandlerList handlers = new HandlerList();
  private boolean cancelled = false;

  private Player player;
  private ItemStack stack;
  private String identifier;
  private InteractionType type;
  private int amount = 1;

  public TNEObjectInteractionEvent(Player player, ItemStack stack, String identifier, InteractionType type) {
    this(player, stack, identifier, type, 1);
  }

  public TNEObjectInteractionEvent(Player player, ItemStack stack, String identifier, InteractionType type, int amount) {
    this.player = player;
    this.stack = stack;
    this.identifier = identifier;
    this.type = type;
    this.amount = amount;
  }

  @Override
  public boolean isCancelled() {
    return cancelled;
  }

  @Override
  public void setCancelled(boolean cancelled) {
    this.cancelled = cancelled;
  }

  @Override
  public HandlerList getHandlers() {
    return handlers;
  }

  public static HandlerList getHandlerList() {
    return handlers;
  }

  public ItemStack getStack() {
    return stack;
  }

  public Player getPlayer() {
    return player;
  }

  public String getIdentifier() {
    return identifier;
  }

  public InteractionType getType() {
    return type;
  }

  public int getAmount() {
    return amount;
  }
}
