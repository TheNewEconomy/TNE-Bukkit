package com.github.tnerevival.commands.bank;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.tnerevival.TNE;
import com.github.tnerevival.commands.TNECommand;
import com.github.tnerevival.utils.BankUtils;
import com.github.tnerevival.utils.MISCUtils;

public class BankDepositCommand extends TNECommand {
	
	public BankDepositCommand(TNE plugin) {
		super(plugin);
	}

	@Override
	public String getName() {
		return "deposit";
	}

	@Override
	public String[] getAliases() {
		return new String[0];
	}

	@Override
	public String getNode() {
		return "tne.bank.deposit";
	}

	@Override
	public boolean console() {
		return false;
	}
	
	@Override
	public boolean execute(CommandSender sender, String[] arguments) {
		Player player = getPlayer(sender);
		if(arguments.length == 1) {
			if(BankUtils.hasBank(player.getUniqueId())) {
				if(BankUtils.bankDeposit(player.getUniqueId(), Double.valueOf(arguments[0]))) {
					player.sendMessage(ChatColor.WHITE + "You have deposited " + ChatColor.GOLD + MISCUtils.formatBalance(player.getWorld().getName(), Double.valueOf(arguments[0])) + ChatColor.WHITE + " into your bank.");
					return true;
				} else {
					player.sendMessage(ChatColor.DARK_RED + "I'm sorry, but you do not have " + ChatColor.GOLD + MISCUtils.formatBalance(player.getWorld().getName(), Double.valueOf(arguments[0])) + ChatColor.DARK_RED + ".");
				}
			} else {
				player.sendMessage(ChatColor.DARK_RED + "I'm sorry, but you do not own a bank. Please try /bank buy to buy one.");
			}
		} else {
			help(sender);
		}
		return false;
	}

	@Override
	public void help(CommandSender sender) {
		sender.sendMessage(ChatColor.GOLD + "/bank deposit <amount> - put the specified amount of money in your bank");
	}
	
}