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
		sender.sendMessage(ChatColor.GOLD + "/lottery help - View general lottery command help.");
		if(sender.hasPermission("tne.admin") || sender.hasPermission("tne.lottery.admin")) {
			sender.sendMessage(ChatColor.GOLD + "/lottery create <name> <cost> - Create a new lottery.");
			sender.sendMessage(ChatColor.GOLD + "/lottery modify <cost/reward/info> <cost> - Create a new lottery.");
			sender.sendMessage(ChatColor.GOLD + "/lottery delete <name> - View the rewards for <lottery>.");
		}
		sender.sendMessage(ChatColor.GOLD + "/lottery list <global/world> - View a list of lotteries for <world> or global.");
		sender.sendMessage(ChatColor.GOLD + "/lottery reward <lottery> - View the rewards for <lottery>.");
		sender.sendMessage(ChatColor.GOLD + "/lottery buy <lottery> <amount> - Buy X amount of entries for <lottery>.");
		sender.sendMessage(ChatColor.GOLD + "/lottery collect - Collect your rewards for winning lotteries.");
	}
	
}