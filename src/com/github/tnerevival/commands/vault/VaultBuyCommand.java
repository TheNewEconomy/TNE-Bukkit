package com.github.tnerevival.commands.vault;

import com.github.tnerevival.TNE;
import com.github.tnerevival.account.Bank;
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

public class VaultBuyCommand extends TNECommand {

  public VaultBuyCommand(TNE plugin) {
    super(plugin);
  }

  @Override
  public String getName() {
    return "buy";
  }

  @Override
  public String[] getAliases() {
    return new String[0];
  }

  @Override
  public String getNode() {
    return "tne.bank.buy";
  }

  @Override
  public boolean console() {
    return false;
  }

  @Override
  public boolean execute(CommandSender sender, String command, String[] arguments) {
    Player player = getPlayer(sender);
    if(BankUtils.hasBank(IDFinder.getID(player))) {
      new Message("Messages.Bank.Already").translate(MISCUtils.getWorld(player), player);
      return false;
    }

    if(!player.hasPermission("tne.bank.bypass")) {
      if(AccountUtils.transaction(IDFinder.getID(player).toString(), null, BankUtils.cost(MISCUtils.getWorld(player), IDFinder.getID(player).toString()), TransactionType.MONEY_INQUIRY, MISCUtils.getWorld(player))) {
        AccountUtils.transaction(IDFinder.getID(player).toString(), null, BankUtils.cost(MISCUtils.getWorld(player), IDFinder.getID(player).toString()), TransactionType.MONEY_REMOVE, MISCUtils.getWorld(player));
      } else {
        Message insufficient = new Message("Messages.Money.Insufficient");
        insufficient.addVariable("$amount",  CurrencyFormatter.format(MISCUtils.getWorld(player), BankUtils.cost(player.getWorld().getName(), IDFinder.getID(player).toString())));
        insufficient.translate(MISCUtils.getWorld(player), player);
        return false;
      }
    }
    MISCUtils.debug(IDFinder.getID(player).toString());
    Bank bank = new Bank(IDFinder.getID(player), BankUtils.size(player.getWorld().getName(), IDFinder.getID(player).toString()));
    AccountUtils.getAccount(IDFinder.getID(player)).getBanks().put(MISCUtils.getWorld(player), bank);
    new Message("Messages.Bank.Bought").translate(MISCUtils.getWorld(player), player);
    return true;
  }

  @Override
  public String getHelp() {
    return "/bank buy - Used to purchase a bank.";
  }

}