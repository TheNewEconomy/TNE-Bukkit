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
 * Created by Daniel on 7/12/2017.
 */
public class ConfigSaveCommand extends TNECommand {

  public ConfigSaveCommand(TNE plugin) {
    super(plugin);
  }

  @Override
  public String getName() {
    return "save";
  }

  @Override
  public String[] getAliases() {
    return new String[0];
  }

  @Override
  public String getNode() {
    return "tne.config.save";
  }

  @Override
  public boolean console() {
    return true;
  }

  @Override
  public String getHelp() {
    return "Messages.Commands.Config.Save";
  }

  @Override
  public boolean execute(CommandSender sender, String command, String[] arguments) {
    String configuration = (arguments.length >= 1)? arguments[0] : "all";
    if(configuration.equalsIgnoreCase("all")) {
      TNE.configurations().saveAll();
      Message message = new Message("Messages.Configuration.SavedAll");
      message.translate(WorldFinder.getWorld(sender), sender);
      return true;
    }

    if(!TNE.configurations().configurations.containsKey(configuration)) {
      Message message = new Message("Messages.Configuration.InvalidFile");
      message.addVariable("$configuration", configuration);
      message.translate(WorldFinder.getWorld(sender), sender);
      return false;
    }

    TNE.configurations().save(TNE.configurations().configurations.get(configuration));
    Message message = new Message("Messages.Configuration.Saved");
    message.addVariable("$configuration", configuration);
    message.translate(WorldFinder.getWorld(sender), sender);
    return true;
  }
}