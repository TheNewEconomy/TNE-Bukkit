package net.tnemc.core.commands.admin;

import net.tnemc.core.TNE;
import net.tnemc.core.commands.TNECommand;
import org.bukkit.ChatColor;
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
public class AdminDebugCommand extends TNECommand {

  public AdminDebugCommand(TNE plugin) {
    super(plugin);
  }

  @Override
  public String name() {
    return "debug";
  }

  @Override
  public String[] aliases() {
    return new String[0];
  }

  @Override
  public String node() {
    return "tne.admin.debug";
  }

  @Override
  public boolean console() {
    return true;
  }

  @Override
  public String helpLine() {
    return "Messages.Commands.Admin.Debug";
  }

  @Override
  public boolean execute(CommandSender sender, String command, String[] arguments) {
    TNE.consoleDebug = !TNE.consoleDebug;

    final String status = (TNE.consoleDebug)? "on" : "off";

    sender.sendMessage(ChatColor.WHITE + "TNE Debug Mode has been toggled " + status + ".");
    return true;
  }
}