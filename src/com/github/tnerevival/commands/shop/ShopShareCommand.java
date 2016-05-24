package com.github.tnerevival.commands.shop;

import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.tnerevival.TNE;
import com.github.tnerevival.commands.TNECommand;
import com.github.tnerevival.core.shops.ShareEntry;
import com.github.tnerevival.core.shops.Shop;
import com.github.tnerevival.utils.MISCUtils;

public class ShopShareCommand extends TNECommand {

	public ShopShareCommand(TNE plugin) {
		super(plugin);
	}

	@Override
	public String getName() {
		return "share";
	}

	@Override
	public String[] getAliases() {
		return new String[] { "+p", "s" };
	}

	@Override
	public String getNode() {
		return "tne.shop.share";
	}

	@Override
	public boolean console() {
		return false;
	}

	@Override
	public void help(CommandSender sender) {
		sender.sendMessage(ChatColor.GOLD + "/shop share <name> <player> [percent](decimal) - Allow/disallow profit sharing with another player.");
	}
	
	@Override
	public boolean execute(CommandSender sender, String[] arguments) {
		if(sender instanceof Player && arguments.length >= 1) {
			if(Shop.exists(arguments[0])) {
				if(Shop.canModify(arguments[0], (Player)sender)) {
					Shop s = Shop.getShop(arguments[0]);
					UUID target = MISCUtils.getID(arguments[1]);
					if(!s.isAdmin()) {
						if(Shop.shares(arguments[0], target)) {
							s.removeShares(target);
							
							//TODO: Removed $player from profit sharing.
							return true;
						} else {
							//TODO: Default sharing percent configuration.
							double percent = (arguments.length >= 3)? Double.parseDouble(arguments[2]) : 0.01;
							
							if(percent <= s.canBeShared()) {
								ShareEntry entry = new ShareEntry(target, percent);
								
								s.addShares(entry);
								//TODO: Added player to shop's profit sharing.
								return true;
							} else {
								//TODO: Total shares cannot be > 100%
								return false;
							}
						}
					}
					
					//TODO: Can't profit share on admin shops.
					return false;
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