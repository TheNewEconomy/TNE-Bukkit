package net.tnemc.core.commands.admin;

import com.github.tnerevival.commands.TNECommand;
import net.tnemc.core.TNE;
import net.tnemc.core.common.utils.MISCUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.time.LocalDateTime;

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

    StringBuilder content = new StringBuilder();
    String name = "";

    name = TNE.instance().getServerName() + "-" + year + "-" + month + "-" + day + "-server.txt";
    try (BufferedReader br = new BufferedReader(new FileReader(new File(TNE.instance().getDataFolder(),"../../logs/latest.log")))){
      String line;
      while ((line = br.readLine()) != null) {
        content.append(line + System.getProperty("line.separator"));
      }
    } catch (Exception e) {
      TNE.debug(e);
    }
    serverLog = MISCUtils.pastebinUpload(name, content);

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
    debugLog = MISCUtils.pastebinUpload(name, content);

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
}