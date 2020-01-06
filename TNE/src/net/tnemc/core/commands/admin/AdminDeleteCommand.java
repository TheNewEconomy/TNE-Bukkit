package net.tnemc.core.commands.admin;

import net.tnemc.core.TNE;
import net.tnemc.core.commands.TNECommand;
import net.tnemc.core.common.Message;
import net.tnemc.core.common.WorldVariant;
import net.tnemc.core.common.account.WorldFinder;
import net.tnemc.core.common.api.IDFinder;
import org.bukkit.command.CommandSender;

/**
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by Daniel on 7/10/2017.
 */
public class AdminDeleteCommand implements CommandExecution {

  public AdminDeleteCommand(TNE plugin) {
    super(plugin);
  }

  @Override
  public String name() {
    return "delete";
  }

  @Override
  public String[] aliases() {
    return new String[0];
  }

  @Override
  public String node() {
    return "tne.admin.delete";
  }

  @Override
  public boolean console() {
    return true;
  }

  @Override
  public String helpLine() {
    return "Messages.Commands.Admin.Delete";
  }

  @Override
  public boolean execute(CommandSender sender, Command command, String label, String[] arguments) {
    if(arguments.length >= 1) {
      String world = WorldFinder.getWorld(sender, WorldVariant.BALANCE);
      if(TNE.manager().exists(IDFinder.getID(arguments[0]))) {
        TNE.manager().deleteAccount(IDFinder.getID(arguments[0]));

        Message m = new Message("Messages.Admin.Deleted");
        m.addVariable("$player", arguments[0]);
        m.translate(world, sender);
        return true;
      }
      Message m = new Message("Messages.General.NoPlayer");
      m.addVariable("$player", arguments[0]);
      m.translate(world, sender);
      return false;
    }
    MISCUtils.help(sender, label, arguments);
    return false;
  }
}