package com.github.tnerevival.core.auctions;

import org.bukkit.inventory.ItemStack;

/**
 * Hold all information pertaining to a specific Auction.
 * @author creatorfromhell
 *
 */
public class Auction {
	
	String creator;
	Integer length;
	Double minBid;
	Double increment;
	Double currentBid;
	String highestBidder;
	ItemStack item;
	
	Boolean queued;
	Boolean finished;
	
	public Auction(String creator, ItemStack item) {
		this(creator, 0.0, 1.0, item);
	}
	
	public Auction(String creator, Double minBid, ItemStack item) {
		this(creator, minBid, 1.0, item);
	}
	
	public Auction(String creator, Double minBid, Double increment, ItemStack item) {
		this.creator = creator;
		this.minBid = minBid;
		this.increment = increment;
		this.currentBid = minBid;
		this.highestBidder = "none";
		this.item = item;
	}

	/**
	 * @return the creator
	 */
	public String getCreator() {
		return creator;
	}

	/**
	 * @param creator the creator to set
	 */
	public void setCreator(String creator) {
		this.creator = creator;
	}

	/**
	 * @return the minBid
	 */
	public Double getMinBid() {
		return minBid;
	}

	/**
	 * @param minBid the minBid to set
	 */
	public void setMinBid(Double minBid) {
		this.minBid = minBid;
	}

	/**
	 * @return the increment
	 */
	public Double getIncrement() {
		return increment;
	}

	/**
	 * @param increment the increment to set
	 */
	public void setIncrement(Double increment) {
		this.increment = increment;
	}

	/**
	 * @return the currentBid
	 */
	public Double getCurrentBid() {
		return currentBid;
	}

	/**
	 * @param currentBid the currentBid to set
	 */
	public void setCurrentBid(Double currentBid) {
		this.currentBid = currentBid;
	}

	/**
	 * @return the highestBidder
	 */
	public String getHighestBidder() {
		return highestBidder;
	}

	/**
	 * @param highestBidder the highestBidder to set
	 */
	public void setHighestBidder(String highestBidder) {
		this.highestBidder = highestBidder;
	}

	/**
	 * @return the item
	 */
	public ItemStack getItem() {
		return item;
	}

	/**
	 * @param item the item to set
	 */
	public void setItem(ItemStack item) {
		this.item = item;
	}

	/**
	 * @return the queued
	 */
	public Boolean getQueued() {
		return queued;
	}

	/**
	 * @param queued the queued to set
	 */
	public void setQueued(Boolean queued) {
		this.queued = queued;
	}

	/**
	 * @return the finished
	 */
	public Boolean getFinished() {
		return finished;
	}

	/**
	 * @param finished the finished to set
	 */
	public void setFinished(Boolean finished) {
		this.finished = finished;
	}
}