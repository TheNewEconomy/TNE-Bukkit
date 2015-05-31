package com.github.tnerevival.core.event;

import org.bukkit.OfflinePlayer;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.github.tnerevival.account.Account;

public class TNECreateAccountEvent extends Event implements Cancellable {
	private static final HandlerList handlers = new HandlerList();
    
    private Boolean cancelled;
    
    private OfflinePlayer player;
 
    public TNECreateAccountEvent(OfflinePlayer player) {
    	this.player = player;
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

	/**
	 * @return the player
	 */
	public OfflinePlayer getPlayer() {
		return player;
	}

	/**
	 * @param player the player to set
	 */
	public void setPlayer(OfflinePlayer player) {
		this.player = player;
	}
	
	public Account getAccount() {
		Account account = new Account(player);
		return account;
	}
}