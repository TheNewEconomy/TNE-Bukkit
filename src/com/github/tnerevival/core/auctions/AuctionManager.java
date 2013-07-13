package com.github.tnerevival.core.auctions;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.github.tnerevival.TheNewEconomy;
import com.github.tnerevival.core.accounts.Account;

/**
 * Holds methods vital to managing an Auction.
 * @author creatorfromhell
 *
 */
public class AuctionManager {
	
	/**
	 * Starts the specified Auction.
	 * @param auction
	 */
	public void startAuction(Auction auction) {
		//TODO: Create startAuction Method.
	}
	
	/**
	 * Bids on the specified Auction for the Player specified.
	 * If the player's bid is lower than the highest bid then it is rejected.
	 * @param player
	 * @param auction
	 */
	public void bid(Player player, Double bid, Auction auction) {
		if(bid <= auction.getCurrentBid()) {
			player.sendMessage("I'm sorry, but your bid is lower than the current bid of " + auction.getCurrentBid() + ".");
		} else {
			if(bid - auction.getCurrentBid() < auction.getIncrement()) {
				player.sendMessage("I'm sorry, but your bid is lower than the increment of " + auction.getIncrement() + " per bid.");
			} else {
				Account account = TheNewEconomy.instance.eco.accounts.get(player.getName());
				if(account.getBalance() < bid) {
					player.sendMessage("I'm sorry, but you can't afford this bid.");
				} else {
					auction.setCurrentBid(bid);
					auction.setHighestBidder(player.getName());
				}
			}
		}
 	}
	
	/**
	 * Gets the next Auction in the queue.
	 */
	public void getNextAuction() {
		//TODO: Create getNextAuction Method.
	}
	
	/**
	 * Cancels the specified Auction. Cancellation request will be denied if someone has already bid.
	 * @param auction
	 */
	public void cancelAuction(Auction auction) {
		Player auctioner = Bukkit.getPlayer(auction.getCreator());
		if(auction.getHighestBidder().equals("none")) {
			auctioner.getInventory().addItem(auction.getItem());
			auctioner.sendMessage("The auction has been cancelled, and your items have been returned.");	
		} else {
			auctioner.sendMessage("I'm sorry, but you cannot cancel the auction at this time.");
		}
	}
	
	/**
	 * Closes the specified Auction. Will be cancelled if no one has bid.
	 * @param auction
	 */
	public void closeAuction(Auction auction) {
		Player auctioner = Bukkit.getPlayer(auction.getCreator());
		if(auction.getHighestBidder().equals("none")) {
			auctioner.getInventory().addItem(auction.getItem());
			auctioner.sendMessage("The auction was closed with no bids. Your items have been returned.");
		} else {
			Player winner = Bukkit.getPlayer(auction.getHighestBidder());
			Account aucAccount = TheNewEconomy.instance.eco.accounts.get(auctioner.getName());
			Account winAccount = TheNewEconomy.instance.eco.accounts.get(winner.getName());
			winner.getInventory().addItem(auction.getItem());
			winAccount.setBalance(winAccount.getBalance() - auction.getCurrentBid());
			aucAccount.setBalance(aucAccount.getBalance() + auction.getCurrentBid());
			auctioner.sendMessage("Auction closed with a winning bid of " + auction.getCurrentBid() + ", which has been added to your account.");
			winner.sendMessage("The auction was closed, and you had the highest bid. Your item is now in your inventory.");
		}
	}
	
	/**
	 * Disables every Auction that is currently running, and/or queued.
	 */
	public void disableAuctions() {
		for(String s : TheNewEconomy.instance.eco.auctions.keySet()) {
			Auction auction = TheNewEconomy.instance.eco.auctions.get(s);
			//Close all auctions that are in the auctions map.
			closeAuction(auction);
		}
	}
}