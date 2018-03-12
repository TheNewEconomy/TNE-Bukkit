package net.tnemc.core.commands.module;

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
 * Created by Daniel on 7/27/2017.
 */
public class ModuleListCommand extends TNECommand {

  public ModuleListCommand(TNE plugin) {
    super(plugin);
  }

  @Override
  public String getName() {
    return "list";
  }

  @Override
  public String[] getAliases() {
    return new String[] {
        "i"
    };
  }

  @Override
  public String getNode() {
    return "tne.module.list";
  }

  @Override
  public boolean console() {
    return true;
  }

  @Override
  public String getHelp() {
    return "Messages.Commands.Module.List";
  }

  @Override
  public boolean execute(CommandSender sender, String command, String[] arguments) {

    StringBuilder modules = new StringBuilder();
    TNE.loader().getModules().forEach((key, value)->{
      if(modules.length() > 0) modules.append(", ");
      modules.append(value.getInfo().name());
    });

    Message message = new Message("Messages.Module.List");
    message.addVariable("$modules", modules.toString());
    message.translate(WorldFinder.getWorld(sender, WorldVariant.ACTUAL), sender);
    return true;
  }
}