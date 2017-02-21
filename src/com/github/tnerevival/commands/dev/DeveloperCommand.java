/*
 * The New Economy Minecraft Server Plugin
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.

 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.github.tnerevival.commands.dev;

import com.github.tnerevival.TNE;
import com.github.tnerevival.commands.TNECommand;

/**
 * Created by creatorfromhell on 11/7/2016.
 **/
public class DeveloperCommand extends TNECommand {

  public DeveloperCommand(TNE plugin) {
    super(plugin);
    subCommands.add(new DeveloperAccountsCommand(plugin));
    subCommands.add(new DeveloperBalanceCommand(plugin));
  }

  @Override
  public String getName() {
    return "theneweconomydev";
  }

  @Override
  public String[] getAliases() {
    return new String[] { "tnedev" };
  }

  @Override
  public String getNode() {
    return "";
  }

  @Override
  public boolean console() {
    return false;
  }

  @Override
  public boolean developer() {
    return true;
  };
}