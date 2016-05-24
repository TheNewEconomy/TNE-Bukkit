package com.github.tnerevival.commands.shop;

import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.tnerevival.TNE;
import com.github.tnerevival.commands.TNECommand;
import com.github.tnerevival.core.shops.Shop;
import com.github.tnerevival.utils.MISCUtils;

public class ShopCreateCommand extends TNECommand {

	public ShopCreateCommand(TNE plugin) {
		super(plugin);
	}

	@Override
	public String getName() {
		return "create";
	}

	@Override
	public String[] getAliases() {
		return new String[] { "+" };
	}

	@Override
	public String getNode() {
		return "tne.shop.create";
	}

	@Override
	public boolean console() {
		return true;
	}

	@Override
	public void help(CommandSender sender) {
		sender.sendMessage(ChatColor.GOLD + "/shop create <name> [admin] [hidden] - Create a new shop. [admin] true/false, [hidden] true/false");
	}
	
	@Override
	public boolean execute(CommandSender sender, String[] arguments) {
		if(arguments.length >= 1) {
			if(!Shop.exists(arguments[0])) {
				UUID owner = null;
				if(sender instanceof Player) {
					owner = MISCUtils.getID((Player)sender);
				}
				
				if(arguments.length >= 2 && arguments[1].equalsIgnoreCase("true")) {
					owner = null;
				}
				
				Shop s = new Shop(owner);
				
				if(arguments.length >= 3 && arguments[2].equalsIgnoreCase("true")) {
					s.setHidden(true);
				}
				TNE.instance.manager.shops.put(s.getName(), s);
				//TODO: Shop created message.
				return true;
			}
			//TODO: Shop with that name already exists message.
			return false;
		} else {
			help(sender);
		}
		return false;
	}

}