package net.tnemc.core.common.utils;

import com.github.tnerevival.user.IDFinder;
import net.tnemc.core.TNE;
import net.tnemc.core.common.WorldVariant;
import net.tnemc.core.common.account.WorldFinder;
import org.bukkit.Bukkit;

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
 * Created by creatorfromhell on 06/30/2017.
 */
public class MISCUtils {

  //Minecraft Version Utils

  /**
   * @return Whether the bukkit in use is for MC >= 1.8
   */
  public static boolean isOneEight() {
    return Bukkit.getVersion().contains("1.8") || isOneNine() || isOneTen() || isOneEleven() || isOneTwelve();
  }

  /**
   * @return Whether the bukkit in use is for MC >= 1.9
   */
  public static boolean isOneNine() {
    return Bukkit.getVersion().contains("1.9") || isOneTen() || isOneEleven() || isOneTwelve();
  }

  /**
   * @return Whether the bukkit in use is for MC >= 1.10
   */
  public static boolean isOneTen() {
    return Bukkit.getVersion().contains("1.10") || isOneEleven() || isOneTwelve();
  }

  /**
   * @return Whether the bukkit in use is for MC >= 1.11
   */
  public static boolean isOneEleven() {
    return Bukkit.getVersion().contains("1.11") || isOneTwelve();
  }

  /**
   * @return Whether the bukkit in use is for MC >= 1.12
   */
  public static boolean isOneTwelve() {
    return Bukkit.getVersion().contains("1.12");
  }

  public static Boolean isBoolean(String value) {
    return value.equalsIgnoreCase(String.valueOf(true)) || value.equalsIgnoreCase(String.valueOf(false));
  }

  public static Boolean isOnline(UUID id) {
   return IDFinder.getPlayer(id.toString()) != null;
  }

  public static Boolean isOnline(UUID id, String world) {
    if(IDFinder.getPlayer(id.toString()) != null) {
      return WorldFinder.getWorld(id, WorldVariant.BALANCE).equalsIgnoreCase(world);
    }
    return false;
  }

  public static Boolean isDouble(String value, String world) {
    try {
      TNE.debug("MISCUtils.isDouble(" + value + "," + world + ")");
      Double.valueOf(value.replace(TNE.instance().api().getString("Core.currency.Decimal", world), "."));
      TNE.debug("Double confirmed");
      return true;
    } catch(Exception e) {
      TNE.debug("Double denied");
      return false;
    }
  }

  public static Boolean isInteger(String value) {
    try {
      Integer.valueOf(value);
      return true;
    } catch(Exception e) {
      return false;
    }
  }
}