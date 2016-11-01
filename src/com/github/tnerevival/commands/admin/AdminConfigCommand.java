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
package com.github.tnerevival.commands.admin;

import com.github.tnerevival.TNE;
import com.github.tnerevival.commands.TNECommand;
import com.github.tnerevival.core.Message;
import com.github.tnerevival.utils.MISCUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by creatorfromhell on 11/1/2016.
 **/
public class AdminConfigCommand extends TNECommand {

  public AdminConfigCommand(TNE plugin) {
    super(plugin);
  }

  @Override
  public String getName() {
    return "config";
  }

  @Override
  public String[] getAliases() {
    return new String[0];
  }

  @Override
  public String getNode() {
    return "tne.admin.config";
  }

  @Override
  public boolean console() {
    return true;
  }

  @Override
  public boolean execute(CommandSender sender, String command, String[] arguments) {
    if(arguments.length >= 2) {
      String id = (sender instanceof Player)? MISCUtils.getID(getPlayer(sender)).toString() : "";
      String world = (sender instanceof Player)? MISCUtils.getWorld(getPlayer(sender)) : TNE.instance.defaultWorld;
      String action = (arguments[0].equalsIgnoreCase("set"))? "set" : "get";
      if(TNE.instance.api.hasConfiguration(arguments[1])) {
        Message m = null;
        switch(action) {
          case "set":
            TNE.instance.api.setConfiguration(arguments[1], arguments[2]);
            m = new Message("Messages.Admin.SetConfiguration");
            m.addVariable("$node", arguments[1]);
            m.addVariable("$value", arguments[2] + "");
            break;
          default:
            Object value = TNE.instance.api.getConfiguration(arguments[1], world, id);
            m = new Message("Messages.Admin.Configuration");
            m.addVariable("$node", arguments[1]);
            m.addVariable("$value", value + "");
            break;
        }
        m.translate(world, sender);
        return true;
      }
      Message m = new Message("Messages.Admin.NoConfiguration");
      m.addVariable("$node", arguments[1]);
      m.translate(world, sender);
      return false;
    }
    help(sender);
    return false;
  }

  @Override
  public String getHelp() {
    return "/theneweconomy config <set/get> <node> [value] - Gets or sets the specified configuration node.";
  }
}