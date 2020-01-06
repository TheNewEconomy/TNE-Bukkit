package net.tnemc.core.commands.admin;

import net.tnemc.commands.core.CommandExecution;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

/**
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by Daniel on 7/10/2017.
 */
public class AdminCaveatsCommand implements CommandExecution {

  @Override
  public boolean execute(CommandSender sender, Command command, String label, String[] arguments) {
    sender.sendMessage("No known caveats at this point.");
    return true;
  }
}