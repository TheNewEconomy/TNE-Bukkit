package com.github.tnerevival.core.event;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class TNEDeleteAccountEvent extends Event implements Cancellable {
	private static final HandlerList handlers = new HandlerList();
    
    private Boolean cancelled;
 
    public TNEDeleteAccountEvent(String username) {
    }
 
    public HandlerList getHandlers() {
        return handlers;
    }
 
    public static HandlerList getHandlerList() {
        return handlers;
    }

	public boolean isCancelled() {
        return cancelled;
    }
 
    public void setCancelled(boolean cancel) {
        cancelled = cancel;
    }
}