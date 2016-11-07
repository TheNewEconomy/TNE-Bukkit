package com.github.tnerevival.commands.bank;

import com.github.tnerevival.TNE;
import com.github.tnerevival.account.IDFinder;
import com.github.tnerevival.commands.TNECommand;
import com.github.tnerevival.core.Message;
import com.github.tnerevival.core.currency.CurrencyFormatter;
import com.github.tnerevival.core.transaction.TransactionType;
import com.github.tnerevival.utils.AccountUtils;
import com.github.tnerevival.utils.BankUtils;
import com.github.tnerevival.utils.MISCUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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
  public boolean execute(CommandSender sender, String command, String[] arguments) {
    String ownerName = (arguments.length >= 2)? arguments[1] : sender.getName();
    Player owner = MISCUtils.getPlayer(ownerName);
    Player player = MISCUtils.getPlayer(sender.getName());

    if(arguments.length == 1) {
      if(BankUtils.hasBank(IDFinder.getID(owner))) {
        Double value = CurrencyFormatter.translateDouble(arguments[0], MISCUtils.getWorld(getPlayer(sender)));
        if (BankUtils.bankMember(IDFinder.getID(owner), IDFinder.getID(sender.getName()))) {
          if(AccountUtils.transaction(IDFinder.getID(player).toString(), IDFinder.getID(owner).toString(), value, TransactionType.BANK_DEPOSIT, MISCUtils.getWorld(player))) {
            Message deposit = new Message("Messages.Bank.Deposit");
            deposit.addVariable("$amount",  CurrencyFormatter.format(player.getWorld().getName(), value));
            deposit.addVariable("$name",  ownerName);
            deposit.translate(MISCUtils.getWorld(player), player);
            return true;
          } else {
            Message insufficient = new Message("Messages.Money.Insufficient");
            insufficient.addVariable("$amount",  CurrencyFormatter.format(player.getWorld().getName(), value));
            insufficient.addVariable("$name",  ownerName);
            insufficient.translate(MISCUtils.getWorld(player), player);
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
  public String getHelp() {
    return "/bank deposit <amount> [owner] - Put <amount> into [owner]'s bank. Defaults to your personal bank.";
  }

}