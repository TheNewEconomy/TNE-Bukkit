package com.github.tnerevival.commands.bank;

import com.github.tnerevival.TNE;
import com.github.tnerevival.account.Account;
import com.github.tnerevival.account.Bank;
import com.github.tnerevival.account.IDFinder;
import com.github.tnerevival.commands.TNECommand;
import com.github.tnerevival.core.Message;
import com.github.tnerevival.core.currency.Currency;
import com.github.tnerevival.core.currency.CurrencyFormatter;
import com.github.tnerevival.core.transaction.TransactionType;
import com.github.tnerevival.utils.AccountUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.math.BigDecimal;

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
    Player player = getPlayer(sender);
    String world = (arguments.length >= 2)? arguments[1] : getWorld(sender);
    String owner = (arguments.length >= 3)? arguments[2] : player.getName();
    String currencyName = (arguments.length >= 4)? getCurrency(world, arguments[3]).getName() : plugin.manager.currencyManager.get(world).getName();

    if(!TNE.instance().manager.currencyManager.contains(world, currencyName)) {
      Message m = new Message("Messages.Money.NoCurrency");
      m.addVariable("$currency", currencyName);
      m.addVariable("$world", world);
      m.translate(world, sender);
      return false;
    }

    Currency currency = getCurrency(world, currencyName);
    Account account = AccountUtils.getAccount(IDFinder.getID(owner));

    if(arguments.length < 1) {
      help(sender);
      return false;
    }

    if(IDFinder.getID(owner) == null) {
      Message notFound = new Message("Messages.General.NoPlayer");
      notFound.addVariable("$player", owner);
      notFound.translate(IDFinder.getWorld(player), player);
      return false;
    }

    if(!account.hasBank(world) && !owner.equals(player.getName())) {
      Message none = new Message("Messages.Bank.None");
      none.addVariable("$amount",  CurrencyFormatter.format(world, Bank.cost(getWorld(sender), IDFinder.getID(player).toString())));
      none.translate(getWorld(sender), player);
      return false;
    }
    if(!AccountUtils.getAccount(IDFinder.getID(owner)).hasBank(world) || !Bank.bankMember(IDFinder.getID(owner), IDFinder.getID(sender.getName())) || !world.equals(getWorld(sender)) && !TNE.instance().api().getBoolean("Core.Bank.MultiManage")) {
      new Message("Messages.General.NoPerm").translate(getWorld(player), player);
      return false;
    }

    String parsed = CurrencyFormatter.parseAmount(currency, world, arguments[0]);
    if(parsed.contains("Messages")) {
      Message max = new Message(parsed);
      max.addVariable("$currency", currency.getName());
      max.addVariable("$world", world);
      max.addVariable("$player", getPlayer(sender).getDisplayName());
      max.translate(getWorld(sender), sender);
      return false;
    }

    BigDecimal value = new BigDecimal(parsed);
    if(value.compareTo(BigDecimal.ZERO) < 0) {
      new Message("Messages.Money.Negative").translate(world, sender);
      return false;
    }

    BigDecimal comparison = Bank.getBankBalance(IDFinder.getID(owner), world, currencyName);
    if(comparison.add(value).compareTo(currency.getMaxBalance()) > 0) {
      Message exceeds = new Message("Messages.Money.ExceedsBankMaximum");
      exceeds.addVariable("$max", CurrencyFormatter.format(world, currencyName, currency.getMaxBalance()));
      exceeds.addVariable("$owner", owner);
      exceeds.translate(world, sender);
      return false;
    }

    if(!AccountUtils.transaction(IDFinder.getID(player).toString(), IDFinder.getID(owner).toString(), value, plugin.manager.currencyManager.get(world, currencyName), TransactionType.BANK_DEPOSIT, IDFinder.getWorld(player))) {
      Message insufficient = new Message("Messages.Money.Insufficient");
      insufficient.addVariable("$amount",  CurrencyFormatter.format(world, currencyName, value));
      insufficient.addVariable("$name",  owner);
      insufficient.translate(IDFinder.getWorld(player), player);
      return false;
    }

    Message deposit = new Message("Messages.Bank.Deposit");
    deposit.addVariable("$amount",  CurrencyFormatter.format(world, currencyName, value));
    deposit.addVariable("$name",  owner);
    deposit.translate(IDFinder.getWorld(player), player);
    return true;
  }

  @Override
  public String getHelp() {
    return "/bank deposit <amount> [world] [owner] [currency] - Put <amount> into [owner]'s bank for world [world]. Defaults to your personal bank.";
  }
}