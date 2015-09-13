package com.github.tnerevival.commands.bank;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.tnerevival.TNE;
import com.github.tnerevival.commands.TNECommand;
import com.github.tnerevival.core.Message;
import com.github.tnerevival.utils.BankUtils;
import com.github.tnerevival.utils.MISCUtils;

public class BankWithdrawCommand extends TNECommand {
	
	public BankWithdrawCommand(TNE plugin) {
		super(plugin);
	}

	@Override
	public String getName() {
		return "withdraw";
	}

	@Override
	public String[] getAliases() {
		return new String[0];
	}

	@Override
	public String getNode() {
		return "tne.bank.withdraw";
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
				if(BankUtils.bankWithdraw(player.getUniqueId(), Double.valueOf(arguments[0]))) {
					Message withdrawn = new Message("Messages.Bank.Withdraw");
					withdrawn.addVariable("$amount",  MISCUtils.formatBalance(player.getWorld().getName(), Double.valueOf(arguments[0])));
					player.sendMessage(withdrawn.translate());
					return true;
				} else {
					Message overdraw = new Message("Messages.Bank.Overdraw");
					overdraw.addVariable("$amount",  MISCUtils.formatBalance(player.getWorld().getName(), Double.valueOf(arguments[0])));
					player.sendMessage(overdraw.translate());
				}
			} else {
				player.sendMessage(new Message("Messages.Bank.None").translate());
			}
		} else {
			help(sender);
		}
		return false;
	}

	@Override
	public void help(CommandSender sender) {
		sender.sendMessage(ChatColor.GOLD + "/bank withdraw <amount> - withdraw the specified amout of money from your bank");
	}
	
}