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
	public boolean execute(CommandSender sender, String command, String[] arguments) {
		String ownerName = (arguments.length >= 2)? arguments[1] : sender.getName();
		Player owner = MISCUtils.getPlayer(ownerName);
		Player player = MISCUtils.getPlayer(sender.getName());


    if(arguments.length == 1) {
      if(BankUtils.hasBank(MISCUtils.getID(owner))) {
				Double value = Double.valueOf(arguments[0].replace(TNE.instance.api.getString("Core.Currency.Decimal", MISCUtils.getWorld(getPlayer(sender)), MISCUtils.getID(getPlayer(sender)).toString()), "."));
        if (BankUtils.bankMember(MISCUtils.getID(owner), MISCUtils.getID(sender.getName()))) {
          if(AccountUtils.transaction(MISCUtils.getID(owner).toString(), MISCUtils.getID(player).toString(), value, TransactionType.BANK_WITHDRAWAL, MISCUtils.getWorld(player))) {
            Message withdrawn = new Message("Messages.Bank.Withdraw");
            withdrawn.addVariable("$amount",  MISCUtils.formatBalance(player.getWorld().getName(), value));
            withdrawn.addVariable("$name",  ownerName);
            withdrawn.translate(MISCUtils.getWorld(player), player);
            return true;
          } else {
            Message overdraw = new Message("Messages.Bank.Overdraw");
            overdraw.addVariable("$amount",  MISCUtils.formatBalance(player.getWorld().getName(), value));
            overdraw.addVariable("$name",  ownerName);
            overdraw.translate(MISCUtils.getWorld(player), player);
						return false;
          }
        }
        Message noAccess = new Message("Messages.Bank.Invalid");
        noAccess.addVariable("$name", ownerName);
        noAccess.translate(MISCUtils.getWorld(player), player);
        return false;
      }
      new Message("Messages.Bank.None").translate(MISCUtils.getWorld(player), player);
      return false;
    }
    help(sender);
    return false;
	}

	@Override
	public void help(CommandSender sender) {
		sender.sendMessage(ChatColor.GOLD + "/bank withdraw <amount> [owner] - Withdraw <amount> from [owner]'s bank. Defaults to your bank.");
	}
	
}