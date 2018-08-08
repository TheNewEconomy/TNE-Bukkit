package net.tnemc.core.commands.module;

import net.tnemc.core.TNE;
import net.tnemc.core.commands.TNECommand;
import net.tnemc.core.common.Message;
import net.tnemc.core.common.WorldVariant;
import net.tnemc.core.common.account.WorldFinder;
import net.tnemc.core.common.module.ModuleLoader;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * Created by Daniel on 8/8/2018.
 * <p>
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */
public class ModuleDownloadCommand extends TNECommand {

  public ModuleDownloadCommand(TNE plugin) {
    super(plugin);
  }

  @Override
  public String getName() {
    return "download";
  }

  @Override
  public String[] getAliases() {
    return new String[] {
        "dl"
    };
  }

  @Override
  public String getNode() {
    return "tne.module.download";
  }

  @Override
  public boolean console() {
    return true;
  }

  @Override
  public String getHelp() {
    return "Messages.Commands.Module.Download";
  }

  @Override
  public boolean execute(CommandSender sender, String command, String[] arguments) {
    if(arguments.length >= 1) {
      final String moduleName = arguments[0].toLowerCase().trim();
      final String world = WorldFinder.getWorld(sender, WorldVariant.ACTUAL);
      if(!ModuleLoader.modulePaths.containsKey(moduleName)) {
        Message message = new Message("Messages.Module.Invalid");
        message.addVariable("$module", moduleName);
        message.translate(world, sender);
        return false;
      }
      Bukkit.getScheduler().runTaskAsynchronously(TNE.instance(), ()->ModuleLoader.downloadModule(moduleName));
      Message message = new Message("Messages.Module.Downloaded");
      message.addVariable("$module", moduleName);
      message.translate(world, sender);
      return true;
    }
    help(sender);
    return false;
  }
}