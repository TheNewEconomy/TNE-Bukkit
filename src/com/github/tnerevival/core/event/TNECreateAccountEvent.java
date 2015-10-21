package com.github.tnerevival.core.event;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.github.tnerevival.account.Account;

public class TNECreateAccountEvent extends Event implements Cancellable {
	private static final HandlerList handlers = new HandlerList();
    
    private Boolean cancelled = false;
    
    private UUID id;
 
    public TNECreateAccountEvent(UUID id) {
    	this.id = id;
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
    
    public UUID getID() {
    	return this.id;
    }

	/**
	 * @return the player
	 */
	public OfflinePlayer getPlayer() {
		return Bukkit.getOfflinePlayer(id);
	}

	/**
	 * @param player the player to set
	 */
	public void setPlayer(OfflinePlayer player) {
		this.id = player.getUniqueId();
	}
	
	public Account getAccount() {
		Account account = new Account(this.id);
		return account;
	}
}