package net.tnemc.core.commands.module;

import net.tnemc.core.TNE;
import net.tnemc.core.commands.TNECommand;
import net.tnemc.core.common.Message;
import net.tnemc.core.common.WorldVariant;
import net.tnemc.core.common.account.WorldFinder;
import net.tnemc.core.common.module.ModuleEntry;
import net.tnemc.core.configuration.ConfigurationEntry;
import net.tnemc.core.configuration.utils.FileMgmt;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import static net.tnemc.core.configuration.ConfigurationManager.addConfiguration;
import static net.tnemc.core.configuration.ConfigurationManager.getRootFolder;

/**
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by Daniel on 7/10/2017.
 */
public class ModuleReloadCommand extends TNECommand {

  public ModuleReloadCommand(TNE plugin) {
    super(plugin);
  }

  @Override
  public String getName() {
    return "reload";
  }

  @Override
  public String[] getAliases() {
    return new String[] {
        "r"
    };
  }

  @Override
  public String getNode() {
    return "tne.module.reload";
  }

  @Override
  public boolean console() {
    return true;
  }

  @Override
  public String getHelp() {
    return "Messages.Commands.Module.Reload";
  }

  @Override
  public boolean execute(CommandSender sender, String command, String[] arguments) {
    if(arguments.length >= 1) {
      String moduleName = arguments[0];
      String world = WorldFinder.getWorld(sender, WorldVariant.ACTUAL);
      ModuleEntry module = TNE.instance().loader().getModule(moduleName);
      if(module == null) {
        Message message = new Message("Messages.Module.Invalid");
        message.addVariable("$module", moduleName);
        message.translate(world, sender);
        return false;
      }

      TNE.instance().loader().unload(moduleName);

      boolean loaded = TNE.instance().loader().load(moduleName);
      if(!loaded) {
        Message message = new Message("Messages.Module.Invalid");
        message.addVariable("$module", moduleName);
        message.translate(world, sender);
        return false;
      }

      TNE.loader().getModules().forEach((key, value)->{
        value.getModule().registerConfigurations().forEach((file, nodes)->{
          addConfiguration(new ConfigurationEntry(nodes, new File(getRootFolder() + FileMgmt.fileSeparator() + file), true, value.getInfo().name()));
        });
      });
      if (!net.tnemc.core.configuration.ConfigurationManager.loadSettings(true)){
        TNE.logger().info("Unable to load some module configurations!");
      }
      module.getModule().getCommands().forEach((com)->{
        List<String> accessors = Arrays.asList(com.getAliases());
        accessors.add(com.getName());
        TNE.instance().registerCommand((String[])accessors.toArray(), com);
      });
      module.getModule().enableSave(TNE.saveManager());
      module.getModule().getListeners(TNE.instance()).forEach(listener->{
        Bukkit.getServer().getPluginManager().registerEvents(listener, TNE.instance());
        TNE.debug("Registering Listener");
      });

      Message message = new Message("Messages.Module.Reloaded");
      message.addVariable("$module", moduleName);
      message.addVariable("$author", module.getInfo().author());
      message.addVariable("$version", module.getInfo().version());
      message.translate(world, sender);
      return true;
    }
    help(sender);
    return false;
  }
}