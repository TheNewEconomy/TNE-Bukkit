package net.tnemc.core.commands.admin;

import com.github.tnerevival.commands.TNECommand;
import net.tnemc.core.TNE;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;

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
 * Created by Daniel on 2/7/2018.
 */
public class AdminUploadCommand extends TNECommand {

  public AdminUploadCommand(TNE plugin) {
    super(plugin);
  }

  @Override
  public String getName() {
    return "upload";
  }

  @Override
  public String[] getAliases() {
    return new String[] {
        "up"
    };
  }

  @Override
  public String getNode() {
    return "tne.admin.upload";
  }

  @Override
  public boolean console() {
    return true;
  }

  @Override
  public String getHelp() {
    return "Messages.Commands.Admin.Upload";
  }

  @Override
  public boolean execute(CommandSender sender, String command, String[] arguments) {

    boolean succeeded = true;
    String serverLog = "";
    String debugLog = "";
    LocalDateTime now = LocalDateTime.now();
    int year = now.getYear();
    int month = now.getMonthValue();
    int day = now.getDayOfMonth();

    final String base = "https://pastebin.com/api/api_post.php";
    final String devKey = "b73948f0d7d8cb449085dc90168a5deb";
    final String format = "txt";
    final String expiration = "N";
    final String privateCode = "1";

    StringBuilder content = new StringBuilder();
    String name = "";
    String parameters = "api_option=paste&api_paste_private=" + privateCode +
                        "&api_expire_date=" + expiration +
                        "&api_dev_key=" + devKey;

    name = TNE.instance().getServerName() + "-" + year + "-" + month + "-" + day + "-server.txt";
    try (BufferedReader br = new BufferedReader(new FileReader(new File(TNE.instance().getDataFolder(),"../../logs/latest.log")))){
      String line;
      while ((line = br.readLine()) != null) {
        content.append(line + System.getProperty("line.separator"));
      }
    } catch (Exception e) {
      TNE.debug(e);
    }
    serverLog = sendPostRequest(base, parameters + "&api_paste_name=" + name + "&api_paste_code=" + content.toString());

    content = new StringBuilder();
    name = TNE.instance().getServerName() + "-" + year + "-" + month + "-" + day + "-debug.txt";
    try (BufferedReader br = new BufferedReader(new FileReader(new File(TNE.instance().getDataFolder(), "debug/debug-" + year + "-" + month + "-" + day + ".txt")))){
      String line;
      while ((line = br.readLine()) != null) {
        content.append(line + System.getProperty("line.separator"));
      }
    } catch (Exception e) {
      TNE.debug(e);
    }
    debugLog = sendPostRequest(base, parameters + "&api_paste_name=" + name + "&api_paste_code=" + content.toString());

    succeeded = serverLog.contains("pastebin.com") || debugLog.contains("pastebin.com");

    if(succeeded) {
      if(!serverLog.contains("pastebin.com")) serverLog = "Unsuccessful";
      if(!debugLog.contains("pastebin.com")) debugLog = "Unsuccessful";
      sender.sendMessage(ChatColor.WHITE + "Successfully uploaded files! Server Log: " + serverLog + " Debug Log: " + debugLog);
      return true;
    }
    sender.sendMessage(ChatColor.RED + "Something went wrong while uploading the files to pastebin!");
    return false;
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
      System.out.println("POST Response Code :: " + responseCode);

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
    System.out.println("POST Final Response: " + builder.toString());
    return builder.toString();
  }
}