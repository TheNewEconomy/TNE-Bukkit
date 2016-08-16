package com.github.tnerevival.core.event.account;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.UUID;

public class TNEAccountEvent extends Event {
	private UUID id;

  public TNEAccountEvent(UUID id) {
    this.id = id;
  }

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  @Override
  public HandlerList getHandlers() {
    return null;
  }
}