package com.github.tnerevival.commands.pin;

import com.github.tnerevival.TNE;
import com.github.tnerevival.account.Account;
import com.github.tnerevival.account.IDFinder;
import com.github.tnerevival.commands.TNECommand;
import com.github.tnerevival.core.Message;
import com.github.tnerevival.utils.AccountUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PinSetCommand extends TNECommand {

  public PinSetCommand(TNE plugin) {
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
    return "tne.pin.set";
  }

  @Override
  public boolean console() {
    return false;
  }

  @Override
  public boolean execute(CommandSender sender, String command, String[] arguments) {
    if(arguments.length == 2) {
      Player player = (Player)sender;
      Account acc = AccountUtils.getAccount(IDFinder.getID(player));

      if(!acc.getPin().equalsIgnoreCase("TNENOSTRINGVALUE")) {
        help(sender);
        return false;
      }

      if(!arguments[0].equals(arguments[1])) {
        help(sender);
        return false;
      }

      if(!acc.getPin().equalsIgnoreCase("TNENOSTRINGVALUE") && !acc.getPin().equals(arguments[2])) {
        help(sender);
        return false;
      }

      acc.setPin(arguments[0]);
      TNE.instance().manager.accounts.put(acc.getUid(), acc);
      TNE.instance().manager.confirmed.add(IDFinder.getID(player));
      Message message = new Message("Messages.Pin.Set");
      message.translate(IDFinder.getWorld(player), player);
      return true;
    }
    help(sender);
    return false;
  }

  @Override
  public String getHelp() {
    return "/pin set <pin> <confirm pin> [old pin] - Set your pin to <pin>'s value. Old pin is required if you have one set. Pins are case-sensitive.";
  }
}