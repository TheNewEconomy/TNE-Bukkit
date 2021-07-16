package net.tnemc.core.commands.config;

import net.tnemc.commands.core.CommandExecution;
import net.tnemc.commands.core.provider.PlayerProvider;
import net.tnemc.core.TNE;
import net.tnemc.core.common.Message;
import net.tnemc.core.common.WorldVariant;
import net.tnemc.core.common.account.WorldFinder;
import net.tnemc.core.common.api.IDFinder;
import net.tnemc.core.common.utils.MISCUtils;
import org.bukkit.command.CommandSender;

/**
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by Daniel on 7/10/2017.
 */
public class ConfigTNEGetCommand implements CommandExecution {

  @Override
  public boolean execute(PlayerProvider provider, String label, String[] arguments) {
    CommandSender sender = MISCUtils.getSender(provider);
    if(arguments.length >= 1) {
      String node = arguments[0];
      String world = (arguments.length >= 2)? arguments[1] : WorldFinder.getWorld(sender, WorldVariant.CONFIGURATION);
      String player = (arguments.length >= 3)? arguments[2] : IDFinder.getID(sender).toString();

      if(!TNE.configurations().hasConfiguration(node)) {
        Message message = new Message("Messages.Configuration.NoSuch");
        message.addVariable("$node", node);
        message.translate(world, sender);
        return false;
      }

      Object value = TNE.configurations().getConfiguration(node, world, player);
      Message message = new Message("Messages.Configuration.Get");
      message.addVariable("$node", node);
      message.addVariable("$value", value.toString());
      message.translate(world, sender);
      return true;
    }
    MISCUtils.help(sender, label, arguments);
    return false;
  }
}