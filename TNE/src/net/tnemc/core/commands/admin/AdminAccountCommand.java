package net.tnemc.core.commands.admin;

import net.tnemc.core.TNE;
import net.tnemc.core.commands.TNECommand;
import org.bukkit.command.CommandSender;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * Created by Daniel on 11/12/2018.
 * <p>
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */
public class AdminAccountCommand extends TNECommand {

  public AdminAccountCommand(TNE plugin) {
    super(plugin);
  }

  @Override
  public String getName() {
    return "account";
  }

  @Override
  public String[] getAliases() {
    return new String[0];
  }

  @Override
  public String getNode() {
    return "tne.admin.account";
  }

  @Override
  public boolean console() {
    return true;
  }

  @Override
  public String getHelp() {
    return "/tne account <username> - Returns a count of how many accounts are associated with a display name, debug command.";
  }

  @Override
  public boolean execute(CommandSender sender, String command, String[] arguments) {
    if(arguments.length >= 1) {
      sender.sendMessage("Accounts associated with " + arguments[0] + ": " + TNE.saveManager().getTNEManager().getTNEProvider().accountCount(arguments[0]));
      return true;
    }
    help(sender);
    return false;
  }
}