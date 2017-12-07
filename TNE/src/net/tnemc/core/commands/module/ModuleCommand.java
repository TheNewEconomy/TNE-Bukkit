package net.tnemc.core.commands.module;

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
public class ModuleCommand extends TNECommand {

  public ModuleCommand(TNE plugin) {
    super(plugin);
    subCommands.add(new ModuleInfoCommand(plugin));
    subCommands.add(new ModuleListCommand(plugin));
    subCommands.add(new ModuleLoadCommand(plugin));
    subCommands.add(new ModuleReloadCommand(plugin));
    subCommands.add(new ModuleUnloadCommand(plugin));
  }

  @Override
  public String getName() {
    return "tnemodule";
  }

  @Override
  public String[] getAliases() {
    return new String[] {
        "tnem"
    };
  }

  @Override
  public String getNode() {
    return "tne.module";
  }

  @Override
  public boolean console() {
    return true;
  }
}