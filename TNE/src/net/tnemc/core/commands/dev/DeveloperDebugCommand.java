package net.tnemc.core.commands.dev;

import net.tnemc.core.TNE;
import net.tnemc.core.commands.TNECommand;
import org.bukkit.command.CommandSender;

/**
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by Daniel on 1/27/2018.
 */
public class DeveloperDebugCommand implements CommandExecution {

  public DeveloperDebugCommand(TNE plugin) {
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
    return "";
  }

  @Override
  public boolean console() {
    return false;
  }

  @Override
  public boolean developer() {
    return true;
  }

  @Override
  public String helpLine() {
    return "/tnedev debug <console/log> - Display the configuration, or balance sharing worlds for this world.";
  }

  @Override
  public boolean execute(CommandSender sender, Command command, String label, String[] arguments) {
    if(arguments.length >= 1) {
      boolean console = arguments[0].equalsIgnoreCase("console");

      TNE.consoleDebug = console;
      sender.sendMessage("The debug configuration has been changed to " + arguments[0]);
      return true;
    }
    MISCUtils.help(sender, label, arguments);
    return false;
  }
}