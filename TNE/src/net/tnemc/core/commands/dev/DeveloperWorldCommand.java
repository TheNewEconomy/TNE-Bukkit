package net.tnemc.core.commands.dev;

import com.github.tnerevival.commands.TNECommand;
import net.tnemc.core.TNE;
import net.tnemc.core.common.WorldVariant;
import net.tnemc.core.common.account.WorldFinder;
import org.bukkit.command.CommandSender;

/**
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by Daniel on 1/27/2018.
 */
public class DeveloperWorldCommand extends TNECommand {

  public DeveloperWorldCommand(TNE plugin) {
    super(plugin);
  }

  @Override
  public String getName() {
    return "world";
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
  public boolean developer() {
    return true;
  }

  @Override
  public String getHelp() {
    return "/tnedev world <configuration/balance> - Display the configuration, or balance sharing worlds for this world.";
  }

  @Override
  public boolean execute(CommandSender sender, String command, String[] arguments) {
    if(arguments.length >= 1) {
      boolean balance = arguments[0].equalsIgnoreCase("balance");
      String world = WorldFinder.getWorld(sender, WorldVariant.ACTUAL);

      String outputworld = world;

      if(balance) {
        outputworld = TNE.instance().getWorldManager(world).getBalanceWorld();
      } else {
        outputworld = TNE.instance().getWorldManager(world).getConfigurationWorld();
      }
      sender.sendMessage("The configured sharing world for the options specified is " + outputworld);
      return true;
    }
    help(sender);
    return false;
  }
}