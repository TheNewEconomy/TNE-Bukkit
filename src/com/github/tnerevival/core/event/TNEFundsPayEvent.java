package com.github.tnerevival.core.event;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.github.tnerevival.utils.AccountUtils;

public class TNEFundsPayEvent extends Event implements Cancellable {
	private static final HandlerList handlers = new HandlerList();
    
    private Boolean cancelled;
    
    private String to;
    private String from;
    
    private Double amount;
 
    public TNEFundsPayEvent(String to, String from, Double amount) {
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
	 * @return The name of the player who is receiving the payment.
	 */
	public String getReceiver() {
		return to;
	}
	
	/**
	 * @return The name of the player who is sending the payment.
	 */
	public String getSender() {
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
		return AccountUtils.getFunds(to);
	}
	
	/**
	 * 
	 * @return Returns what the balance of the player who is receiving the payment will be afterwards.
	 */
	public Double getReceiverNewBalance() {
		return (AccountUtils.getFunds(to) + getAmount());
	}
	
	/**
	 * 
	 * @return Returns the balance of the player who is sending the payment beforehand.
	 */
	public Double getSenderPreviousBalance() {
		return AccountUtils.getFunds(from);
	}
	
	/**
	 * 
	 * @return Returns what the balance of the player who is sending the payment will be afterwards.
	 */
	public Double getSenderNewBalance() {
		return (AccountUtils.getFunds(from) + getAmount());
	}
}