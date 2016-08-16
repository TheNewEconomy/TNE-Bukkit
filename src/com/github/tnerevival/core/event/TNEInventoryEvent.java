package com.github.tnerevival.core.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.UUID;

/**
 * Created by Daniel on 8/15/2016.
 */
public class TNEInventoryEvent extends Event {

  private UUID player;
  private boolean viewOnly = true;

  public TNEInventoryEvent(UUID Player) {
    this.player = player;
  }

  public UUID getPlayer() {
    return player;
  }

  public boolean isViewOnly() {
    return viewOnly;
  }

  public void setViewOnly(boolean viewOnly) {
    this.viewOnly = viewOnly;
  }

  @Override
  public HandlerList getHandlers() {
    return null;
  }
}
