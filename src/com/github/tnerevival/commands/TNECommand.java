package com.github.tnerevival.commands;

import com.github.tnerevival.TNE;
import com.github.tnerevival.core.Message;
import com.github.tnerevival.utils.MISCUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public abstract class TNECommand {
	
	protected TNE plugin;
	
	public TNECommand(TNE plugin) {
		this.plugin = plugin;
	}

	public List<TNECommand> subCommands = new ArrayList<>();
	public abstract String getName();
	public abstract String[] getAliases();
	public abstract String getNode();
	public abstract boolean console();
	public abstract void help(CommandSender sender);
	
	public boolean execute(CommandSender sender, String command, String[] arguments) {
		if(arguments.length == 0) {
			help(sender);
			return false;
		}
		
		TNECommand sub = FindSub(arguments[0]);
		if(sub == null && !arguments[0].equalsIgnoreCase("help")) {
			Message noCommand = new Message("Messages.Command.None");
			noCommand.addVariable("$command", "/" + getName());
			noCommand.addVariable("$arguments", arguments[0]);
			noCommand.translate(MISCUtils.getWorld(getPlayer(sender)), getPlayer(sender));
			return false;
		}

		if(arguments[0].equalsIgnoreCase("help")) {
			help(sender);
			return false;
		}

		if(sub.canExecute(sender) && arguments.length >= 2 && arguments[1].equalsIgnoreCase("?")) {
		  sub.help(sender);
      return false;
    }


		if(!sub.canExecute(sender)) {
			Message unable = new Message("Messages.Command.Unable");
			unable.addVariable("$command", "/" + getName());
			unable.translate(MISCUtils.getWorld(getPlayer(sender)), getPlayer(sender));
			return false;
		}
		return sub.execute(sender, command, removeSub(arguments));
	}
	
	protected String[] removeSub(String[] oldArguments) {
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
	
	@SuppressWarnings("deprecation")
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