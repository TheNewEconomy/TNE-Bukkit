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

public class ShopWhitelistCommand extends TNECommand {

	public ShopWhitelistCommand(TNE plugin) {
		super(plugin);
	}

	@Override
	public String getName() {
		return "whitelist";
	}

	@Override
	public String[] getAliases() {
		return new String[] { "wl" };
	}

	@Override
	public String getNode() {
		return "tne.shop.whitelist";
	}

	@Override
	public boolean console() {
		return false;
	}

	@Override
	public void help(CommandSender sender) {
		sender.sendMessage(ChatColor.GOLD + "/shop whitelist <name> <player> - Add/remove the specified player to the shop's whitelist.");
	}
	
	@Override
	public boolean execute(CommandSender sender, String[] arguments) {
		if(sender instanceof Player && arguments.length >= 1) {
			if(Shop.exists(arguments[0], MISCUtils.getWorld(getPlayer(sender)))) {
				if(Shop.canModify(arguments[0], (Player)sender)) {
					Shop s = Shop.getShop(arguments[0], MISCUtils.getWorld(getPlayer(sender)));
					UUID target = MISCUtils.getID(arguments[1]);
					if(s.whitelisted(MISCUtils.getID(arguments[1]))) {
						s.addWhitelist(target);
						getPlayer(sender).sendMessage(new Message("Messages.Shop.WhitelistRemoved").translate());
					} else {
						s.removeWhitelist(target);
						getPlayer(sender).sendMessage(new Message("Messages.Shop.WhitelistAdded").translate());
					}
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