package com.github.tnerevival.commands.pin;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.github.tnerevival.TNE;
import com.github.tnerevival.commands.TNECommand;
import com.github.tnerevival.core.Message;

public class PinCommand extends TNECommand {

	public PinCommand(TNE plugin) {
		super(plugin);
		
		subCommands.add(new PinSetCommand(plugin));
		subCommands.add(new PinConfirmCommand(plugin));
	}

	@Override
	public String getName() {
		return "pin";
	}

	@Override
	public String[] getAliases() {
		return new String[0];
	}

	@Override
	public String getNode() {
		return "tne.pin";
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
		sender.sendMessage(ChatColor.GOLD + "~~~~~Pin Commands~~~~~");
		sender.sendMessage(ChatColor.GOLD + "/pin confirm <pin> - Cofirm your identity with your account pin. Pins are case-sensitive.");
		sender.sendMessage(ChatColor.GOLD + "/pin set <pin> <confirm pin> [old pin] - Set your pin to <pin>'s value. Old pin is required if you have one set. Pins are case-sensitive.");
	}
	
}