package net.tnemc.core.commands.config;

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
public class ConfigGetCommand extends TNECommand {

  public ConfigGetCommand(TNE plugin) {
    super(plugin);
  }

  @Override
  public String name() {
    return "get";
  }

  @Override
  public String[] aliases() {
    return new String[0];
  }

  @Override
  public String node() {
    return "tne.config.get";
  }

  @Override
  public boolean console() {
    return true;
  }

  @Override
  public String helpLine() {
    return "Messages.Commands.Config.Get";
  }

  @Override
  public boolean execute(CommandSender sender, String command, String[] arguments) {
    if(arguments.length >= 1) {
      String node = arguments[0];
      String world = WorldFinder.getWorld(sender, WorldVariant.CONFIGURATION);
      String configuration = (arguments.length >= 2)? arguments[1] : "all";
      boolean valid = (configuration.equalsIgnoreCase("all"))? TNE.configurations().hasNode(node,
                                                                              TNE.configurations().fromPrefix(
                                                                                  TNE.configurations().getPrefix(node)))
                                                                        : TNE.configurations().hasNode(node, configuration);
      if(!valid) {
        Message message = new Message("Messages.Configuration.NoSuch");
        message.addVariable("$node", node);
        message.addVariable("$configuration", configuration);
        message.translate(world, sender);
        return false;
      }
      Object value = (configuration.equalsIgnoreCase("all"))? TNE.configurations().getValue(node)
                                                                       : TNE.configurations().getValue(node, configuration, world, IDFinder.getID(sender).toString());

      Message message = new Message("Messages.Configuration.Get");
      message.addVariable("$node", node);
      message.addVariable("$configuration", configuration);
      message.addVariable("$value", value.toString());
      message.translate(world, sender);
      return true;
    }
    help(sender);
    return false;
  }
}