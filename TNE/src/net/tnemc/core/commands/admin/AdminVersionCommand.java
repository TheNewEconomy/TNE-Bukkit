package net.tnemc.core.commands.admin;

import net.tnemc.core.TNE;
import net.tnemc.core.commands.TNECommand;
import org.bukkit.command.CommandSender;

/**
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by Daniel on 7/10/2017.
 */
public class AdminVersionCommand extends TNECommand {

  public AdminVersionCommand(TNE plugin) {
    super(plugin);
  }

  @Override
  public String name() {
    return "version";
  }

  @Override
  public String[] aliases() {
    return new String[] {
        "v"
    };
  }

  @Override
  public String node() {
    return "tne.admin.version";
  }

  @Override
  public boolean console() {
    return true;
  }

  @Override
  public String helpLine() {
    return "Messages.Commands.Admin.Version";
  }

  @Override
  public boolean execute(CommandSender sender, String command, String[] arguments) {
    sender.sendMessage("You're currently running TNE version " + TNE.instance().getDescription().getVersion());
    return true;
  }
}