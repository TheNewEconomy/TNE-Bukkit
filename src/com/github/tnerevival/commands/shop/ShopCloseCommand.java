package com.github.tnerevival.commands.shop;

import com.github.tnerevival.TNE;
import com.github.tnerevival.commands.TNECommand;
import com.github.tnerevival.core.Message;
import com.github.tnerevival.core.shops.Shop;
import com.github.tnerevival.utils.MISCUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class ShopCloseCommand extends TNECommand {

	public ShopCloseCommand(TNE plugin) {
		super(plugin);
	}

	@Override
	public String getName() {
		return "close";
	}

	@Override
	public String[] getAliases() {
		return new String[] { "c" };
	}

	@Override
	public String getNode() {
		return "tne.shop.close";
	}

	@Override
	public boolean console() {
		return false;
	}

	@Override
	public void help(CommandSender sender) {
		sender.sendMessage(ChatColor.GOLD + "/shop close <name> - Close the specified shop.");
	}
	
	@Override
	public boolean execute(CommandSender sender, String[] arguments) {
		if(sender instanceof Player && arguments.length >= 1) {
			if(Shop.exists(arguments[0], MISCUtils.getWorld(getPlayer(sender)))) {
				if(Shop.canModify(arguments[0], (Player)sender)) {
					Shop s = Shop.getShop(arguments[0], MISCUtils.getWorld(getPlayer(sender)));
					
					for(UUID shopper : s.getShoppers()) {
						Player p = MISCUtils.getPlayer(shopper);
						p.closeInventory();
						getPlayer(sender).sendMessage(new Message("Messages.Shop.ClosedBrowse").translate());
					}
					s.getShoppers().clear();
					
					TNE.instance.manager.shops.remove(arguments[0]);
					Message hidden = new Message("Messages.Shop.Closed");
					hidden.addVariable("$shop", s.getName());
					getPlayer(sender).sendMessage(hidden.translate());
					return true;
				}
				getPlayer(sender).sendMessage(new Message("Messages.Shop.Permission").translate());
				return false;
			}
			getPlayer(sender).sendMessage(new Message("Messages.Shop.None").translate());
			return false;
		} else {
			help(sender);
		}
		return false;
	}
}