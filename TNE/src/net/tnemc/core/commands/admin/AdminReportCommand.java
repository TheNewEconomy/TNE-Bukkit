package net.tnemc.core.commands.admin;

import net.tnemc.commands.core.CommandExecution;
import net.tnemc.commands.core.provider.PlayerProvider;
import net.tnemc.core.TNE;
import net.tnemc.core.common.utils.MISCUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.time.LocalDateTime;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * Created by Daniel on 10/31/2018.
 * <p>
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */
public class AdminReportCommand implements CommandExecution {

  @Override
  public boolean execute(PlayerProvider provider, String label, String[] arguments) {
    CommandSender sender = MISCUtils.getSender(provider);
    boolean succeeded = true;
    String serverLog = "";
    String configLog = "";
    String reportLog = "";
    final LocalDateTime now = LocalDateTime.now();
    final int year = now.getYear();
    final int month = now.getMonthValue();
    final int day = now.getDayOfMonth();
    final int minute = now.getMinute();

    StringBuilder content = new StringBuilder();
    String name = "";

    TNE.instance().getLogger().info("Logging report.");
    name = TNE.instance().getServerName() + "-" + year + "-" + month + "-" + day + "-" + minute + "-server.txt";
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
    name = TNE.instance().getServerName() + "-" + year + "-" + month + "-" + day + "-" + minute + "-config.txt";
    try (BufferedReader br = new BufferedReader(new FileReader(new File(TNE.instance().getDataFolder(),"config.yml")))){
      String line;
      while ((line = br.readLine()) != null) {
        if(line.toLowerCase().contains("mysql") || line.toLowerCase().contains("host") ||
            line.toLowerCase().contains("user") || line.toLowerCase().contains("pass") ||
            line.toLowerCase().contains("port")) continue;
        content.append(line + System.getProperty("line.separator"));
      }
    } catch (Exception e) {
      TNE.debug(e);
    }
    configLog = MISCUtils.pastebinUpload(name, "yaml", content);

    TNE.debug("ConfigLog: " + configLog);
    TNE.debug("serverLog: " + serverLog);

    if(!configLog.contains("pastebin.com")) {
      sender.sendMessage(ChatColor.RED + "Something went wrong while preparing your report.");
      return false;
    }

    content = new StringBuilder();
    name = TNE.instance().getServerName() + "-" + year + "-" + month + "-" + day + "-report.txt";
    content.append("Report Description: " + String.join(" ", arguments) + System.getProperty("line.separator"));
    content.append("TNE Version: " + TNE.instance().getDescription().getVersion() + System.getProperty("line.separator"));
    content.append("TNE Build: " + TNE.build + System.getProperty("line.separator"));
    content.append("Modules Loaded: " + String.join(", ", TNE.loader().getModules().keySet()) + System.getProperty("line.separator"));
    content.append("API: " + Bukkit.getName() + System.getProperty("line.separator"));
    content.append("API Version: " + Bukkit.getVersion() + System.getProperty("line.separator"));
    content.append("Online Mode: " + Bukkit.getOnlineMode() + System.getProperty("line.separator"));
    content.append("config.yml: " + configLog + System.getProperty("line.separator"));
    content.append("latest.log: " + serverLog);

    reportLog = MISCUtils.pastebinUpload(name, "yaml", content);

    succeeded = reportLog.contains("pastebin.com");
    TNE.debug("reportLog: " + reportLog);

    if(succeeded) {
      sender.sendMessage(ChatColor.WHITE + "Report URL: " + reportLog);
      return true;
    }
    sender.sendMessage(ChatColor.RED + "Something went wrong while preparing your report.");
    return false;
  }
}