package net.tnemc.core.commands.admin;

import net.tnemc.core.TNE;
import net.tnemc.core.commands.TNECommand;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * Created by creatorfromhell on 12/25/2019.
 * <p>
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */
public class AdminReloadCommand extends TNECommand {

  public AdminReloadCommand(TNE plugin) {
    super(plugin);
  }

  @Override
  public String name() {
    return "reload";
  }

  @Override
  public String[] aliases() {
    return new String[0];
  }

  @Override
  public String node() {
    return "tne.admin.reload";
  }

  @Override
  public boolean console() {
    return true;
  }

  @Override
  public String helpLine() {
    return "Messages.Commands.Admin.Reload";
  }

  @Override
  public boolean execute(CommandSender sender, String command, String[] arguments) {
    String id = (arguments.length == 1)? arguments[0] : "all";
    if(TNE.configurations().reload(id)) {
      sender.sendMessage(ChatColor.WHITE + "Successfully reload configuration with id of: " + id + ".");
      return true;
    }
    help(sender);
    return false;
  }
}