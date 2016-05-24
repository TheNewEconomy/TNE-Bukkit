package com.github.tnerevival.commands.shop;

import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.tnerevival.TNE;
import com.github.tnerevival.commands.TNECommand;
import com.github.tnerevival.core.shops.Shop;
import com.github.tnerevival.utils.MISCUtils;

public class ShopBlacklistCommand extends TNECommand {

	public ShopBlacklistCommand(TNE plugin) {
		super(plugin);
	}

	@Override
	public String getName() {
		return "blacklist";
	}

	@Override
	public String[] getAliases() {
		return new String[] { "bl" };
	}

	@Override
	public String getNode() {
		return "tne.shop.blacklist";
	}

	@Override
	public boolean console() {
		return false;
	}

	@Override
	public void help(CommandSender sender) {
		sender.sendMessage(ChatColor.GOLD + "/shop blacklist <shop> <player> - Add/remove the specified player to the shop's blacklist.");
	}
	
	@Override
	public boolean execute(CommandSender sender, String[] arguments) {
		if(sender instanceof Player && arguments.length >= 1) {
			if(Shop.exists(arguments[0])) {
				if(Shop.canModify(arguments[0], (Player)sender)) {
					Shop s = Shop.getShop(arguments[0]);
					UUID target = MISCUtils.getID(arguments[1]);
					if(s.blacklisted(MISCUtils.getID(arguments[1]))) {
						s.addBlacklist(target);
						//TODO: Player has been removed from the shop's blacklist.
					} else {
						s.removeBlacklist(target);
						//TODO: Player has been added to the shop's blacklist.
					}
					return true;
				}
				//TODO: Must be shop owner to do that.
				return false;
			}
			//TODO: Shop doesn't exist message.
			return false;
		} else {
			help(sender);
		}
		return false;
	}
}