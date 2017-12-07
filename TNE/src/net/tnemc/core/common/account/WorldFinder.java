package net.tnemc.core.common.account;

import com.github.tnerevival.user.IDFinder;
import net.tnemc.core.TNE;
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
 * Created by Daniel on 7/8/2017.
 */
public class WorldFinder {

  public static String getWorld(Player player) {
    TNE.debug("=====START WorldFinder.getWorld =====");
    if(player == null) {
      TNE.debug("Player is null");
      return TNE.instance().defaultWorld;
    }
    TNE.debug("=====END WorldFinder.getWorld =====");
    return player.getWorld().getName();
  }

  public static String getWorld(CommandSender sender) {
    if(!(sender instanceof Player)) return TNE.instance().defaultWorld;
    return getWorld((Player)sender);
  }

  public static String getWorld(String identifier) {
    return getWorld(IDFinder.getPlayer(identifier));
  }

  public static String getWorld(UUID id) {
    return getWorld(id.toString());
  }

  public static String getBalanceWorld(Player player) {
    return TNE.instance().getWorldManager(getWorld(player)).getBalanceWorld();
  }

  public static String getBalanceWorld(String identifier) {
    return getBalanceWorld(IDFinder.getPlayer(identifier));
  }

  public static String getConfigurationWorld(Player player) {
    return TNE.instance().getWorldManager(getWorld(player)).getConfigurationWorld();
  }

  public static String getConfigurationWorld(String identifier) {
    return getConfigurationWorld(IDFinder.getPlayer(identifier));
  }
}