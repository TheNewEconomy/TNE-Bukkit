package com.github.tnerevival.commands.bank;

import com.github.tnerevival.TNE;
import com.github.tnerevival.account.Account;
import com.github.tnerevival.account.Bank;
import com.github.tnerevival.account.IDFinder;
import com.github.tnerevival.commands.TNECommand;
import com.github.tnerevival.core.Message;
import com.github.tnerevival.core.currency.CurrencyFormatter;
import com.github.tnerevival.utils.AccountUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

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
  public boolean execute(CommandSender sender, String command, String[] arguments) {
    Player player = getPlayer(sender);
    String world = (arguments.length >= 1)? arguments[0] : getWorld(sender);
    String owner = (arguments.length >= 2)? arguments[1] : player.getName();
    String currency = (arguments.length >= 3)? getCurrency(world, arguments[2]).getName() : plugin.manager.currencyManager.get(world).getName();
    Account account = AccountUtils.getAccount(IDFinder.getID(owner));
    UUID id = IDFinder.getID(player);

    if(IDFinder.getID(owner) == null) {
      Message notFound = new Message("Messages.General.NoPlayer");
      notFound.addVariable("$player", owner);
      notFound.translate(IDFinder.getWorld(player), player);
      return false;
    }

    if(!account.hasBank(world) && !owner.equals(player.getName())) {
      Message none = new Message("Messages.Bank.None");
      none.addVariable("$amount",  CurrencyFormatter.format(getWorld(sender), Bank.cost(getWorld(sender), IDFinder.getID(player).toString())));
      none.translate(getWorld(sender), player);
      return false;
    }

    if(!AccountUtils.getAccount(IDFinder.getID(owner)).hasBank(world) || !Bank.bankMember(IDFinder.getID(owner), IDFinder.getID(sender.getName())) || !world.equals(getWorld(sender)) && !TNE.instance.api.getBoolean("Core.Bank.MultiManage")) {
      new Message("Messages.General.NoPerm").translate(getWorld(player), player);
      return false;
    }
    Message balance = new Message("Messages.Bank.Balance");
    balance.addVariable("$amount",  CurrencyFormatter.format(world, currency, Bank.getBankBalance(IDFinder.getID(owner), world, currency)));
    balance.addVariable("$name", owner);
    balance.translate(IDFinder.getWorld(player), player);
    return true;
  }

  @Override
  public String getHelp() {
    return "/bank balance [world] [owner] [currency] - Find out how much gold is in a specific bank. Defaults to your personal bank.";
  }
}