package com.github.tnerevival.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.tnerevival.TNE;

public abstract class TNECommand {
	
	protected TNE plugin;
	
	public TNECommand(TNE plugin) {
		this.plugin = plugin;
	}

	public List<TNECommand> subCommands = new ArrayList<TNECommand>();
	public abstract String getName();
	public abstract String[] getAliases();
	public abstract String getNode();
	public abstract boolean console();
	public abstract void help(CommandSender sender);
	
	public boolean execute(CommandSender sender, String[] arguments) {
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
			sender.sendMessage(ChatColor.YELLOW + "Command \"/"  + getName() + " " + arguments[0] + "\" could not be found! Try using \"/"  + getName() + " help" + "\".");
			return false;
		}
		if(!sub.canExecute(sender)) {
			sender.sendMessage(ChatColor.RED + "I'm sorry, but you're not allowed to use that command.");
			return false;
		}
		return sub.execute(sender, removeSub(arguments));
	}
	
	private String[] removeSub(String[] oldArguments) {
		String[] arguments = new String[oldArguments.length - 1];
		for(int i = 1; i < oldArguments.length; i++) {
			arguments[i - 1] = oldArguments[i];
		}
		return arguments;
	}
	
	public TNECommand FindSub(String name) {
		for(TNECommand sub : subCommands) {
			if(sub.getName().equalsIgnoreCase(name)) {
				return sub;
			}
		}
		for(TNECommand sub : subCommands) {
			for(String s : sub.getAliases()) {
				if(s.equalsIgnoreCase(name)) {
					return sub;
				}
			}
		}
		return null;
	}
	
	public boolean canExecute(CommandSender sender) {
		if(sender instanceof Player) {
			return sender.hasPermission(getNode());
		}
		return console();
	}
	
	protected Player getPlayer(CommandSender sender) {
		if(sender instanceof Player) {
			return (Player)sender;
		}
		return null;
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