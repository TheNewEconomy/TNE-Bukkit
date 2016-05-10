package com.github.tnerevival.core.eventold;

import org.bukkit.OfflinePlayer;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.github.tnerevival.utils.AccountUtils;
import com.github.tnerevival.utils.MISCUtils;

public class TNEFundsTakeEvent extends Event implements Cancellable {
	private static final HandlerList handlers = new HandlerList();
    
    private Boolean cancelled = false;
    
    private OfflinePlayer to;
    private OfflinePlayer from;
    
    private Double amount;
 
    public TNEFundsTakeEvent(OfflinePlayer to, OfflinePlayer from, Double amount) {
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
	 * @return The player who is being given the money.
	 */
	public OfflinePlayer getTarget() {
		return to;
	}
	
	/**
	 * @return The player who is giving the money.
	 */
	public OfflinePlayer getTaker() {
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
	public Double getTargetPreviousBalance() {
		return AccountUtils.getFunds(MISCUtils.getID(to));
	}
	
	/**
	 * 
	 * @return Returns what the balance of the player who is being given the money will be afterwards.
	 */
	public Double getTargetNewBalance() {
		return (AccountUtils.getFunds(MISCUtils.getID(to)) + getAmount());
	}
}