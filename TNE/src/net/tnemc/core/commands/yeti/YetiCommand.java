package net.tnemc.core.commands.yeti;

import com.github.tnerevival.commands.TNECommand;
import net.tnemc.core.TNE;
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
 * Created by Daniel on 10/17/2017.
 */
public class YetiCommand extends TNECommand {

  public YetiCommand(TNE plugin) {
    super(plugin);
    subCommands.add(new YetiIdiotCommand(plugin));
  }

  @Override
  public String getName() {
    return "yediot";
  }

  @Override
  public String[] getAliases() {
    return new String[0];
  }

  @Override
  public String getNode() {
    return "tne.yediot";
  }

  @Override
  public boolean console() {
    return false;
  }

  @Override
  public boolean execute(CommandSender sender, String command, String[] arguments) {
    if(arguments.length == 0 || !arguments[0].equalsIgnoreCase("idiot")) {
      arguments = new String[1];
      arguments[0] = "idiot";
    }

    return super.execute(sender, command, arguments);
  }
}
