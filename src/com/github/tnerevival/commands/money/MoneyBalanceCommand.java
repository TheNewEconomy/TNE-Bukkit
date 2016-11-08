package com.github.tnerevival.commands.money;

import com.github.tnerevival.TNE;
import com.github.tnerevival.account.IDFinder;
import com.github.tnerevival.commands.TNECommand;
import com.github.tnerevival.core.Message;
import com.github.tnerevival.core.currency.Currency;
import com.github.tnerevival.core.currency.CurrencyFormatter;
import com.github.tnerevival.utils.AccountUtils;
import com.github.tnerevival.utils.MISCUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MoneyBalanceCommand extends TNECommand {

  public MoneyBalanceCommand(TNE plugin) {
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
    return "tne.money.balance";
  }

  @Override
  public boolean console() {
    return false;
  }

  @Override
  public boolean execute(CommandSender sender, String command, String[] arguments) {
    Player player = getPlayer(sender);
    String world = (arguments.length >= 1)? getWorld(sender, arguments[0]) : getWorld(sender);
    String currencyName = (arguments.length >= 2)? arguments[1] : "Default";
    Currency currency = getCurrency(world, currencyName);

    MISCUtils.debug(AccountUtils.getAccount(IDFinder.getID(player)).getBalances().toString());

    if(!TNE.instance.manager.currencyManager.contains(world, currencyName)) {
      Message m = new Message("Messages.Money.NoCurrency");
      m.addVariable("$currency", currencyName);
      m.addVariable("$world", world);
      m.translate(world, sender);
      return false;
    }

    Message balance = new Message("Messages.Money.Balance");
    balance.addVariable("$amount",  CurrencyFormatter.format(currency, plugin.api.getBalance(player, world, currency)));
    balance.translate(world, player);
    return true;
  }

  @Override
  public String getHelp() {
    return "/money balance [world] [currency] - find out how much money you have on you";
  }
}