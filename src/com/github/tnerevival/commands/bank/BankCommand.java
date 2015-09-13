package com.github.tnerevival.commands.bank;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.tnerevival.TNE;
import com.github.tnerevival.commands.TNECommand;
import com.github.tnerevival.core.Message;
import com.github.tnerevival.utils.BankUtils;

public class BankCommand extends TNECommand {

	public BankCommand(TNE plugin) {
		super(plugin);
		subCommands.add(new BankBalanceCommand(plugin));
		subCommands.add(new BankBuyCommand(plugin));
		subCommands.add(new BankDepositCommand(plugin));
		subCommands.add(new BankPriceCommand(plugin));
		subCommands.add(new BankViewCommand(plugin));
		subCommands.add(new BankWithdrawCommand(plugin));
	}

	@Override
	public String getName() {
		return "bank";
	}

	@Override
	public String[] getAliases() {
		return new String[0];
	}

	@Override
	public String getNode() {
		return "tne.bank";
	}

	@Override
	public boolean console() {
		return false;
	}
	
	@Override
	public boolean execute(CommandSender sender, String[] arguments) {
		Player player = getPlayer(sender);
		if(!BankUtils.enabled(player.getWorld().getName())) {
			player.sendMessage(new Message("Messages.Bank.Disabled").translate());
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
		sender.sendMessage(ChatColor.GOLD + "~~~~~Bank Commands~~~~~");
		sender.sendMessage(ChatColor.GOLD + "/bank help - general bank help");
		sender.sendMessage(ChatColor.GOLD + "/bank balance - find out how much gold is in your bank");
		sender.sendMessage(ChatColor.GOLD + "/bank buy - buy yourself a bank");
		sender.sendMessage(ChatColor.GOLD + "/bank deposit <amount> - put the specified amount of money in your bank");
		sender.sendMessage(ChatColor.GOLD + "/bank price - see how much a bank cost");
		sender.sendMessage(ChatColor.GOLD + "/bank view - view your bank");
		sender.sendMessage(ChatColor.GOLD + "/bank withdraw <amount> - withdraw the specified amout of money from your bank");
	}
	
}