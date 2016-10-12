package com.github.tnerevival.commands.money;

import com.github.tnerevival.TNE;
import com.github.tnerevival.commands.TNECommand;
import com.github.tnerevival.core.Message;
import com.github.tnerevival.core.transaction.TransactionType;
import com.github.tnerevival.utils.AccountUtils;
import com.github.tnerevival.utils.MISCUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

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
      Double value = Double.valueOf(arguments[1].replace(TNE.instance.api.getString("Core.Currency.Decimal", world, MISCUtils.getID(getPlayer(sender)).toString()), "."));
      if(value < 0) {
        sender.sendMessage(new Message("Messages.Money.Negative").translate());
        return false;
      }

      if(getPlayer(sender, arguments[0]) != null) {

        AccountUtils.transaction(MISCUtils.getID(getPlayer(sender, arguments[0])).toString(), MISCUtils.getID(getPlayer(sender)).toString(), value, TransactionType.MONEY_SET, world);
        Message set = new Message("Messages.Money.Set");
        set.addVariable("$amount",  MISCUtils.formatBalance(world, AccountUtils.round(value)));
        set.addVariable("$player", arguments[0]);
        sender.sendMessage(set.translate());
        return true;
      }
    } else {
      help(sender);
    }
    return false;
  }



  @Override
  public void help(CommandSender sender) {
    sender.sendMessage(ChatColor.GOLD + "/money set <player> <amount> [world] - Set <player>'s balance to <amount>.");
  }
}
