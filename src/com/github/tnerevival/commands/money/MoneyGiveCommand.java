package com.github.tnerevival.commands.money;

import com.github.tnerevival.TNE;
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

public class MoneyGiveCommand extends TNECommand {

  public MoneyGiveCommand(TNE plugin) {
    super(plugin);
  }

  @Override
  public String getName() {
    return "give";
  }

  @Override
  public String[] getAliases() {
    return new String[0];
  }

  @Override
  public String getNode() {
    return "tne.money.give";
  }

  @Override
  public boolean console() {
    return true;
  }

  @Override
  public boolean execute(CommandSender sender, String command, String[] arguments) {
    if(arguments.length >= 2) {
      String world = (arguments.length == 3)? getWorld(sender, arguments[2]) : getWorld(sender);
      String currencyName = (arguments.length >= 4)? arguments[3] : TNE.instance.manager.currencyManager.get(world).getName();
      Currency currency = getCurrency(world, currencyName);
      BigDecimal value = CurrencyFormatter.translateBigDecimal(arguments[1], world);
      if(value.compareTo(BigDecimal.ZERO) < 0) {
        new Message("Messages.Money.Negative").translate(world, sender);
        return false;
      }

      if(!TNE.instance.manager.currencyManager.contains(world, currencyName)) {
        Message m = new Message("Messages.Money.NoCurrency");
        m.addVariable("$currency", currencyName);
        m.addVariable("$world", world);
        m.translate(world, sender);
        return false;
      }

      if(IDFinder.getID(arguments[0]) != null) {

        String id = (sender instanceof Player)? IDFinder.getID(getPlayer(sender)).toString() : null;
        AccountUtils.transaction(id, IDFinder.getID(arguments[0]).toString(), value, currency, TransactionType.MONEY_GIVE, world);
        Message gave = new Message("Messages.Money.Gave");
        gave.addVariable("$amount",  CurrencyFormatter.format(world, value));
        gave.addVariable("$player", arguments[0]);
        gave.translate(world, sender);
        return true;
      }
    } else {
      help(sender);
    }
    return false;
  }



  @Override
  public String getHelp() {
    return "/money give <player> <amount> [world] [currency] - summon money from air and give it to a player";
  }
}