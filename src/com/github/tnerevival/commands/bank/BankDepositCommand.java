package com.github.tnerevival.commands.bank;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.tnerevival.TNE;
import com.github.tnerevival.commands.TNECommand;
import com.github.tnerevival.core.Message;
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
			if(BankUtils.hasBank(MISCUtils.getID(player))) {
				if(BankUtils.bankDeposit(MISCUtils.getID(player), Double.valueOf(arguments[0]))) {
					Message deposit = new Message("Messages.Bank.Deposit");
					deposit.addVariable("$amount",  MISCUtils.formatBalance(player.getWorld().getName(), Double.valueOf(arguments[0])));
					player.sendMessage(deposit.translate());
					return true;
				} else {
					Message insufficient = new Message("Messages.Money.Insufficient");
					insufficient.addVariable("$amount",  MISCUtils.formatBalance(player.getWorld().getName(), Double.valueOf(arguments[0])));
					player.sendMessage(insufficient.translate());
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
		sender.sendMessage(ChatColor.GOLD + "/bank deposit <amount> - put the specified amount of money in your bank");
	}
	
}