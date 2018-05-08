package net.tnemc.core.commands.admin;

import com.github.tnerevival.commands.TNECommand;
import net.tnemc.core.TNE;
import net.tnemc.core.common.utils.MISCUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.io.File;

/**
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by Daniel on 2/8/2018.
 */
public class AdminRestoreCommand extends TNECommand {

  public AdminRestoreCommand(TNE plugin) {
    super(plugin);
  }

  @Override
  public String getName() {
    return "restore";
  }

  @Override
  public String[] getAliases() {
    return new String[0];
  }

  @Override
  public String getNode() {
    return "tne.admin.restore";
  }

  @Override
  public boolean console() {
    return true;
  }

  @Override
  public String getHelp() {
    return "Messages.Commands.Admin.Restore";
  }

  @Override
  public boolean execute(CommandSender sender, String command, String[] arguments) {
    File file = new File(TNE.instance().getDataFolder(), "extracted.yml");
    if(file.exists()) {
      Bukkit.getScheduler().runTaskAsynchronously(TNE.instance(), new Runnable() {
        @Override
        public void run() {
          MISCUtils.restore(sender);
        }
      });
      return true;
    }
    sender.sendMessage(ChatColor.RED + "Unable to locate extracted.yml file for restoration.");
    return false;
  }
}