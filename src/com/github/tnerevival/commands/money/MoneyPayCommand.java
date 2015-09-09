package com.github.tnerevival.commands.money;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.tnerevival.TNE;
import com.github.tnerevival.commands.TNECommand;
import com.github.tnerevival.utils.AccountUtils;
import com.github.tnerevival.utils.MISCUtils;

public class MoneyPayCommand extends TNECommand {

	public MoneyPayCommand(TNE plugin) {
		super(plugin);
	}

	@Override
	public String getName() {
		return "pay";
	}

	@Override
	public String[] getAliases() {
		return new String[0];
	}

	@Override
	public String getNode() {
		return "tne.money.pay";
	}

	@Override
	public boolean console() {
		return false;
	}
	
	@Override
	public boolean execute(CommandSender sender, String[] arguments) {
		Player player = getPlayer(sender);
		if(arguments.length == 2) {
			if(AccountUtils.hasFunds(player.getUniqueId(), Double.valueOf(arguments[1]))) {
				if(getPlayer(sender, arguments[0]) != null && AccountUtils.payMoney(player.getUniqueId(), getPlayer(sender, arguments[0]).getUniqueId(), Double.valueOf(arguments[1]))) {
					player.sendMessage(ChatColor.WHITE + "Successfully paid " + arguments[0] + " " + ChatColor.GOLD + MISCUtils.formatBalance(player.getWorld().getName(), Double.valueOf(arguments[1])) + ChatColor.WHITE + ".");
					return true;
				}
			} else {
				player.sendMessage(ChatColor.DARK_RED + "I'm sorry, but you do not have " + ChatColor.GOLD + MISCUtils.formatBalance(player.getWorld().getName(), Double.valueOf(arguments[1])) + ChatColor.WHITE + ".");
			}
		} else {
			help(sender);
		}
		return false;
	}

	@Override
	public void help(CommandSender sender) {
		sender.sendMessage(ChatColor.GOLD + "/money pay <player> <amount> - pay a player money from your balance");
	}
	
}