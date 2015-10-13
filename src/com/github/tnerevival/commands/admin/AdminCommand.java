package com.github.tnerevival.commands.admin;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.github.tnerevival.TNE;
import com.github.tnerevival.commands.TNECommand;

public class AdminCommand extends TNECommand {

	public AdminCommand(TNE plugin) {
		super(plugin);
		subCommands.add(new AdminBackupCommand(plugin));
		subCommands.add(new AdminBalanceCommand(plugin));
		subCommands.add(new AdminBankCommand(plugin));
		subCommands.add(new AdminReloadCommand(plugin));
		subCommands.add(new AdminSaveCommand(plugin));
	}

	@Override
	public String getName() {
		return "theneweconomy";
	}

	@Override
	public String[] getAliases() {
		return new String[] { "tne" };
	}

	@Override
	public String getNode() {
		return "tne.admin";
	}

	@Override
	public boolean console() {
		return true;
	}

	@Override
	public void help(CommandSender sender) {
		sender.sendMessage(ChatColor.GOLD + "~~~~~TNE Core Commands~~~~~");
		sender.sendMessage(ChatColor.GOLD + "/theneweconomy backup - Saves & back ups the TNE Database file.(currently only FlatFile and SQLITE)");
		sender.sendMessage(ChatColor.GOLD + "/theneweconomy balance <player> [world] - Check the specified player's balance for [world]");
		sender.sendMessage(ChatColor.GOLD + "/theneweconomy bank <player> [world] - View the specified player's bank for [world]");
		sender.sendMessage(ChatColor.GOLD + "/theneweconomy help - general TNE help");
		sender.sendMessage(ChatColor.GOLD + "/theneweconomy reload <all/config/mobs/worlds> - reload the TNE configurations or reload the specified file");
		sender.sendMessage(ChatColor.GOLD + "/theneweconomy save - force saves all TNE data");
	}
	
}