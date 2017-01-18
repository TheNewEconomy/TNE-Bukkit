package com.github.tnerevival.commands.bank;

import com.github.tnerevival.TNE;
import com.github.tnerevival.account.Bank;
import com.github.tnerevival.account.IDFinder;
import com.github.tnerevival.commands.TNECommand;
import com.github.tnerevival.core.Message;
import com.github.tnerevival.core.currency.CurrencyFormatter;
import com.github.tnerevival.core.transaction.TransactionType;
import com.github.tnerevival.utils.AccountUtils;
import com.github.tnerevival.utils.BankUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

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
    UUID owner = IDFinder.getID(ownerName);
    UUID id = IDFinder.getID(sender.getName());
    String world = IDFinder.getWorld((Player)sender);


    if(arguments.length == 1) {
      if(AccountUtils.getAccount(owner).hasBank(getWorld(sender))) {
        Double value = CurrencyFormatter.translateDouble(arguments[0], IDFinder.getWorld(getPlayer(sender)));
        if (BankUtils.bankMember(owner, IDFinder.getID(sender.getName()))) {
          if(AccountUtils.transaction(owner.toString(), id.toString(), value, TransactionType.BANK_WITHDRAWAL, IDFinder.getWorld(id))) {
            Message withdrawn = new Message("Messages.Bank.Withdraw");
            withdrawn.addVariable("$amount",  CurrencyFormatter.format(world, value));
            withdrawn.addVariable("$name",  ownerName);
            withdrawn.translate(IDFinder.getWorld(id), id);
            return true;
          } else {
            Message overdraw = new Message("Messages.Bank.Overdraw");
            overdraw.addVariable("$amount",  CurrencyFormatter.format(world, value));
            overdraw.addVariable("$name",  ownerName);
            overdraw.translate(IDFinder.getWorld(id), id);
            return false;
          }
        }
        Message noAccess = new Message("Messages.Bank.Invalid");
        noAccess.addVariable("$name", ownerName);
        noAccess.translate(world, id);
        return false;
      }
      Message none = new Message("Messages.Bank.None");
      none.addVariable("$amount",  CurrencyFormatter.format(IDFinder.getWorld(id), Bank.cost(IDFinder.getWorld(id), id.toString())));
      none.translate(IDFinder.getWorld(id), id);
      return false;
    }
    help(sender);
    return false;
  }

  @Override
  public String getHelp() {
    return "/bank withdraw <amount> [owner] - Withdraw <amount> from [owner]'s bank. Defaults to your bank.";
  }
}