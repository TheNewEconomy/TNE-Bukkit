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
 * Created by Daniel on 2/7/2018.
 */
public class AdminBuildCommand extends TNECommand {

  public AdminBuildCommand(TNE plugin) {
    super(plugin);
  }

  @Override
  public String getName() {
    return "build";
  }

  @Override
  public String[] getAliases() {
    return new String[] {
        "b"
    };
  }

  @Override
  public String getNode() {
    return "tne.admin.build";
  }

  @Override
  public boolean console() {
    return true;
  }

  @Override
  public String getHelp() {
    return "Messages.Commands.Admin.Build";
  }

  @Override
  public boolean execute(CommandSender sender, String command, String[] arguments) {
    sender.sendMessage("You're currently running TNE build " + TNE.build);
    return true;
  }
}