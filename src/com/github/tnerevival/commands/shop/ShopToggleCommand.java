package com.github.tnerevival.commands.shop;

import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.tnerevival.TNE;
import com.github.tnerevival.commands.TNECommand;
import com.github.tnerevival.core.shops.Shop;
import com.github.tnerevival.utils.MISCUtils;

public class ShopToggleCommand extends TNECommand {

	public ShopToggleCommand(TNE plugin) {
		super(plugin);
	}

	@Override
	public String getName() {
		return "toggle";
	}

	@Override
	public String[] getAliases() {
		return new String[] { "t" };
	}

	@Override
	public String getNode() {
		return "tne.shop.toggle";
	}

	@Override
	public boolean console() {
		return false;
	}

	@Override
	public void help(CommandSender sender) {
		sender.sendMessage(ChatColor.GOLD + "/shop toggle <name> - Toggle this shop's visibility. Only whitelisted players can buy from hidden shops.");
	}
	
	@Override
	public boolean execute(CommandSender sender, String[] arguments) {
		if(sender instanceof Player && arguments.length >= 1) {
			if(Shop.exists(arguments[0])) {
				if(Shop.canModify(arguments[0], (Player)sender)) {
					Shop s = Shop.getShop(arguments[0]);
					if(s.isHidden()) {
						s.setHidden(false);
						//TODO: Shop is no longer hidden.
					} else {
						s.setHidden(true);
						
						for(UUID shopper : s.getShoppers()) {
							if(!s.whitelisted(shopper)) {
								Player p = MISCUtils.getPlayer(shopper);
								p.closeInventory();
								//TODO: Shop has been hidden message.
							}
						}
						//TODO: Shop is now hidden.
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