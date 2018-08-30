package net.tnemc.core.commands.yeti;

import net.tnemc.core.TNE;
import net.tnemc.core.commands.TNECommand;
import org.bukkit.command.CommandSender;

/**
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by Daniel on 10/17/2017.
 */
public class YetiCommand extends TNECommand {

  public YetiCommand(TNE plugin) {
    super(plugin);
    subCommands.add(new YetiIdiotCommand(plugin));
  }

  @Override
  public String getName() {
    return "yediot";
  }

  @Override
  public String[] getAliases() {
    return new String[0];
  }

  @Override
  public String getNode() {
    return "";
  }

  @Override
  public boolean console() {
    return false;
  }

  @Override
  public boolean execute(CommandSender sender, String command, String[] arguments) {
    if(arguments.length == 0 || !arguments[0].equalsIgnoreCase("idiot")) {
      arguments = new String[1];
      arguments[0] = "idiot";
    }

    return super.execute(sender, command, arguments);
  }
}
