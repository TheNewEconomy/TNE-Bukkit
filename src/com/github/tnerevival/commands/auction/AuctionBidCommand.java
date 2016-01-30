package com.github.tnerevival.commands.auction;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.github.tnerevival.TNE;
import com.github.tnerevival.commands.TNECommand;

public class AuctionBidCommand extends TNECommand {
	
	public AuctionBidCommand(TNE plugin) {
		super(plugin);
	}

	@Override
	public String getName() {
		return "bid";
	}

	@Override
	public String[] getAliases() {
		return new String[0];
	}

	@Override
	public String getNode() {
		return "tne.auction.bid";
	}

	@Override
	public boolean console() {
		return false;
	}
	
	@Override
	public boolean execute(CommandSender sender, String[] arguments) {
		return true;
	}

	@Override
	public void help(CommandSender sender) {
		sender.sendMessage(ChatColor.GOLD + "/auction bid <lot> <amount> - Bid <amount> for the specified auction lot.");
	}
	
}