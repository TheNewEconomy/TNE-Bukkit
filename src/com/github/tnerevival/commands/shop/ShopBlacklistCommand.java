package com.github.tnerevival.commands.shop;

import com.github.tnerevival.TNE;
import com.github.tnerevival.commands.TNECommand;
import com.github.tnerevival.core.Message;
import com.github.tnerevival.core.shops.Shop;
import com.github.tnerevival.utils.MISCUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

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
	public String getHelp() {
    return "/shop blacklist <shop> <player> - Add/remove the specified player to the shop's blacklist.";
	}
	
	@Override
	public boolean execute(CommandSender sender, String command, String[] arguments) {
		if(sender instanceof Player && arguments.length >= 1) {
			Player player = getPlayer(sender);
			if(Shop.exists(arguments[0], MISCUtils.getWorld(getPlayer(sender)))) {
				if(Shop.canModify(arguments[0], (Player)sender)) {
					Shop s = Shop.getShop(arguments[0], MISCUtils.getWorld(getPlayer(sender)));
					UUID target = MISCUtils.getID(arguments[1]);
					if(s.blacklisted(MISCUtils.getID(arguments[1]))) {
						s.addBlacklist(target);
						new Message("Messages.Shop.BlacklistRemoved").translate(MISCUtils.getWorld(player), player);
					} else {
						s.removeBlacklist(target);
						new Message("Messages.Shop.BlacklistAdded").translate(MISCUtils.getWorld(player), player);
					}
					return true;
				}
				new Message("Messages.Shop.Permission").translate(MISCUtils.getWorld(player), player);
				return false;
			}
			new Message("Messages.Shop.None").translate(MISCUtils.getWorld(player), player);
			return false;
		} else {
			help(sender);
		}
		return false;
	}
}