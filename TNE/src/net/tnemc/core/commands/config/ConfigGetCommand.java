package net.tnemc.core.commands.config;

import com.github.tnerevival.commands.TNECommand;
import com.github.tnerevival.core.Message;
import net.tnemc.core.TNE;
import net.tnemc.core.common.account.WorldFinder;
import org.bukkit.command.CommandSender;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * Created by Daniel on 7/10/2017.
 */
public class ConfigGetCommand extends TNECommand {

  public ConfigGetCommand(TNE plugin) {
    super(plugin);
  }

  @Override
  public String getName() {
    return "get";
  }

  @Override
  public String[] getAliases() {
    return new String[0];
  }

  @Override
  public String getNode() {
    return "tne.config.get";
  }

  @Override
  public boolean console() {
    return true;
  }

  @Override
  public String getHelp() {
    return "Messages.Commands.Config.Get";
  }

  @Override
  public boolean execute(CommandSender sender, String command, String[] arguments) {
    if(arguments.length >= 1) {
      String node = arguments[0];
      String configuration = (arguments.length >= 2)? arguments[1] : "all";
      boolean valid = (configuration.equalsIgnoreCase("all"))? TNE.configurations().hasNode(node,
                                                                              TNE.configurations().fromPrefix(
                                                                                  TNE.configurations().getPrefix(node)))
                                                                        : TNE.configurations().hasNode(node, configuration);
      if(!valid) {
        Message message = new Message("Messages.Configuration.NoSuch");
        message.addVariable("$node", node);
        message.addVariable("$configuration", configuration);
        message.translate(WorldFinder.getWorld(sender), sender);
        return false;
      }
      Object value = (configuration.equalsIgnoreCase("all"))? TNE.configurations().getValue(node)
                                                                       : TNE.configurations().getValue(node, configuration);

      Message message = new Message("Messages.Configuration.Get");
      message.addVariable("$node", node);
      message.addVariable("$configuration", configuration);
      message.addVariable("$value", value.toString());
      message.translate(WorldFinder.getWorld(sender), sender);
      return true;
    }
    help(sender);
    return false;
  }
}