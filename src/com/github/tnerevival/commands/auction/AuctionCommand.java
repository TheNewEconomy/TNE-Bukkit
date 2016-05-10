package com.github.tnerevival.commands.auction;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.tnerevival.TNE;
import com.github.tnerevival.account.Account;
import com.github.tnerevival.commands.TNECommand;
import com.github.tnerevival.core.Message;
import com.github.tnerevival.utils.AccountUtils;
import com.github.tnerevival.utils.MISCUtils;

public class AuctionCommand extends TNECommand {

	public AuctionCommand(TNE plugin) {
		super(plugin);
	}

	@Override
	public String getName() {
		return "auction";
	}

	@Override
	public String[] getAliases() {
		return new String[0];
	}

	@Override
	public String getNode() {
		return "tne.auction";
	}

	@Override
	public boolean console() {
		return false;
	}
	
	@Override
	public boolean execute(CommandSender sender, String[] arguments) {
		
		Player player = getPlayer(sender);
		Account acc = AccountUtils.getAccount(MISCUtils.getID(player));
		
		if(!acc.getStatus().getBank()) {
			Message locked = new Message("Messages.Account.Locked");
			locked.addVariable("$player", player.getDisplayName());
			sender.sendMessage(locked.translate());
			return false;
		}
		
		if(acc.getPin().equalsIgnoreCase("TNENOSTRINGVALUE")) {
			Message set = new Message("Messages.Account.Set");
			sender.sendMessage(set.translate());
			return false;
		}
		
		if(!acc.getPin().equalsIgnoreCase("TNENOSTRINGVALUE") && !TNE.instance.manager.confirmed.contains(MISCUtils.getID(player))) {
			Message confirm = new Message("Messages.Account.Confirm");
			sender.sendMessage(confirm.translate());
			return false;
		}
		
		if(arguments.length == 0) {
			help(sender);
			return false;
		}
		
		if(arguments[0].equalsIgnoreCase("help")) {
			help(sender);
			return false;
		}
		
		TNECommand sub = FindSub(arguments[0]);
		if(sub == null) {
			Message noCommand = new Message("Messages.Command.None");
			noCommand.addVariable("$command", "/" + getName());
			noCommand.addVariable("$arguments", arguments[0]);
			sender.sendMessage(noCommand.translate());
			return false;
		}
		if(!sub.canExecute(sender)) {
			Message unable = new Message("Messages.Command.Unable");
			unable.addVariable("$command", "/" + getName());
			sender.sendMessage(unable.translate());
			return false;
		}
		return sub.execute(sender, removeSub(arguments));
	}

	@Override
	public void help(CommandSender sender) {
		sender.sendMessage(ChatColor.GOLD + "~~~~~Lottery Commands~~~~~");
		sender.sendMessage(ChatColor.GOLD + "/auction help - View general auction command help.");
		if(sender.hasPermission("tne.admin") || sender.hasPermission("tne.auction.admin")) {
			sender.sendMessage(ChatColor.GOLD + "/auction lock [lot] - Locks the current auction or auction with [lot] number.");
			sender.sendMessage(ChatColor.GOLD + "/auction stop [lot] - Stops the current auction or auction for [lot] without transferring ownership of the item.");
		}
		sender.sendMessage(ChatColor.GOLD + "/auction bid [lot] <amount> - Places a bid of <amount> for the current auction or auction with lot [lot].");
		sender.sendMessage(ChatColor.GOLD + "/auction cancel [lot] - Cancels your current auction or auction with lot [lot].");
		sender.sendMessage(ChatColor.GOLD + "/auction end [lot] - Ends your current auction or auction with lot [lot], and transfers ownership of the item.");
		sender.sendMessage(ChatColor.GOLD + "/auction list - Lists all auctions, current and upcoming.");
		sender.sendMessage(ChatColor.GOLD + "/auction mute [lot] - Mutes the auction with the specified lot number or all if no lot is specified.");
		sender.sendMessage(ChatColor.GOLD + "/auction start <starting_bid> [amount] [bid_increment] [length(seconds)] [delay(seconds)] [world/global] - Starts an auction for the item in your hand for <starting_bid>.");
		sender.sendMessage(ChatColor.GOLD + "/auction view [lot] - Views the current auction or auction with lot [lot].");
	}
}