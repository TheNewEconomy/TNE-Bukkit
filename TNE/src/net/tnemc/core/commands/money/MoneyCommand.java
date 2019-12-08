package net.tnemc.core.commands.money;

import net.tnemc.core.TNE;
import net.tnemc.core.commands.TNECommand;
import org.bukkit.command.CommandSender;

/**
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by Daniel on 7/10/2017.
 */
public class MoneyCommand extends TNECommand {

  public MoneyCommand(TNE plugin) {
    super(plugin);
    addSub(new MoneyBalanceCommand(plugin));
    addSub(new MoneyConvertCommand(plugin));
    addSub(new MoneyConsolidateCommand(plugin));
    addSub(new MoneyGiveCommand(plugin));
    addSub(new MoneyNoteCommand(plugin));
    addSub(new MoneyOtherCommand(plugin));
    addSub(new MoneyPayCommand(plugin));
    addSub(new MoneyPayFromCommand(plugin));
    addSub(new MoneySetCommand(plugin));
    addSub(new MoneySetAllCommand(plugin));
    addSub(new MoneyTakeCommand(plugin));
    addSub(new MoneyTopCommand(plugin));
  }

  @Override
  public String name() {
    return "money";
  }

  @Override
  public String[] aliases() {
    return new String[] {
        "bal", "balance", "pay", "baltop",
        "balother", "balo", "balanceother",
        "givemoney", "givebal", "setbal",
        "setmoney", "takemoney", "takebal"
    };
  }

  @Override
  public String node() {
    return "";
  }

  @Override
  public boolean console() {
    return true;
  }

  @Override
  public boolean execute(CommandSender sender, String command, String[] arguments) {
    if(command.equalsIgnoreCase("givemoney") ||
        command.equalsIgnoreCase("givebal")) {
      TNECommand sub = findSub("give");
      if(sub.canExecute(sender)) {
        return sub.execute(sender, command, arguments);
      }
    }

    if(command.equalsIgnoreCase("setmoney") ||
        command.equalsIgnoreCase("setbal")) {
      TNECommand sub = findSub("set");
      if(sub.canExecute(sender)) {
        return sub.execute(sender, command, arguments);
      }
    }

    if(command.equalsIgnoreCase("takemoney") ||
        command.equalsIgnoreCase("takebal")) {
      TNECommand sub = findSub("take");
      if(sub.canExecute(sender)) {
        return sub.execute(sender, command, arguments);
      }
    }

    if(command.equalsIgnoreCase("balother") ||
        command.equalsIgnoreCase("balo") ||
        command.equalsIgnoreCase("balanceother")) {
      TNECommand sub = findSub("other");
      if(sub.canExecute(sender)) {
        return sub.execute(sender, command, arguments);
      }
    }

    if(command.equalsIgnoreCase("baltop")) {
      TNECommand sub = findSub("top");
      if(sub.canExecute(sender)) {
        return sub.execute(sender, command, arguments);
      }
    }

    if(command.equalsIgnoreCase("pay")) {
      TNECommand sub = findSub("pay");
      if(sub.canExecute(sender)) {
        return sub.execute(sender, command, arguments);
      }
    }

    if(command.equalsIgnoreCase("balance") ||
        command.equalsIgnoreCase("bal") ||
        arguments.length == 0) {
      TNECommand sub = findSub("balance");

      if(!TNE.configurations().getBoolean("Core.Multiworld") && arguments.length > 0) {
        sub = findSub("other");
      }
      if(sub.canExecute(sender)) {
        return sub.execute(sender, command, arguments);
      }
    }
    return super.execute(sender, command, arguments);
  }
}