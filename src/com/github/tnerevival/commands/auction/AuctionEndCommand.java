package com.github.tnerevival.commands.auction;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.github.tnerevival.TNE;
import com.github.tnerevival.commands.TNECommand;

public class AuctionEndCommand extends TNECommand {
	
	public AuctionEndCommand(TNE plugin) {
		super(plugin);
	}

	@Override
	public String getName() {
		return "end";
	}

	@Override
	public String[] getAliases() {
		return new String[0];
	}

	@Override
	public String getNode() {
		return "tne.auction.end";
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
		sender.sendMessage(ChatColor.GOLD + "/auction end [lot] - Ends the current auction. Lot number is required when multiple auctions are allowed at once.");
	}
	
}