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
		sender.sendMessage(ChatColor.GOLD + "/shop whitelist <name> <player> - Add/remove the specified player to the shop's whitelist.");
		sender.sendMessage(ChatColor.GOLD + "/shop blacklist <name> <player> - Add/remove the specified player to the shop's blacklist.");
		sender.sendMessage(ChatColor.GOLD + "/shop toggle <name> - Toggle this shop's visibility. Only whitelisted players can buy from hidden shops.");
		sender.sendMessage(ChatColor.GOLD + "/shop create <name> [admin] [hidden] - Create a new shop. [admin] yes/no, [hidden] yes/no");
		sender.sendMessage(ChatColor.GOLD + "/shop close <name> - Close the specified shop.");
		sender.sendMessage(ChatColor.GOLD + "/shop share <name> <player> [percent](decimal) - Allow/disallow profit sharing with another player.");
		sender.sendMessage(ChatColor.GOLD + "/shop remove <name> <amount> [item] [cost(gold:amount or trade:name:amount)] - Remove a specific item from your shop. Cost is required if multiple entries exist.");
		sender.sendMessage(ChatColor.GOLD + "/shop add <name> <amount> [item] [gold:amount] [trade:block/item name:amount(default 1)]  - Add a new item to your shop for [cost] and/or [trade]. Leave out item name to use currently held item.");
	}
}