package com.github.tnerevival.commands.bank;

import com.github.tnerevival.core.transaction.TransactionType;
import com.github.tnerevival.utils.AccountUtils;
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
    String ownerName = (arguments.length >= 2)? arguments[1] : sender.getName();
    Player owner = MISCUtils.getPlayer(ownerName);
    Player player = MISCUtils.getPlayer(sender.getName());

    if(arguments.length == 1) {
      if(BankUtils.hasBank(MISCUtils.getID(owner))) {
        if (BankUtils.bankMember(MISCUtils.getID(owner), MISCUtils.getID(sender.getName()))) {
          if(AccountUtils.transaction(MISCUtils.getID(player).toString(), MISCUtils.getID(owner).toString(), Double.valueOf(arguments[0]), TransactionType.BANK_DEPOSIT, MISCUtils.getWorld(player))) {
            Message deposit = new Message("Messages.Bank.Deposit");
            deposit.addVariable("$amount",  MISCUtils.formatBalance(player.getWorld().getName(), Double.valueOf(arguments[0])));
            deposit.addVariable("$name",  ownerName);
            player.sendMessage(deposit.translate());
            return true;
          } else {
            Message insufficient = new Message("Messages.Money.Insufficient");
            insufficient.addVariable("$amount",  MISCUtils.formatBalance(player.getWorld().getName(), Double.valueOf(arguments[0])));
            insufficient.addVariable("$name",  ownerName);
            player.sendMessage(insufficient.translate());
            return false;
          }
        }
        Message noAccess = new Message("Messages.Bank.Invalid");
        noAccess.addVariable("$name", ownerName);
        player.sendMessage(noAccess.translate());
        return false;
      }
      player.sendMessage(new Message("Messages.Bank.None").translate());
      return false;
		}
		help(sender);
		return false;
	}

	@Override
	public void help(CommandSender sender) {
		sender.sendMessage(ChatColor.GOLD + "/bank deposit <amount> [owner] - Put <amount> into [owner]'s bank. Defaults to your personal bank.");
	}
	
}