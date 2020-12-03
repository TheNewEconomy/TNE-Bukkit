package net.tnemc.core.commands.admin;

import net.tnemc.commands.core.CommandExecution;
import net.tnemc.core.TNE;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * Created by Daniel on 11/12/2018.
 * <p>
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */
public class AdminDebugCommand implements CommandExecution {

  @Override
  public boolean execute(CommandSender sender, Command command, String label, String[] arguments) {
    TNE.instance().debugMode = !TNE.instance().debugMode;

    final String status = (TNE.instance().debugMode)? "on" : "off";

    sender.sendMessage(ChatColor.WHITE + "TNE Debug Mode has been toggled " + status + ".");
    return true;
  }
}
