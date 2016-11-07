package com.github.tnerevival.commands.admin;

import com.github.tnerevival.TNE;
import com.github.tnerevival.account.Account;
import com.github.tnerevival.account.IDFinder;
import com.github.tnerevival.commands.TNECommand;
import com.github.tnerevival.core.Message;
import com.github.tnerevival.core.currency.CurrencyFormatter;
import com.github.tnerevival.utils.AccountUtils;
import org.bukkit.command.CommandSender;

public class AdminBalanceCommand extends TNECommand {

  public AdminBalanceCommand(TNE plugin) {
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
    return "tne.admin.balance";
  }

  @Override
  public boolean console() {
    return true;
  }

  @SuppressWarnings("deprecation")
  @Override
  public boolean execute(CommandSender sender, String command, String[] arguments) {
    if(arguments.length == 1 || arguments.length == 2) {
      String world = (arguments.length == 2) ? arguments[1] : TNE.instance.defaultWorld;
      if(IDFinder.getID(arguments[0]) != null && TNE.instance.manager.accounts.containsKey(IDFinder.getID(arguments[0]))) {
        Account acc = AccountUtils.getAccount(IDFinder.getID(arguments[0]));
        if(acc.getBalances().containsKey(world)) {
          Message balance = new Message("Messages.Admin.Balance");
          balance.addVariable("$player", arguments[0]);
          balance.addVariable("$world", world);
          balance.addVariable("$amount", CurrencyFormatter.format(world, acc.getBalance(world)));
          balance.translate(world, sender);
          return true;
        }
        Message noBalance = new Message("Messages.Admin.NoBalance");
        noBalance.addVariable("$player", arguments[0]);
        noBalance.addVariable("$world", world);
        noBalance.translate(world, sender);
        return false;
      }
      Message noPlayer = new Message("Messages.General.NoPlayer");
      noPlayer.addVariable("$player", arguments[0]);
      noPlayer.translate(world, sender);
      return false;
    }
    help(sender);
    return false;
  }

  @Override
  public String getHelp() {
    return "/theneweconomy balance <player> [world] - Check the specified player's balance for [world]";
  }
}