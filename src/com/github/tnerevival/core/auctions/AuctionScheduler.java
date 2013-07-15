package com.github.tnerevival.core.auctions;

import org.bukkit.Material;


public class AuctionScheduler implements Runnable {
	
	Auction auction;
	Material material;
	
	public AuctionScheduler(Auction auction) {
		this.auction = auction;
		this.material = auction.getItem().getType();
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
	}

}