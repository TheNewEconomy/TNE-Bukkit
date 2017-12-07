package net.tnemc.core.commands.config;

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
public class ConfigCommand extends TNECommand {

  public ConfigCommand(TNE plugin) {
    super(plugin);
    subCommands.add(new ConfigGetCommand(plugin));
    subCommands.add(new ConfigSaveCommand(plugin));
    subCommands.add(new ConfigSetCommand(plugin));
    subCommands.add(new ConfigTNEGetCommand(plugin));
    subCommands.add(new ConfigUndoCommand(plugin));
  }

  @Override
  public String getName() {
    return "tneconfig";
  }

  @Override
  public String[] getAliases() {
    return new String[] {
        "tnec"
    };
  }

  @Override
  public String getNode() {
    return "tne.config";
  }

  @Override
  public boolean console() {
    return true;
  }
}