package com.github.tnerevival.core.event;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.github.tnerevival.utils.AccountUtils;

public class TNEFundsGiveEvent extends Event implements Cancellable {
	private static final HandlerList handlers = new HandlerList();
    
    private Boolean cancelled;
    
    private String to;
    private String from;
    
    private Double amount;
 
    public TNEFundsGiveEvent(String to, String from, Double amount) {
    	this.to = to;
    	this.from = from;
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
	 * @return The name of the player who is being given the money.
	 */
	public String getReceiver() {
		return to;
	}
	
	/**
	 * @return The name of the player who is giving the money.
	 */
	public String getSender() {
		return from;
	}

	/**
	 * @return the amount
	 */
	public Double getAmount() {
		return amount;
	}

	/**
	 * @param amount the amount to set
	 */
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	
	/**
	 * 
	 * @return Returns the balance of the player who is being given the money beforehand.
	 */
	public Double getReceiverPreviousBalance() {
		return AccountUtils.getFunds(to);
	}
	
	/**
	 * 
	 * @return Returns what the balance of the player who is being given the money will be afterwards.
	 */
	public Double getReceiverNewBalance() {
		return (AccountUtils.getFunds(to) + getAmount());
	}
}