package com.github.tnerevival.core.event;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.github.tnerevival.utils.AccountUtils;

public class TNEFundsRemoveEvent extends Event implements Cancellable {
	private static final HandlerList handlers = new HandlerList();
    
    private Boolean cancelled;
    
    private String username;
    
    private Double amount;
 
    public TNEFundsRemoveEvent(String username, Double amount) {
    	this.username = username;
    	this.amount = amount;
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
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return The amount being removed from the player's balance.
	 */
	public Double getAmount() {
		return amount;
	}

	/**
	 * @param amount The amount to be removed from the player's balance.
	 */
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	
	/**
	 * 
	 * @return Returns the player's balance before the funds are removed.
	 */
	public Double getPreviousBalance() {
		return AccountUtils.getFunds(username);
	}
	
	/**
	 * 
	 * @return Returns what the player's balance will be after the funds are removed.
	 */
	public Double getNewBalance() {
		return (AccountUtils.getFunds(username) - getAmount());
	}
}