package com.github.tnerevival.commands.bank;

import com.github.tnerevival.TNE;
import com.github.tnerevival.account.Bank;
import com.github.tnerevival.commands.TNECommand;
import com.github.tnerevival.core.Message;
import com.github.tnerevival.core.transaction.TransactionType;
import com.github.tnerevival.utils.AccountUtils;
import com.github.tnerevival.utils.BankUtils;
import com.github.tnerevival.utils.MISCUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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
	public boolean execute(CommandSender sender, String command, String[] arguments) {
		Player player = getPlayer(sender);
		if(BankUtils.hasBank(MISCUtils.getID(player))) {
			new Message("Messages.Bank.Already").translate(MISCUtils.getWorld(player), player);
			return false;
		}
		
		if(!player.hasPermission("tne.bank.bypass")) {
			if(AccountUtils.transaction(MISCUtils.getID(player).toString(), null, BankUtils.cost(MISCUtils.getWorld(player), MISCUtils.getID(player).toString()), TransactionType.MONEY_INQUIRY, MISCUtils.getWorld(player))) {
				AccountUtils.transaction(MISCUtils.getID(player).toString(), null, BankUtils.cost(MISCUtils.getWorld(player), MISCUtils.getID(player).toString()), TransactionType.MONEY_REMOVE, MISCUtils.getWorld(player));
			} else {
				Message insufficient = new Message("Messages.Money.Insufficient");
				insufficient.addVariable("$amount",  MISCUtils.formatBalance(MISCUtils.getWorld(player), BankUtils.cost(player.getWorld().getName(), MISCUtils.getID(player).toString())));
				insufficient.translate(MISCUtils.getWorld(player), player);
				return false;
			}
		}
		Bank bank = new Bank(MISCUtils.getID(player), BankUtils.size(player.getWorld().getName(), MISCUtils.getID(player).toString()));
		AccountUtils.getAccount(MISCUtils.getID(player)).getBanks().put(MISCUtils.getWorld(player), bank);
		new Message("Messages.Bank.Bought").translate(MISCUtils.getWorld(player), player);
		return true;
	}

	@Override
	public String getHelp() {
		return "/bank buy - buy yourself a bank";
	}
	
}