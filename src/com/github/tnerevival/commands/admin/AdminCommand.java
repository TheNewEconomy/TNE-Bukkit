package com.github.tnerevival.commands.admin;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.github.tnerevival.TNE;
import com.github.tnerevival.commands.TNECommand;

public class AdminCommand extends TNECommand {

	public AdminCommand(TNE plugin) {
		super(plugin);
		subCommands.add(new AdminReloadCommand(plugin));
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
		return false;
	}

	@Override
	public void help(CommandSender sender) {
		sender.sendMessage(ChatColor.GOLD + "~~~~~TNE Core Commands~~~~~");
		sender.sendMessage(ChatColor.GOLD + "/theneweconomy help - general TNE help");
		sender.sendMessage(ChatColor.GOLD + "/theneweconomy reload <all/config/mobs/worlds> - reload the TNE configurations or reload the specified file");
	}
	
}