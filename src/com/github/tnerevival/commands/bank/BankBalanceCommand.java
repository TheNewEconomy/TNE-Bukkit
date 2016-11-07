package com.github.tnerevival.commands.bank;

import com.github.tnerevival.TNE;
import com.github.tnerevival.account.IDFinder;
import com.github.tnerevival.commands.TNECommand;
import com.github.tnerevival.core.Message;
import com.github.tnerevival.core.currency.CurrencyFormatter;
import com.github.tnerevival.utils.BankUtils;
import com.github.tnerevival.utils.MISCUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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
    String ownerName = (arguments.length >= 1)? arguments[0] : sender.getName();
    Player owner = MISCUtils.getPlayer(ownerName);
    Player player = MISCUtils.getPlayer(sender.getName());

    if(BankUtils.hasBank(IDFinder.getID(owner))) {
      if(BankUtils.bankMember(IDFinder.getID(owner), IDFinder.getID(sender.getName()))) {
        Message balance = new Message("Messages.Bank.Balance");
        balance.addVariable("$amount",  CurrencyFormatter.format(owner.getWorld().getName(), BankUtils.getBankBalance(IDFinder.getID(player))));
        balance.addVariable("$name", ownerName);
        balance.translate(MISCUtils.getWorld(player), player);
        return true;
      }
      Message noAccess = new Message("Messages.Bank.Invalid");
      noAccess.addVariable("$name", ownerName);
      noAccess.translate(MISCUtils.getWorld(player), player);
    }
    new Message("Messages.Bank.None").translate(MISCUtils.getWorld(player), player);
    return false;
  }

  @Override
  public String getHelp() {
    return "/bank balance [owner] - Find out how much gold is in a specific bank. Defaults to your personal bank.";
  }
}