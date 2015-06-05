package com.github.tnerevival.commands;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.tnerevival.TNE;

public abstract class Command {
	
	protected TNE plugin;
	
	public Command(TNE plugin) {
		this.plugin = plugin;
	}
	
	public abstract String getName();
	public abstract String[] getAliases();
	public abstract String getNode();
	public abstract boolean console();
	public abstract void execute(CommandSender sender, String[] arguments);
	public abstract void help(CommandSender sender);
	
	protected boolean canExecute(CommandSender sender) {
		if(sender instanceof Player) {
			return sender.hasPermission(getNode());
		}
		return console();
	}
	
	protected Player getPlayer(CommandSender sender, String username) {
		if(username != null) {
			List<Player> matches = sender.getServer().matchPlayer(username);
			if(!matches.isEmpty()) {
				return matches.get(0);
			}
			sender.sendMessage(ChatColor.WHITE + "Player \"" + ChatColor.RED + username + ChatColor.WHITE + "\" could not be found!");
			return null;
		} else {
			if(sender instanceof Player) {
				return (Player)sender;
			}
		}
		return null;
	}
}