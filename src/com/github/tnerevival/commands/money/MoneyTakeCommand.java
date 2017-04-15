package com.github.tnerevival.commands.money;

import com.github.tnerevival.TNE;
import com.github.tnerevival.account.IDFinder;
import com.github.tnerevival.commands.TNECommand;
import com.github.tnerevival.core.Message;
import com.github.tnerevival.core.currency.Currency;
import com.github.tnerevival.core.currency.CurrencyFormatter;
import com.github.tnerevival.core.transaction.TransactionType;
import com.github.tnerevival.utils.AccountUtils;
import com.github.tnerevival.utils.MISCUtils;
import org.bukkit.command.CommandSender;

import java.math.BigDecimal;

public class MoneyTakeCommand extends TNECommand {

  public MoneyTakeCommand(TNE plugin) {
    super(plugin);
  }

  @Override
  public String getName() {
    return "take";
  }

  @Override
  public String[] getAliases() {
    return new String[0];
  }

  @Override
  public String getNode() {
    return "tne.money.take";
  }

  @Override
  public boolean console() {
    return true;
  }

  @Override
  public boolean execute(CommandSender sender, String command, String[] arguments) {
    if(arguments.length >= 2) {
      String world = (arguments.length >= 3)? getWorld(sender, arguments[2]) : getWorld(sender);
      String currencyName = (arguments.length >= 4)? arguments[3] : TNE.instance().manager.currencyManager.get(world).getName();
      Currency currency = getCurrency(world, currencyName);
      BigDecimal value = CurrencyFormatter.translateBigDecimal(arguments[1], world);

      if(value.compareTo(BigDecimal.ZERO) < 0) {
        new Message("Messages.Money.Negative").translate(world, sender);
        return false;
      }

      if(arguments[0].equalsIgnoreCase(TNE.instance().api().getString("Core.Server.Name"))
          && !sender.hasPermission("tne.server.take")) {
        new Message("Messages.General.NoPerm").translate(world, sender);
        return false;
      }

      if(!TNE.instance().manager.currencyManager.contains(world, currencyName)) {
        Message m = new Message("Messages.Money.NoCurrency");
        m.addVariable("$currency", currencyName);
        m.addVariable("$world", world);
        m.translate(world, sender);
        return false;
      }

      MISCUtils.debug(world);

      if(getPlayer(sender, arguments[0]) != null) {
        if(AccountUtils.transaction(IDFinder.getID(getPlayer(sender, arguments[0])).toString(), IDFinder.getID(getPlayer(sender)).toString(), value, currency, TransactionType.MONEY_REMOVE, world)) {
          Message took = new Message("Messages.Money.Took");
          took.addVariable("$amount", CurrencyFormatter.format(world, value));
          took.addVariable("$player", arguments[0]);
          took.translate(world, sender);
          return true;
        }
      }
    }
    help(sender);
    return false;
  }

  @Override
  public String getHelp() {
    return "/money take <player> <amount> [world] [currency] - Make some of <player>'s money vanish into thin air";
  }

}