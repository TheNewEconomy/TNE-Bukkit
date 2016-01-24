package com.github.tnerevival.commands.packages;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.github.tnerevival.TNE;
import com.github.tnerevival.commands.TNECommand;
import com.github.tnerevival.core.Message;

public class PackageCommand extends TNECommand {

	public PackageCommand(TNE plugin) {
		super(plugin);
		subCommands.add(new PackageListCommand(plugin));
		subCommands.add(new PackageBuyCommand(plugin));
	}

	@Override
	public String getName() {
		return "package";
	}

	@Override
	public String[] getAliases() {
		return new String[0];
	}

	@Override
	public String getNode() {
		return "tne.package";
	}

	@Override
	public boolean console() {
		return false;
	}
	
	@Override
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
		sender.sendMessage(ChatColor.GOLD + "~~~~~PAckage Commands~~~~~");
		sender.sendMessage(ChatColor.GOLD + "/package help - View general package command help.");
		sender.sendMessage(ChatColor.GOLD + "/package list <type> - List all packages for the specified inventory <type>");
		sender.sendMessage(ChatColor.GOLD + "/package buy <type> <package> - Buy <package> for inventory <type>");
	}
	
}