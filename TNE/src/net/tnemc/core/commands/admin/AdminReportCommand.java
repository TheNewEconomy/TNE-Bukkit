package net.tnemc.core.commands.admin;

import net.tnemc.core.TNE;
import net.tnemc.core.commands.TNECommand;
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
public class AdminReportCommand extends TNECommand {

  public AdminReportCommand(TNE plugin) {
    super(plugin);
  }

  @Override
  public String getName() {
    return "report";
  }

  @Override
  public String[] getAliases() {
    return new String[0];
  }

  @Override
  public String getNode() {
    return "tne.admin.report";
  }

  @Override
  public boolean console() {
    return true;
  }

  @Override
  public String getHelp() {
    return "Messages.Commands.Admin.Report";
  }

  @Override
  public boolean execute(CommandSender sender, String command, String[] arguments) {
    boolean succeeded = true;
    String serverLog = "";
    String configLog = "";
    String reportLog = "";
    final LocalDateTime now = LocalDateTime.now();
    final int year = now.getYear();
    final int month = now.getMonthValue();
    final int day = now.getDayOfMonth();

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

    name = TNE.instance().getServerName() + "-" + year + "-" + month + "-" + day + "-config.txt";
    try (BufferedReader br = new BufferedReader(new FileReader(new File(TNE.instance().getDataFolder(),"config.yml")))){
      String line;
      while ((line = br.readLine()) != null) {
        if(line.contains("Database")) continue;
        content.append(line + System.getProperty("line.separator"));
      }
    } catch (Exception e) {
      TNE.debug(e);
    }
    configLog = MISCUtils.pastebinUpload(name, content);

    if(!serverLog.contains("pastebin.com") ||
       !configLog.contains("pastebin.com")) {
      sender.sendMessage(ChatColor.RED + "Something went wrong while preparing your report.");
      return false;
    }

    content = new StringBuilder();
    name = TNE.instance().getServerName() + "-" + year + "-" + month + "-" + day + "-report.txt";
    content.append("Report Description: " + String.join(" ", arguments));
    content.append("TNE Version: " + TNE.instance().getDescription().getVersion());
    content.append("TNE Build: " + TNE.build);
    content.append("Modules Loaded: " + String.join(", ", TNE.loader().getModules().keySet()));
    content.append("API: " + Bukkit.getName());
    content.append("API Version: " + Bukkit.getVersion());
    content.append("Online Mode: " + Bukkit.getOnlineMode());
    content.append("config.yml: " + configLog);
    content.append("latest.log: " + serverLog);

    reportLog = MISCUtils.pastebinUpload(name, content);

    succeeded = reportLog.contains("pastebin.com");

    if(succeeded) {
      sender.sendMessage(ChatColor.WHITE + "Report URL: " + reportLog);
      return true;
    }
    sender.sendMessage(ChatColor.RED + "Something went wrong while preparing your report.");
    return false;
  }
}