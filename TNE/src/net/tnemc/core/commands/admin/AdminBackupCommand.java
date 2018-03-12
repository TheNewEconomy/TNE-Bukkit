package net.tnemc.core.commands.admin;

import com.github.tnerevival.commands.TNECommand;
import net.tnemc.core.TNE;
import org.bukkit.command.CommandSender;

/**
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by Daniel on 7/10/2017.
 */
public class AdminBackupCommand extends TNECommand {

  public AdminBackupCommand(TNE plugin) {
    super(plugin);
  }

  @Override
  public String getName() {
    return "backup";
  }

  @Override
  public String[] getAliases() {
    return new String[0];
  }

  @Override
  public String getNode() {
    return "tne.admin.backup";
  }

  @Override
  public boolean console() {
    return true;
  }

  @Override
  public String getHelp() {
    return "Messages.Commands.Admin.Backup";
  }

  @Override
  public boolean execute(CommandSender sender, String command, String[] arguments) {
    if(TNE.saveManager().getTNEManager().getTNEProvider().backupData()) {
      sender.sendMessage("Successfully backed up all data.");
      return true;
    }
    sender.sendMessage("Something went wrong while trying to backup data!");
    return false;
  }
}