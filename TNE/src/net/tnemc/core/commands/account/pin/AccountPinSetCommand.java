package net.tnemc.core.commands.account.pin;

import net.tnemc.core.TNE;
import net.tnemc.core.commands.TNECommand;
import net.tnemc.core.common.Message;
import net.tnemc.core.common.WorldVariant;
import net.tnemc.core.common.account.TNEAccount;
import net.tnemc.core.common.account.WorldFinder;
import net.tnemc.core.common.api.IDFinder;
import org.bukkit.command.CommandSender;

import java.util.UUID;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * Created by creatorfromhell on 6/21/2019.
 * <p>
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */
public class AccountPinSetCommand extends TNECommand {

  public AccountPinSetCommand(TNE plugin) {
    super(plugin);
  }

  @Override
  public String name() {
    return "set";
  }

  @Override
  public String[] aliases() {
    return new String[0];
  }

  @Override
  public String node() {
    return "tne.account.pin.set";
  }

  @Override
  public boolean console() {
    return false;
  }

  @Override
  public String helpLine() {
    return "Messages.Commands.Account.PinSet";
  }

  @Override
  public boolean execute(CommandSender sender, String command, String[] arguments) {
    if(arguments.length < 1) {
      help(sender);
      return false;
    }

    final UUID id = IDFinder.getID(sender);
    final String world = WorldFinder.getWorld(sender, WorldVariant.CONFIGURATION);
    final String pin = arguments[0];

    TNEAccount account = TNE.manager().getAccount(id);
    final boolean set = !account.getPin().equalsIgnoreCase("TNEPIN");

    if(set && arguments.length < 2) {
      new Message("Messages.Account.PinSetAlready").translate(world, sender);
      return false;
    }

    if(set && !account.getPin().equals(arguments[1])) {
      new Message("Messages.Account.PinIncorrect").translate(world, sender);
      return false;
    }

    if(pin.length() > 60) {
      new Message("Messages.Account.PinMax").translate(world, sender);
      return false;
    }

    account.setPin(pin);
    TNE.manager().addAccount(account);

    Message message = new Message("Messages.Account.PinSet");
    message.addVariable("$pin", pin);
    message.translate(world, sender);
    return true;
  }
}