package net.tnemc.core.commands.admin;

import com.github.tnerevival.commands.TNECommand;
import com.github.tnerevival.core.Message;
import com.github.tnerevival.user.IDFinder;
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
public class AdminIDCommand extends TNECommand {

  public AdminIDCommand(TNE plugin) {
    super(plugin);
  }

  @Override
  public String getName() {
    return "id";
  }

  @Override
  public String[] getAliases() {
    return new String[0];
  }

  @Override
  public String getNode() {
    return "tne.admin.id";
  }

  @Override
  public boolean console() {
    return true;
  }

  @Override
  public String getHelp() {
    return "Messages.Commands.Admin.ID";
  }

  @Override
  public boolean execute(CommandSender sender, String command, String[] arguments) {
    if(arguments.length >= 1) {
      String world = WorldFinder.getWorld(sender);

      if(TNE.manager().exists(IDFinder.getID(arguments[0]))) {
        Message m = new Message("Messages.Admin.ID");
        m.addVariable("$player", arguments[0]);
        m.addVariable("$id", IDFinder.getID(arguments[0]).toString());

        m.translate(world, sender);
        return true;
      }
      Message m = new Message("Messages.General.NoPlayer");
      m.addVariable("$player", arguments[0]);
      m.translate(world, sender);
      return false;
    }
    help(sender);
    return false;
  }
}