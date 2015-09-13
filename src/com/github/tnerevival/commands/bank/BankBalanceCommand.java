package com.github.tnerevival.commands.bank;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.tnerevival.TNE;
import com.github.tnerevival.commands.TNECommand;
import com.github.tnerevival.core.Message;
import com.github.tnerevival.utils.BankUtils;
import com.github.tnerevival.utils.MISCUtils;

public class BankBalanceCommand extends TNECommand {
	
	public BankBalanceCommand(TNE plugin) {
		super(plugin);
	}

	@Override
	public String getName() {
		return "balance";
	}

	@Override
	public String[] getAliases() {
		return new String[0];
	}

	@Override
	public String getNode() {
		return "tne.bank.balance";
	}

	@Override
	public boolean console() {
		return false;
	}
	
	@Override
	public boolean execute(CommandSender sender, String[] arguments) {
		Player player = getPlayer(sender);
		if(BankUtils.hasBank(player.getUniqueId())) {
			Message balance = new Message("Messages.Bank.Balance");
			balance.addVariable("$amount",  MISCUtils.formatBalance(player.getWorld().getName(), BankUtils.getBankBalance(player.getUniqueId())));
			player.sendMessage(balance.translate());
			return true;
		} else {
			player.sendMessage(new Message("Messages.Bank.None").translate());
		}
		return false;
	}

	@Override
	public void help(CommandSender sender) {
		sender.sendMessage(ChatColor.GOLD + "/bank balance - find out how much gold is in your bank");
	}
	
}