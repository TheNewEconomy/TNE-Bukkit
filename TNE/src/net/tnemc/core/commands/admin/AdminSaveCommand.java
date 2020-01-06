package net.tnemc.core.commands.admin;

import net.tnemc.core.TNE;
import net.tnemc.core.commands.TNECommand;
import org.bukkit.command.CommandSender;

import java.sql.SQLException;

/**
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by Daniel on 7/10/2017.
 */
public class AdminSaveCommand implements CommandExecution {

  public AdminSaveCommand(TNE plugin) {
    super(plugin);
  }

  @Override
  public String name() {
    return "save";
  }

  @Override
  public String[] aliases() {
    return new String[0];
  }

  @Override
  public String node() {
    return "tne.admin.save";
  }

  @Override
  public boolean console() {
    return true;
  }

  @Override
  public String helpLine() {
    return "Messages.Commands.Admin.Save";
  }

  @Override
  public boolean execute(CommandSender sender, Command command, String label, String[] arguments) {
    try {
      TNE.saveManager().save();
    } catch (SQLException e) {
      TNE.debug(e);
    }
    sender.sendMessage("Successfully saved all TNE Data!");
    return true;
  }
}