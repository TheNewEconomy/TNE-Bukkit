package net.tnemc.core.commands.admin;

import com.github.tnerevival.commands.TNECommand;
import net.tnemc.core.TNE;

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
public class AdminCommand extends TNECommand {

  public AdminCommand(TNE plugin) {
    super(plugin);
    subCommands.add(new AdminBackupCommand(plugin));
    subCommands.add(new AdminBalanceCommand(plugin));
    subCommands.add(new AdminCaveatsCommand(plugin));
    subCommands.add(new AdminCreateCommand(plugin));
    subCommands.add(new AdminDeleteCommand(plugin));
    subCommands.add(new AdminIDCommand(plugin));
    subCommands.add(new AdminMenuCommand(plugin));
    subCommands.add(new AdminPurgeCommand(plugin));
    subCommands.add(new AdminReloadCommand(plugin));
    subCommands.add(new AdminSaveCommand(plugin));
    subCommands.add(new AdminStatusCommand(plugin));
    subCommands.add(new AdminVersionCommand(plugin));
  }

  @Override
  public String getName() {
    return "tne";
  }

  @Override
  public String[] getAliases() {
    return new String[0];
  }

  @Override
  public String getNode() {
    return "tne.admin";
  }

  @Override
  public boolean console() {
    return true;
  }
}