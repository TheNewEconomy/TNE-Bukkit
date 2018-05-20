package net.tnemc.core.common.account;

import net.tnemc.core.TNE;
import net.tnemc.core.common.WorldVariant;
import net.tnemc.core.common.api.IDFinder;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
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

    return actualWorld;
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