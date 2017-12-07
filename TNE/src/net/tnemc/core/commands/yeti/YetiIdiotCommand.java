package net.tnemc.core.commands.yeti;

import com.github.tnerevival.commands.TNECommand;
import com.github.tnerevival.user.IDFinder;
import net.tnemc.core.TNE;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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
 * Created by Daniel on 10/17/2017.
 */
public class YetiIdiotCommand extends TNECommand {

  public YetiIdiotCommand(TNE plugin) {
    super(plugin);
  }

  @Override
  public String getName() {
    return "idiot";
  }

  @Override
  public String[] getAliases() {
    return new String[0];
  }

  @Override
  public String getNode() {
    return "tne.yeti.idiot";
  }

  @Override
  public boolean console() {
    return true;
  }

  @Override
  public String getHelp() {
    return ChatColor.GREEN + "/yediot" + ChatColor.WHITE + " - Determine if a Yediot is near. http://www.urbandictionary.com/define.php?term=yediot";
  }

  @Override
  public boolean execute(CommandSender sender, String command, String[] arguments) {
    Player player = getPlayer(sender);
    Player yeti = Bukkit.getPlayer(IDFinder.getID("TheNetYeti"));
    Player growlf = Bukkit.getPlayer(IDFinder.getID("growlf"));

    if(player.getDisplayName().equalsIgnoreCase("TheNetYeti")) {
      sender.sendMessage(ChatColor.GREEN + "Congratulations you have found the Yediot.");
      return true;
    }

    if(yeti != null) {
      if(yeti.getLocation().distanceSquared(player.getLocation()) <= 25) {
        sender.sendMessage(ChatColor.WHITE + "There is a Yediot near you.");
        return true;
      }
      sender.sendMessage(ChatColor.WHITE + "There is a Yediot on this server.");
      return true;
    }

    if(growlf != null) {
      if(yeti.getLocation().distanceSquared(player.getLocation()) <= 25) {
        sender.sendMessage(ChatColor.WHITE + "There is a Yediot in disguise near you.");
        return true;
      }
      sender.sendMessage(ChatColor.WHITE + "There is a Yediot in disguise on this server.");
      return true;
    }
    sender.sendMessage(ChatColor.WHITE + "There is no Yediot on this server.");
    return true;
  }
}