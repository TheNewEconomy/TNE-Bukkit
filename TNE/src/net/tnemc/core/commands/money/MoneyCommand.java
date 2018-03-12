package net.tnemc.core.commands.money;

import com.github.tnerevival.commands.TNECommand;
import net.tnemc.core.TNE;
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
    subCommands.add(new MoneyBalanceCommand(plugin));
    subCommands.add(new MoneyConvertCommand(plugin));
    subCommands.add(new MoneyGiveCommand(plugin));
    subCommands.add(new MoneyNoteCommand(plugin));
    subCommands.add(new MoneyPayCommand(plugin));
    subCommands.add(new MoneySetCommand(plugin));
    subCommands.add(new MoneyTakeCommand(plugin));
    subCommands.add(new MoneyTopCommand(plugin));
  }

  @Override
  public String getName() {
    return "money";
  }

  @Override
  public String[] getAliases() {
    return new String[] {
        "bal", "balance", "pay", "baltop"
    };
  }

  @Override
  public String getNode() {
    return "tne.money";
  }

  @Override
  public boolean console() {
    return true;
  }

  @Override
  public boolean execute(CommandSender sender, String command, String[] arguments) {
    if(command.equalsIgnoreCase("baltop")) {
      TNECommand sub = FindSub("top");
      return sub.execute(sender, command, arguments);
    }

    if(command.equalsIgnoreCase("pay")) {
      TNECommand sub = FindSub("pay");
      return sub.execute(sender, command, arguments);
    }

    if(command.equalsIgnoreCase("balance") ||
        command.equalsIgnoreCase("bal") ||
        arguments.length == 0) {
      TNECommand sub = FindSub("balance");
      return sub.execute(sender, command, arguments);
    }
    return super.execute(sender, command, arguments);
  }
}