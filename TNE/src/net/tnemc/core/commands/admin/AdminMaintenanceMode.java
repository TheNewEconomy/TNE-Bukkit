package net.tnemc.core.commands.admin;

import net.tnemc.core.TNE;
import net.tnemc.core.commands.TNECommand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * Created by Daniel on 11/26/2018.
 * <p>
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */
public class AdminMaintenanceMode extends TNECommand {

  public AdminMaintenanceMode(TNE plugin) {
    super(plugin);
  }

  @Override
  public String getName() {
    return "maintenance";
  }

  @Override
  public String[] getAliases() {
    return new String[0];
  }

  @Override
  public String getNode() {
    return "tne.admin.maintenance";
  }

  @Override
  public boolean console() {
    return true;
  }

  @Override
  public String getHelp() {
    return "Messages.Commands.Admin.Maintenance";
  }

  @Override
  public boolean execute(CommandSender sender, String command, String[] arguments) {
    TNE.maintenance = !TNE.maintenance;

    final String status = (TNE.maintenance)? "on" : "off";

    sender.sendMessage(ChatColor.WHITE + "TNE Maintenance Mode has been toggled " + status + ".");

    if(TNE.maintenance) {
      Bukkit.broadcastMessage(ChatColor.RED + "Economy has entered maintenance mode. Economy commands have been disabled to prevent balance rollback.");
    }
    return true;
  }
}