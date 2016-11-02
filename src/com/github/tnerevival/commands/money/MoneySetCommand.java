package com.github.tnerevival.commands.money;

import com.github.tnerevival.TNE;
import com.github.tnerevival.commands.TNECommand;
import com.github.tnerevival.core.Message;
import com.github.tnerevival.core.currency.CurrencyFormatter;
import com.github.tnerevival.core.transaction.TransactionType;
import com.github.tnerevival.utils.AccountUtils;
import com.github.tnerevival.utils.MISCUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by Daniel on 10/12/2016.
 */
public class MoneySetCommand extends TNECommand {

  public MoneySetCommand(TNE plugin) {
    super(plugin);
  }

  @Override
  public String getName() {
    return "set";
  }

  @Override
  public String[] getAliases() {
    return new String[0];
  }

  @Override
  public String getNode() {
    return "tne.money.set";
  }

  @Override
  public boolean console() {
    return true;
  }

  @Override
  public boolean execute(CommandSender sender, String command, String[] arguments) {
    if(arguments.length >= 2) {
      String world = (arguments.length == 3)? arguments[2] : TNE.instance.defaultWorld;
      Double value = CurrencyFormatter.translateDouble(arguments[1], world);
      if(value < 0) {
        new Message("Messages.Money.Negative").translate(world, sender);
        return false;
      }

      if(MISCUtils.getPlayer(arguments[0]) != null) {

        String id = (sender instanceof Player)? MISCUtils.getID(getPlayer(sender)).toString() : null;

        AccountUtils.transaction(MISCUtils.getID(MISCUtils.getPlayer(arguments[0])).toString(), id, value, TransactionType.MONEY_SET, world);
        Message set = new Message("Messages.Money.Set");
        set.addVariable("$amount",  CurrencyFormatter.format(world, AccountUtils.round(value)));
        set.addVariable("$player", arguments[0]);
        set.translate(world, sender);
        return true;
      }
    } else {
      help(sender);
    }
    return false;
  }



  @Override
  public String getHelp() {
    return "/money set <player> <amount> [world] - Set <player>'s balance to <amount>.";
  }
}
