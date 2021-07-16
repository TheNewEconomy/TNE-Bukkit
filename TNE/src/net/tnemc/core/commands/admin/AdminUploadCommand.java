package net.tnemc.core.commands.admin;

import net.tnemc.commands.core.CommandExecution;
import net.tnemc.commands.core.provider.PlayerProvider;
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
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by Daniel on 2/7/2018.
 */
public class AdminUploadCommand implements CommandExecution {

  @Override
  public boolean execute(PlayerProvider provider, String label, String[] arguments) {
    CommandSender sender = MISCUtils.getSender(provider);

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
    serverLog = MISCUtils.pastebinUpload(name, "yaml", content);

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
    debugLog = MISCUtils.pastebinUpload(name, "yaml", content);

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