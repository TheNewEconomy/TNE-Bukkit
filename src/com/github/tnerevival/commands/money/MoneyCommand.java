package com.github.tnerevival.commands.money;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.github.tnerevival.TNE;
import com.github.tnerevival.commands.TNECommand;

public class MoneyCommand extends TNECommand {

	public MoneyCommand(TNE plugin) {
		super(plugin);
		subCommands.add(new MoneyBalanceCommand(plugin));
		subCommands.add(new MoneyGiveCommand(plugin));
		subCommands.add(new MoneyPayCommand(plugin));
	}

	@Override
	public String getName() {
		return "money";
	}

	@Override
	public String[] getAliases() {
		return new String[0];
	}

	@Override
	public String getNode() {
		return "tne.money";
	}

	@Override
	public boolean console() {
		return false;
	}

	@Override
	public void help(CommandSender sender) {
		sender.sendMessage(ChatColor.GOLD + "~~~~~Money Commands~~~~~");
		sender.sendMessage(ChatColor.GOLD + "/money help - general money help");
		sender.sendMessage(ChatColor.GOLD + "/money balance - find out how much money you have on you");
		sender.sendMessage(ChatColor.GOLD + "/money give <player> <amount> - summon money from air and give it to a player");
		sender.sendMessage(ChatColor.GOLD + "/money pay <player> <amount> - pay a player money from your balance");	
	}
}