package com.github.tnerevival.auction;

import java.io.Serializable;
import java.util.UUID;

public class Bid implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private UUID bidder;
	private Double amount;
	
	public Bid(UUID bidder, Double amount) {
		this.bidder = bidder;
		this.amount = amount;
	}

	public UUID getBidder() {
		return bidder;
	}

	public void setBidder(UUID bidder) {
		this.bidder = bidder;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}
}