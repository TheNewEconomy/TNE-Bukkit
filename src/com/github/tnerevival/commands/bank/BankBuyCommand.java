package com.github.tnerevival.commands.bank;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.tnerevival.TNE;
import com.github.tnerevival.account.Bank;
import com.github.tnerevival.commands.TNECommand;
import com.github.tnerevival.utils.AccountUtils;
import com.github.tnerevival.utils.BankUtils;
import com.github.tnerevival.utils.MISCUtils;

public class BankBuyCommand extends TNECommand {
	
	public BankBuyCommand(TNE plugin) {
		super(plugin);
	}

	@Override
	public String getName() {
		return "buy";
	}

	@Override
	public String[] getAliases() {
		return new String[0];
	}

	@Override
	public String getNode() {
		return "tne.bank.buy";
	}

	@Override
	public boolean console() {
		return false;
	}
	
	@Override
	public boolean execute(CommandSender sender, String[] arguments) {
		Player player = getPlayer(sender);
		if(BankUtils.hasBank(player.getUniqueId())) {
			player.sendMessage(ChatColor.RED + "You already own a bank!");
			return false;
		}
		
		if(!player.hasPermission("tne.bank.bypass")) {
			if(AccountUtils.hasFunds(player.getUniqueId(), BankUtils.cost(player.getWorld().getName()))) {
				AccountUtils.removeFunds(player.getUniqueId(), BankUtils.cost(player.getWorld().getName()));
			} else {
				player.sendMessage(ChatColor.DARK_RED + "I'm sorry, but you need at least " + ChatColor.GOLD + MISCUtils.formatBalance(player.getWorld().getName(), BankUtils.cost(player.getWorld().getName())) + ChatColor.DARK_RED + " to create a bank.");
				return false;
			}
		}
		Bank bank = new Bank(player, BankUtils.size(player.getWorld().getName()));
		AccountUtils.getAccount(player.getUniqueId()).getBanks().put(player.getWorld().getName(), bank);
		player.sendMessage(ChatColor.WHITE + "Congratulations! You have successfully purchased a bank!");
		return true;
	}

	@Override
	public void help(CommandSender sender) {
		sender.sendMessage(ChatColor.GOLD + "/bank buy - buy yourself a bank");
	}
	
}