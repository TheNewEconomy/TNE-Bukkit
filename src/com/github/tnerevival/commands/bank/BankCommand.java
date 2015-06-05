package com.github.tnerevival.commands.bank;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.github.tnerevival.TNE;
import com.github.tnerevival.commands.TNECommand;

public class BankCommand extends TNECommand {

	public BankCommand(TNE plugin) {
		super(plugin);
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