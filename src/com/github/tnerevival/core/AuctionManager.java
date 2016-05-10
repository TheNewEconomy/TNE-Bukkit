package com.github.tnerevival.core;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.bukkit.Bukkit;

import com.github.tnerevival.TNE;
import com.github.tnerevival.auction.Auction;
import com.github.tnerevival.utils.MISCUtils;

public class AuctionManager {

	public Map<Integer, Auction> auctions = new HashMap<Integer, Auction>();
	
	public void work() {
		for(Integer lot : auctions.keySet()) {
			Auction a = get(lot);
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
	
	public boolean exists(Integer lot) {
		return auctions.containsKey(lot);
	}
	
	public Auction get(Integer lot) {
		if(exists(lot)) {
			return auctions.get(lot);
		}
		return null;
	}
	
	public void start(Integer lot) {
		Auction auction = get(lot);
		

		Message message = new Message("Messages.Auction.Win");
		message.addVariable("$amount", MISCUtils.formatBalance(auction.getWorld(), auction.getHighestBid().getAmount()));
		message.addVariable("$lot", "# " + lot);
		
	}
	
	public void end(Integer lot) {
		Auction auction = get(lot);
		
		Message message = new Message("Messages.Auction.Win");
		message.addVariable("$amount", MISCUtils.formatBalance(auction.getWorld(), auction.getHighestBid().getAmount()));
		message.addVariable("$lot", "# " + lot);
		
		MISCUtils.getPlayer(auction.getHighestBid().getBidder()).sendMessage(message.translate());
	}
	
	public void save(Integer lot) {
		
	}
}