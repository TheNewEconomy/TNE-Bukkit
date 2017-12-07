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

      if(!TNE.configurations().hasNode(node, configuration)) {
        Message message = new Message("Messages.Configuration.NoSuch");
        message.addVariable("$node", node);
        message.translate(WorldFinder.getWorld(sender), sender);
        return false;
      }

      Object value = TNE.configurations().getValue(node, configuration);
      Object newValue = arguments[1];

      if(!value.getClass().equals(newValue.getClass())) {
        Message message = new Message("Messages.Configuration.Invalid");
        message.addVariable("$node", node);
        message.addVariable("$value", newValue.toString());
        message.translate(WorldFinder.getWorld(sender), sender);
        return false;
      }
      TNE.configurations().setValue(node, configuration, newValue);
      Message message = new Message("Messages.Configuration.Set");
      message.addVariable("$node", node);
      message.addVariable("$value", newValue.toString());
      message.translate(WorldFinder.getWorld(sender), sender);
      return true;
    }
    help(sender);
    return true;
  }
}