package com.github.tnerevival.commands.shop;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.tnerevival.TNE;
import com.github.tnerevival.commands.TNECommand;
import com.github.tnerevival.core.Message;
import com.github.tnerevival.utils.AccountUtils;

public class ShopCommand extends TNECommand {
	
	public ShopCommand(TNE plugin) {
		super(plugin);
	}
	
	@Override
	public String getName() {
		return "shop";
	}
	
	@Override
	public String[] getAliases() {
		return new String[] { "s" };
	}
	
	@Override
	public String getNode() {
		return "tne.shop.command";
	}
	
	@Override
	public boolean console() {
		return true;
	}
	
	@Override
	public boolean execute(CommandSender sender, String[] arguments) {
		
		if(sender instanceof Player) {
			Player player = getPlayer(sender);
			
			if(AccountUtils.commandLocked(player)) {
				return false;
			}
		}
		
		if(arguments.length == 0 || arguments.length == 1 && arguments[0].equalsIgnoreCase("help")) {
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
		sender.sendMessage(ChatColor.GOLD + "~~~~~Shop Commands~~~~~");
		sender.sendMessage(ChatColor.GOLD + "/shop help - Shows general shop help.");
		sender.sendMessage(ChatColor.GOLD + "/shop list - Shows a list of shops you own.");
		sender.sendMessage(ChatColor.GOLD + "/shop close <name> - Close the specified shop.");
		sender.sendMessage(ChatColor.GOLD + "/shop remove <name> - Remove a specific item from your shop");
		sender.sendMessage(ChatColor.GOLD + "/shop add <name> [gold:amount] [trade:block/item name:amount(default 1)]  - Add a new item to your shop for [cost] and/or [trade].");
	}
}