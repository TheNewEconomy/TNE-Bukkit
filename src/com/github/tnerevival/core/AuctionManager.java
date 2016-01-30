package com.github.tnerevival.core;

import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

import org.bukkit.Bukkit;

import com.github.tnerevival.TNE;
import com.github.tnerevival.auction.Auction;
import com.github.tnerevival.utils.MISCUtils;

public class AuctionManager {

	public NavigableMap<Integer, Auction> auctions = new TreeMap<Integer, Auction>();
	
	public void work() {
		for(Integer lot : auctions.keySet()) {
			Auction a = auctions.get(lot);
			if(a.getStart() > 0L) {
				int seconds = (int)TimeUnit.SECONDS.convert(System.nanoTime() - a.getStart(), TimeUnit.NANOSECONDS);
				
				if(seconds >= a.getLength()) {
					end(lot);
					continue;
				}
				
				if(seconds <= TNE.configurations.getInt("Core.Auction.CountdownTime") && TNE.configurations.getBoolean("Core.Auction.Countdown")) {
					announce(lot, seconds);
					continue;
				}
				
				if(seconds % TNE.configurations.getInt("Core.Auction.Interval") == 0 && TNE.configurations.getBoolean("Core.Auction.Announce")) {
					announce(lot, seconds);
				}
			}
		}
	}
	
	public void announce(Integer lot, int time) {
		String timeString = (time == 1)? " second" : " seconds";
		Message message = new Message("Messages.Auction.Announce");
		message.addVariable("$time", time + timeString);
		message.addVariable("$name", "# " + lot);
		
		Bukkit.broadcast(message.translate(), "TNE.Auction.Message." + lot);
	}
	
	public int getFreeID() {
		int free = auctions.size() + 1;
		for(int i = 1; i < free; i++) {
			if(!auctions.containsKey(i)) {
				return i;
			}
		}
		return free;
	}
	
	public void start(Integer lot) {
		Auction auction = auctions.get(lot);
		

		Message message = new Message("Messages.Auction.Win");
		message.addVariable("$amount", MISCUtils.formatBalance(auction.getWorld(), auction.getHighestBid().getAmount()));
		message.addVariable("$lot", "# " + lot);
		
	}
	
	public void end(Integer lot) {
		Auction auction = auctions.get(lot);
		
		Message message = new Message("Messages.Auction.Win");
		message.addVariable("$amount", MISCUtils.formatBalance(auction.getWorld(), auction.getHighestBid().getAmount()));
		message.addVariable("$lot", "# " + lot);
		
		MISCUtils.getPlayer(auction.getHighestBid().getBidder()).sendMessage(message.translate());
	}
	
	public void save(Integer lot) {
		
	}
}