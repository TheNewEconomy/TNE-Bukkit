package net.tnemc.core.commands.config;

import net.tnemc.commands.core.CommandExecution;
import net.tnemc.commands.core.provider.PlayerProvider;
import net.tnemc.core.TNE;
import net.tnemc.core.common.Message;
import net.tnemc.core.common.WorldVariant;
import net.tnemc.core.common.account.WorldFinder;
import net.tnemc.core.common.utils.MISCUtils;
import org.bukkit.command.CommandSender;

/**
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by Daniel on 7/12/2017.
 */
public class ConfigSaveCommand implements CommandExecution {

  @Override
  public boolean execute(PlayerProvider provider, String label, String[] arguments) {
    CommandSender sender = MISCUtils.getSender(provider);
    String configuration = (arguments.length >= 1)? arguments[0] : "all";
    String world = WorldFinder.getWorld(sender, WorldVariant.CONFIGURATION);
    if(configuration.equalsIgnoreCase("all")) {
      TNE.configurations().saveAll();
      Message message = new Message("Messages.Configuration.SavedAll");
      message.translate(world, sender);
      return true;
    }

    if(!TNE.configurations().configurations.containsKey(configuration)) {
      Message message = new Message("Messages.Configuration.InvalidFile");
      message.addVariable("$configuration", configuration);
      message.translate(world, sender);
      return false;
    }

    TNE.configurations().save(TNE.configurations().configurations.get(configuration));
    Message message = new Message("Messages.Configuration.Saved");
    message.addVariable("$configuration", configuration);
    message.translate(world, sender);
    return true;
  }
}