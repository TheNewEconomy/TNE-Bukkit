package com.github.tnerevival.core.event;

import org.bukkit.OfflinePlayer;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.github.tnerevival.utils.AccountUtils;

public class TNEFundsPayEvent extends Event implements Cancellable {
	private static final HandlerList handlers = new HandlerList();
    
    private Boolean cancelled;
    
    private OfflinePlayer to;
    private OfflinePlayer from;
    
    private Double amount;
 
    public TNEFundsPayEvent(OfflinePlayer to, OfflinePlayer from, Double amount) {
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
	 * @return The player who is receiving the payment.
	 */
	public OfflinePlayer getReceiver() {
		return to;
	}
	
	/**
	 * @return The player who is sending the payment.
	 */
	public OfflinePlayer getSender() {
		return from;
	}

	/**
	 * @return The amount of money being paid to the receiver
	 */
	public Double getAmount() {
		return amount;
	}

	/**
	 * @param amount The amount of money to be paid to the receiver.
	 */
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	
	/**
	 * 
	 * @return Returns the balance of the player who is receiving the payment beforehand.
	 */
	public Double getReceiverPreviousBalance() {
		return AccountUtils.getFunds(to.getUniqueId());
	}
	
	/**
	 * 
	 * @return Returns what the balance of the player who is receiving the payment will be afterwards.
	 */
	public Double getReceiverNewBalance() {
		return (AccountUtils.getFunds(to.getUniqueId()) + getAmount());
	}
	
	/**
	 * 
	 * @return Returns the balance of the player who is sending the payment beforehand.
	 */
	public Double getSenderPreviousBalance() {
		return AccountUtils.getFunds(from.getUniqueId());
	}
	
	/**
	 * 
	 * @return Returns what the balance of the player who is sending the payment will be afterwards.
	 */
	public Double getSenderNewBalance() {
		return (AccountUtils.getFunds(from.getUniqueId()) + getAmount());
	}
}