package net.tnemc.core.commands.config;

import com.github.tnerevival.user.IDFinder;
import net.tnemc.core.TNE;
import net.tnemc.core.commands.TNECommand;
import net.tnemc.core.common.Message;
import net.tnemc.core.common.WorldVariant;
import net.tnemc.core.common.account.WorldFinder;
import org.bukkit.command.CommandSender;

/**
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by Daniel on 7/10/2017.
 */
public class ConfigSetCommand extends TNECommand {

  public ConfigSetCommand(TNE plugin) {
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
    return "tne.config.set";
  }

  @Override
  public boolean console() {
    return true;
  }

  @Override
  public String getHelp() {
    return "Messages.Commands.Config.Set";
  }

  @Override
  public boolean execute(CommandSender sender, String command, String[] arguments) {
    if(arguments.length >= 2) {
      String node = arguments[0];
      String configuration = (arguments.length >= 3)? arguments[2] :
                                                      TNE.configurations().fromPrefix(
                                                          TNE.configurations().getPrefix(node)
                                                      );

      String world = WorldFinder.getWorld(sender, WorldVariant.CONFIGURATION);
      if(!TNE.configurations().hasNode(node, configuration)) {
        Message message = new Message("Messages.Configuration.NoSuch");
        message.addVariable("$node", node);
        message.translate(world, sender);
        return false;
      }

      Object value = TNE.configurations().getValue(node, configuration, world, IDFinder.getID(sender).toString());
      Object newValue = arguments[1];

      if(!value.getClass().equals(newValue.getClass())) {
        Message message = new Message("Messages.Configuration.Invalid");
        message.addVariable("$node", node);
        message.addVariable("$value", newValue.toString());
        message.translate(world, sender);
        return false;
      }
      TNE.configurations().setValue(node, configuration, newValue);
      Message message = new Message("Messages.Configuration.Set");
      message.addVariable("$node", node);
      message.addVariable("$value", newValue.toString());
      message.translate(world, sender);
      return true;
    }
    help(sender);
    return true;
  }
}