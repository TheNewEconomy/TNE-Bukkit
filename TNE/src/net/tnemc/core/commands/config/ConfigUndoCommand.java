package net.tnemc.core.commands.config;

import com.github.tnerevival.commands.TNECommand;
import com.github.tnerevival.core.Message;
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
 * Created by Daniel on 7/10/2017.
 */
public class ConfigUndoCommand extends TNECommand {

  public ConfigUndoCommand(TNE plugin) {
    super(plugin);
  }

  @Override
  public String getName() {
    return "undo";
  }

  @Override
  public String[] getAliases() {
    return new String[] {
        "u"
    };
  }

  @Override
  public String getNode() {
    return "tne.config.undo";
  }

  @Override
  public boolean console() {
    return true;
  }

  @Override
  public String getHelp() {
    return "Messages.Commands.Config.Undo";
  }

  @Override
  public boolean execute(CommandSender sender, String command, String[] arguments) {
    String configuration = (arguments.length >= 1)? arguments[0] : "all";
    String world = WorldFinder.getWorld(sender, WorldVariant.BALANCE);
    if(configuration.equalsIgnoreCase("all")) {
      TNE.configurations().undoAll();
      Message message = new Message("Messages.Configuration.UndoneAll");
      message.translate(world, sender);
      return true;
    }

    if(!TNE.configurations().configurations.containsKey(configuration)) {
      Message message = new Message("Messages.Configuration.InvalidFile");
      message.addVariable("$configuration", configuration);
      message.translate(world, sender);
      return false;
    }

    TNE.configurations().configurations.get(configuration).modified.clear();
    Message message = new Message("Messages.Configuration.Undone");
    message.addVariable("$modified", configuration);
    message.translate(world, sender);
    return true;
  }
}