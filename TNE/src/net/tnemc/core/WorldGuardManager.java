package net.tnemc.core;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.StringFlag;
import com.sk89q.worldguard.protection.managers.RegionManager;
import net.tnemc.core.common.currency.TNECurrency;
import org.bukkit.Location;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * Created by creatorfromhell on 6/4/2019.
 * <p>
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */
public class WorldGuardManager {

  public static final StringFlag currencyFlag = new StringFlag("currency", "WorldCurrency");

  public static void init() {
    WorldGuard.getInstance().getFlagRegistry().register(currencyFlag);
  }

  public static TNECurrency findCurrency(String world, Location location) {
    if(TNE.instance().getServer().getPluginManager().getPlugin("WorldGuard") != null) {
      final RegionManager regionManager = WorldGuard.getInstance().getPlatform().getRegionContainer().get(BukkitAdapter.adapt(location.getWorld()));

      if (regionManager != null) {
        final ApplicableRegionSet set = regionManager.getApplicableRegions(BlockVector3.at(location.getX(), location.getY(), location.getZ()));

        final String value = set.queryValue(null, currencyFlag);

        if (value != null && !value.equalsIgnoreCase(currencyFlag.getDefault())) {
          return TNE.manager().currencyManager().get(world, value);
        }
      }
    }
    return TNE.manager().currencyManager().get(world);
  }

  public static String findCurrencyName(String world, Location location) {
    final RegionManager regionManager = WorldGuard.getInstance().getPlatform().getRegionContainer().get(BukkitAdapter.adapt(location.getWorld()));

    if(regionManager != null) {
      final ApplicableRegionSet set = regionManager.getApplicableRegions(BlockVector3.at(location.getX(), location.getY(), location.getZ()));

      final String value = set.queryValue(null, currencyFlag);

      if(value != null && !value.equalsIgnoreCase(currencyFlag.getDefault())) {
        return value;
      }
    }
    return TNE.manager().currencyManager().get(world).name();
  }
}