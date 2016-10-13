package com.github.tnerevival.core.event.object;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Created by Daniel on 10/12/2016.
 */
public class TNEObjectInteractionEvent extends Event implements Cancellable {
  private static final HandlerList handlers = new HandlerList();
  private boolean cancelled = false;

  private Player player;
  private String identifier;
  private InteractionType type;
  private int amount = 1;

  public TNEObjectInteractionEvent(Player player, String identifier, InteractionType type) {
    this(player, identifier, type, 1);
  }

  public TNEObjectInteractionEvent(Player player, String identifier, InteractionType type, int amount) {
    this.player = player;
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

  public Player getPlayer() {
    return player;
  }

  public void setPlayer(Player player) {
    this.player = player;
  }

  public String getIdentifier() {
    return identifier;
  }

  public void setIdentifier(String identifier) {
    this.identifier = identifier;
  }

  public InteractionType getType() {
    return type;
  }

  public void setType(InteractionType type) {
    this.type = type;
  }

  public int getAmount() {
    return amount;
  }

  public void setAmount(int amount) {
    this.amount = amount;
  }
}
