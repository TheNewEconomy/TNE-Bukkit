package net.tnemc.core.commands.admin;

import com.github.tnerevival.commands.TNECommand;
import com.github.tnerevival.core.Message;
import com.github.tnerevival.user.IDFinder;
import net.tnemc.core.TNE;
import net.tnemc.core.common.WorldVariant;
import net.tnemc.core.common.account.WorldFinder;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

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
 * Created by Daniel on 11/8/2017.
 */
public class AdminMenuCommand extends TNECommand {

  public AdminMenuCommand(TNE plugin) {
    super(plugin);
  }

  @Override
  public String getName() {
    return "menu";
  }

  @Override
  public String[] getAliases() {
    return new String[0];
  }

  @Override
  public String getNode() {
    return "tne.admin.menu";
  }

  @Override
  public boolean console() {
    return true;
  }

  @Override
  public String getHelp() {
    return "Messages.Commands.Admin.Menu";
  }

  @Override
  public boolean execute(CommandSender sender, String command, String[] arguments) {
    if(arguments.length >= 1) {
      String world = WorldFinder.getWorld(sender, WorldVariant.BALANCE);

      if(TNE.manager().exists(IDFinder.getID(arguments[0]))) {
        UUID id = IDFinder.getID(sender);
        Player player = getPlayer(sender);

        TNE.menuManager().open("main", player);
        TNE.menuManager().setViewerData(id, "action_player",IDFinder.getID(arguments[0]));
        TNE.menuManager().setViewerData(id, "action_world", world);
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