package com.github.tnerevival.commands.money;

import com.github.tnerevival.TNE;
import com.github.tnerevival.account.IDFinder;
import com.github.tnerevival.commands.TNECommand;
import com.github.tnerevival.core.Message;
import com.github.tnerevival.core.currency.CurrencyFormatter;
import com.github.tnerevival.core.transaction.TransactionType;
import com.github.tnerevival.utils.AccountUtils;
import com.github.tnerevival.utils.MISCUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.math.BigDecimal;

public class MoneyPayCommand extends TNECommand {

  public MoneyPayCommand(TNE plugin) {
    super(plugin);
  }

  @Override
  public String getName() {
    return "pay";
  }

  @Override
  public String[] getAliases() {
    return new String[0];
  }

  @Override
  public String getNode() {
    return "tne.money.pay";
  }

  @Override
  public boolean console() {
    return false;
  }

  @Override
  public boolean execute(CommandSender sender, String command, String[] arguments) {
    Player player = getPlayer(sender);
    if(arguments.length >= 2) {
      BigDecimal value = CurrencyFormatter.translateBigDecimal(arguments[1], IDFinder.getWorld(getPlayer(sender)));
      if(value.compareTo(BigDecimal.ZERO) < 0) {
        new Message("Messages.Money.Negative").translate(IDFinder.getWorld(player), player);
        return false;
      }
      if(getPlayer(sender, arguments[0]) != null && IDFinder.getID(player).equals(IDFinder.getID(getPlayer(sender, arguments[0])))) {
        new Message("Messages.Money.SelfPay").translate(IDFinder.getWorld(player), player);
        return false;
      }

      if(AccountUtils.transaction(IDFinder.getID(player).toString(), null, value, TransactionType.MONEY_INQUIRY, IDFinder.getWorld(player))) {
        MISCUtils.debug("Player has funds");
        if(getPlayer(sender, arguments[0]) != null) {
          MISCUtils.debug("Player not null");
          boolean transaction = AccountUtils.transaction(IDFinder.getID(player).toString(), IDFinder.getID(getPlayer(sender, arguments[0])).toString(), value, TransactionType.MONEY_PAY, IDFinder.getWorld(player));
          MISCUtils.debug("" + transaction);
          if(transaction) {
            MISCUtils.debug("Paid player");
            Message paid = new Message("Messages.Money.Paid");
            paid.addVariable("$amount", CurrencyFormatter.format(player.getWorld().getName(), value));
            paid.addVariable("$player", arguments[0]);
            paid.translate(IDFinder.getWorld(player), player);
            return true;
          }
        }
      } else {
        Message insufficient = new Message("Messages.Money.Insufficient");
        insufficient.addVariable("$amount", CurrencyFormatter.format(player.getWorld().getName(), CurrencyFormatter.translateBigDecimal(arguments[1], getWorld(sender))));
        insufficient.translate(IDFinder.getWorld(player), player);
        return false;
      }
    }
    help(sender);
    return false;
  }

  @Override
  public String getHelp() {
    return "/money pay <player> <amount> - pay a player money from your balance";
  }

}