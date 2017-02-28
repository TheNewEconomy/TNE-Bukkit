package com.github.tnerevival.commands.admin;

import com.github.tnerevival.TNE;
import com.github.tnerevival.account.Account;
import com.github.tnerevival.account.IDFinder;
import com.github.tnerevival.commands.TNECommand;
import com.github.tnerevival.core.Message;
import com.github.tnerevival.core.currency.Currency;
import com.github.tnerevival.core.currency.CurrencyFormatter;
import com.github.tnerevival.utils.AccountUtils;
import com.github.tnerevival.utils.MISCUtils;
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
    if(arguments.length >= 1 && arguments.length <= 3) {
      String world = (arguments.length >= 2)? getWorld(sender, arguments[1]) : getWorld(sender);
      String currencyName = (arguments.length >= 3)? arguments[2] : TNE.instance().manager.currencyManager.get(world).getName();
      Currency currency = getCurrency(world, currencyName);
      MISCUtils.debug(TNE.instance().manager.accounts.containsKey(IDFinder.getID(arguments[0])) + "");
      MISCUtils.debug("RETURNED ID: " + IDFinder.getID(arguments[0]).toString() + " for User: " + arguments[0]);
      if(IDFinder.getID(arguments[0]) != null && TNE.instance().manager.accounts.containsKey(IDFinder.getID(arguments[0]))) {
        Account acc = AccountUtils.getAccount(IDFinder.getID(arguments[0]));
        if(acc.getBalances().containsKey(world + ":" + currency.getName())) {
          MISCUtils.debug("Has currency and balance.");
          Message balance = new Message("Messages.Admin.Balance");
          MISCUtils.debug("Passed message check");
          balance.addVariable("$player", arguments[0]);
          MISCUtils.debug("Passed player check");
          balance.addVariable("$world", world);
          MISCUtils.debug("Passed world check");
          balance.addVariable("$amount", CurrencyFormatter.format(world, plugin.api().getBalanceDecimal(arguments[0], world, currency)));
          MISCUtils.debug("Passed amount check");
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
    return "/theneweconomy balance <player> [world] [currency] - Check the specified player's balance for [world]";
  }
}