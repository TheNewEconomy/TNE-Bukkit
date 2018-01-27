package net.tnemc.core.commands.language;

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
 * Created by Daniel on 1/27/2018.
 */
public class LanguageCommand extends TNECommand {

  public LanguageCommand(TNE plugin) {
    super(plugin);
    subCommands.add(new LanguageCurrentCommand(plugin));
    subCommands.add(new LanguageListCommand(plugin));
    subCommands.add(new LanguageSetCommand(plugin));
  }

  @Override
  public String getName() {
    return "language";
  }

  @Override
  public String[] getAliases() {
    return new String[] {
        "lang"
    };
  }

  @Override
  public String getNode() {
    return "tne.language";
  }

  @Override
  public boolean console() {
    return false;
  }
}