package net.tnemc.core.commands.admin;

import com.github.tnerevival.commands.TNECommand;
import com.github.tnerevival.core.Message;
import com.github.tnerevival.user.IDFinder;
import net.tnemc.core.TNE;
import net.tnemc.core.common.account.WorldFinder;
import org.bukkit.Bukkit;
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
public class AdminPurgeCommand extends TNECommand {

  public AdminPurgeCommand(TNE plugin) {
    super(plugin);
  }

  @Override
  public String getName() {
    return "purge";
  }

  @Override
  public String[] getAliases() {
    return new String[0];
  }

  @Override
  public String getNode() {
    return "tne.admin.purge";
  }

  @Override
  public boolean console() {
    return true;
  }

  @Override
  public String getHelp() {
    return "Messages.Commands.Admin.Purge";
  }

  @Override
  public boolean execute(CommandSender sender, String command, String[] arguments) {
    String world = WorldFinder.getWorld(sender);
    boolean isWorld = arguments.length >= 1;
    if(isWorld) {
      if(Bukkit.getWorld(arguments[0]) == null)
        new Message("Messages.General.NoWorld").translate(world, sender);
      TNE.manager().purge(arguments[0]);
      Message m = new Message("Messages.Admin.PurgeWorld");
      m.addVariable("$world", arguments[0]);
      m.translate(world, IDFinder.getID(sender));
      return true;
    }
    TNE.manager().purgeAll();
    new Message("Messages.Admin.Purge").translate(world, sender);
    return true;
  }
}