package com.github.tnerevival.commands.bank;

import com.github.tnerevival.TNE;
import com.github.tnerevival.account.Account;
import com.github.tnerevival.account.Bank;
import com.github.tnerevival.account.IDFinder;
import com.github.tnerevival.commands.TNECommand;
import com.github.tnerevival.core.Message;
import com.github.tnerevival.core.currency.CurrencyFormatter;
import com.github.tnerevival.core.transaction.TransactionType;
import com.github.tnerevival.utils.AccountUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.math.BigDecimal;
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
    Player player = getPlayer(sender);
    String world = (arguments.length >= 2)? arguments[1] : getWorld(sender);
    String owner = (arguments.length >= 3)? arguments[2] : player.getName();
    String currency = (arguments.length >= 4)? getCurrency(world, arguments[3]).getName() : plugin.manager.currencyManager.get(world).getName();
    Account account = AccountUtils.getAccount(IDFinder.getID(owner));
    UUID id = IDFinder.getID(player);

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
      none.addVariable("$amount",  CurrencyFormatter.format(getWorld(sender), Bank.cost(getWorld(sender), IDFinder.getID(player).toString())));
      none.translate(getWorld(sender), player);
      return false;
    }

    if(!AccountUtils.getAccount(IDFinder.getID(owner)).hasBank(world) || !Bank.bankMember(IDFinder.getID(owner), IDFinder.getID(sender.getName())) || !world.equals(getWorld(sender)) && !TNE.instance().api().getBoolean("Core.Bank.MultiManage")) {
      new Message("Messages.General.NoPerm").translate(getWorld(player), player);
      return false;
    }

    BigDecimal value = CurrencyFormatter.translateBigDecimal(arguments[0], IDFinder.getWorld(getPlayer(sender)));
    if(!AccountUtils.transaction(IDFinder.getID(owner).toString(), id.toString(), value, plugin.manager.currencyManager.get(world, currency), TransactionType.BANK_WITHDRAWAL, IDFinder.getWorld(id))) {
      Message overdraw = new Message("Messages.Bank.Overdraw");
      overdraw.addVariable("$amount",  CurrencyFormatter.format(world, currency, value));
      overdraw.addVariable("$name",  owner);
      overdraw.translate(IDFinder.getWorld(id), id);
      return false;
    }
    Message withdrawn = new Message("Messages.Bank.Withdraw");
    withdrawn.addVariable("$amount",  CurrencyFormatter.format(world, currency, value));
    withdrawn.addVariable("$name",  owner);
    withdrawn.translate(IDFinder.getWorld(id), id);
    return true;
  }

  @Override
  public String getHelp() {
    return "/bank withdraw <amount> [world] [owner] [currency] - Withdraw <amount> from [owner]'s bank for world [world]. Defaults to your bank.";
  }
}