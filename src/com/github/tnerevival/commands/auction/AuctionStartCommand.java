package com.github.tnerevival.commands.auction;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.github.tnerevival.TNE;
import com.github.tnerevival.commands.TNECommand;

public class AuctionStartCommand extends TNECommand {
	
	public AuctionStartCommand(TNE plugin) {
		super(plugin);
	}

	@Override
	public String getName() {
		return "start";
	}

	@Override
	public String[] getAliases() {
		return new String[0];
	}

	@Override
	public String getNode() {
		return "tne.auction.start";
	}

	@Override
	public boolean console() {
		return false;
	}
	
	@Override
	public boolean execute(CommandSender sender, String[] arguments) {
		
		if(arguments.length >= 1) {
			double starting = Integer.parseInt(arguments[0]);
			double amount = 1;
			double increment = 5;
			long length = 60000;
			boolean global = true;
		}
		help(sender);
		return false;
	}

	@Override
	public void help(CommandSender sender) {
		sender.sendMessage(ChatColor.GOLD + "/auction start <starting_bid> [amount] [bid_increment] [length(seconds)] [delay(seconds)] [world/global] - Starts an auction for the item in your hand for <starting_bid>.");
	}
	
}