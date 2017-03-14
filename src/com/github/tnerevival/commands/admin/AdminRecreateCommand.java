package com.github.tnerevival.commands.admin;

import com.github.tnerevival.TNE;
import com.github.tnerevival.commands.TNECommand;
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
 * Created by creatorfromhell on 10/23/2016.
 */
public class AdminRecreateCommand extends TNECommand {

  public AdminRecreateCommand(TNE plugin) {
    super(plugin);
  }

  @Override
  public String getName() {
    return "recreate";
  }

  @Override
  public String[] getAliases() {
    return new String[0];
  }

  @Override
  public String getNode() {
    return "tne.admin.recreate";
  }

  @Override
  public boolean console() {
    return true;
  }

  @Override
  public boolean execute(CommandSender sender, String command, String[] arguments) {
    if(plugin.saveManager.type.equalsIgnoreCase("flatfile")) {
      sender.sendMessage("Cannot recreate tables for database type: flatfile.");
      return false;
    }
    sender.sendMessage("Attempting to recreate database tables...");
    TNE.instance().saveManager.recreate();
    return true;
  }

  @Override
  public String getHelp() {
    return "/tne recreate - Attempts to recreate database tables..";
  }
}