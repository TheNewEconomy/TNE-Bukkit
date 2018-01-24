package net.tnemc.core.common.account;

import com.github.tnerevival.user.IDFinder;
import net.tnemc.core.TNE;
import net.tnemc.core.common.WorldVariant;
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

  public static String getWorld(Player player, WorldVariant variant) {
    TNE.debug("=====START WorldFinder.getWorld =====");
    if(player == null) {
      TNE.debug("Player is null");
      return TNE.instance().defaultWorld;
    }
    TNE.debug("=====END WorldFinder.getWorld =====");
    if(!TNE.configurations().getBoolean("Core.Multiworld")) return TNE.instance().defaultWorld;
    String actualWorld = player.getWorld().getName();

    if(variant.equals(WorldVariant.BALANCE)) return TNE.instance().getWorldManager(actualWorld).getBalanceWorld();
    if(variant.equals(WorldVariant.CONFIGURATION)) return TNE.instance().getWorldManager(actualWorld).getConfigurationWorld();

    return player.getWorld().getName();
  }

  public static String getWorld(CommandSender sender, WorldVariant variant) {
    if(!(sender instanceof Player)) return TNE.instance().defaultWorld;
    return getWorld((Player)sender, variant);
  }

  public static String getWorld(String identifier, WorldVariant variant) {
    return getWorld(IDFinder.getPlayer(identifier), variant);
  }

  public static String getWorld(UUID id, WorldVariant variant) {
    return getWorld(id.toString(), variant);
  }

  public static String getBalanceWorld(Player player) {
    return getWorld(player, WorldVariant.BALANCE);
  }

  public static String getBalanceWorld(String identifier) {
    return getBalanceWorld(IDFinder.getPlayer(identifier));
  }

  public static String getConfigurationWorld(Player player) {
    return getWorld(player, WorldVariant.CONFIGURATION);
  }

  public static String getConfigurationWorld(String identifier) {
    return getConfigurationWorld(IDFinder.getPlayer(identifier));
  }
}