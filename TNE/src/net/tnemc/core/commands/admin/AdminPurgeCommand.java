package net.tnemc.core.commands.admin;

import net.tnemc.commands.core.CommandExecution;
import net.tnemc.commands.core.provider.PlayerProvider;
import net.tnemc.core.TNE;
import net.tnemc.core.common.Message;
import net.tnemc.core.common.WorldVariant;
import net.tnemc.core.common.account.WorldFinder;
import net.tnemc.core.common.api.IDFinder;
import net.tnemc.core.common.utils.MISCUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

/**
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by Daniel on 7/10/2017.
 */
public class AdminPurgeCommand implements CommandExecution {

  @Override
  public boolean execute(PlayerProvider provider, String label, String[] arguments) {
    CommandSender sender = MISCUtils.getSender(provider);
    String world = WorldFinder.getWorld(sender, WorldVariant.BALANCE);
    if(arguments.length >= 1) {
      if (Bukkit.getWorld(arguments[0]) == null)
        new Message("Messages.General.NoWorld").translate(world, sender);
      TNE.manager().purge(arguments[0]);
      Message m = new Message("Messages.Admin.PurgeWorld");
      m.addVariable("$world", arguments[0]);
      m.translate(world, IDFinder.getID(sender));
      return true;
    }
    MISCUtils.help(sender, label, arguments);
    return true;
  }
}