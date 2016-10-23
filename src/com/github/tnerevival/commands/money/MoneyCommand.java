package com.github.tnerevival.commands.money;

import com.github.tnerevival.TNE;
import com.github.tnerevival.commands.TNECommand;
import com.github.tnerevival.core.Message;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MoneyCommand extends TNECommand {

  public MoneyCommand(TNE plugin) {
    super(plugin);
    subCommands.add(new MoneyBalanceCommand(plugin));
    subCommands.add(new MoneyGiveCommand(plugin));
    subCommands.add(new MoneyHistoryCommand(plugin));
    subCommands.add(new MoneyPayCommand(plugin));
    subCommands.add(new MoneySetCommand(plugin));
    subCommands.add(new MoneyTakeCommand(plugin));
  }

  @Override
  public String getName() {
    return "money";
  }

  @Override
  public String[] getAliases() {
    if(TNE.instance.api.getBoolean("Core.Commands.BalanceShort")) {
      return new String[] { "bal", "balance" };
    }
    return new String[0];
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
  public Boolean confirm() {
    return true;
  }

  @Override
  public Boolean locked() {
    return true;
  }

  @Override
  public boolean execute(CommandSender sender, String command, String[] arguments) {

    if(command.equalsIgnoreCase("bal") || command.equalsIgnoreCase("balance") ||
       arguments.length == 0 && sender instanceof Player && !command.equalsIgnoreCase("pay")) {
      TNECommand sub = FindSub("balance");
      if(sub.canExecute(sender)) {
        return sub.execute(sender, command, arguments);
      } else {
        Message unable = new Message("Messages.Command.Unable");
        unable.addVariable("$command", "/" + getName());
        unable.translate(TNE.instance.defaultWorld, sender);
        return false;
      }
    }

    if(command.equalsIgnoreCase("pay")) {
      TNECommand sub = FindSub("pay");
      return sub.execute(sender, command, arguments);
    }

    return super.execute(sender, command, arguments);
  }
}