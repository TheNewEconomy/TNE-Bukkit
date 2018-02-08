package net.tnemc.core.common.utils;

import com.github.tnerevival.user.IDFinder;
import net.tnemc.core.TNE;
import net.tnemc.core.common.WorldVariant;
import net.tnemc.core.common.account.WorldFinder;
import org.bukkit.Bukkit;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;

import static com.google.common.net.HttpHeaders.USER_AGENT;

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

  public static String pastebinUpload(String name, StringBuilder content) {
    final String base = "https://pastebin.com/api/api_post.php";
    final String devKey = "b73948f0d7d8cb449085dc90168a5deb";
    final String format = "txt";
    final String expiration = "N";
    final String privateCode = "1";

    String parameters = "api_option=paste&api_paste_private=" + privateCode +
        "&api_expire_date=" + expiration +
        "&api_dev_key=" + devKey;

    return sendPostRequest(base, parameters + "&api_paste_name=" + name + "&api_paste_code=" + content.toString());
  }

  private static String sendPostRequest(String URL, String parameters) {
    StringBuilder builder = new StringBuilder();
    try {
      HttpURLConnection connection = (HttpURLConnection) new URL(URL).openConnection();
      connection.setRequestMethod("POST");
      connection.setRequestProperty("User-Agent", USER_AGENT);
      connection.setDoOutput(true);
      OutputStream os = connection.getOutputStream();
      os.write(parameters.getBytes());
      os.flush();
      os.close();

      int responseCode = connection.getResponseCode();

      if(responseCode == HttpURLConnection.HTTP_OK) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String response;
        while ((response = reader.readLine()) != null) {
          builder.append(response);
        }
        reader.close();
      }
    } catch (Exception e) {
      TNE.debug(e);
    }
    return builder.toString();
  }
}