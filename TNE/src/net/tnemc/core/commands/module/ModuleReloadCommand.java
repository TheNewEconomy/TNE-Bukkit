package net.tnemc.core.commands.module;

import net.tnemc.commands.core.CommandExecution;
import net.tnemc.commands.core.CommandsHandler;
import net.tnemc.commands.core.provider.PlayerProvider;
import net.tnemc.core.TNE;
import net.tnemc.core.common.Message;
import net.tnemc.core.common.WorldVariant;
import net.tnemc.core.common.account.WorldFinder;
import net.tnemc.core.common.module.ModuleWrapper;
import net.tnemc.core.common.utils.MISCUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

/**
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by Daniel on 7/10/2017.
 */
public class ModuleReloadCommand implements CommandExecution {

  @Override
  public boolean execute(PlayerProvider provider, String label, String[] arguments) {
    CommandSender sender = MISCUtils.getSender(provider);
    if(arguments.length >= 1) {
      String moduleName = arguments[0];
      String world = WorldFinder.getWorld(sender, WorldVariant.ACTUAL);
      ModuleWrapper module = TNE.loader().getModule(moduleName);
      if(module == null) {
        Message message = new Message("Messages.Module.Invalid");
        message.addVariable("$module", moduleName);
        message.translate(world, sender);
        return false;
      }

      TNE.loader().unload(moduleName);

      boolean loaded = TNE.loader().load(moduleName);
      if(!loaded) {
        Message message = new Message("Messages.Module.Invalid");
        message.addVariable("$module", moduleName);
        message.translate(world, sender);
        return false;
      }


      module.getModule().initializeConfigurations();
      module.getModule().loadConfigurations();
      module.getModule().configurations().forEach((configuration, identifier)->{
        TNE.configurations().add(configuration, identifier);
      });

      //Load Module Commands
      module.getModule().commands().forEach((com)-> CommandsHandler.manager().register(com.getIdentifiers(true), com));

      //Load Module Executors
      module.getModule().commandExecutors().forEach((name, executor)-> CommandsHandler.instance().addExecutor(name, executor));

      module.getModule().enableSave(TNE.saveManager());
      module.getModule().listeners(TNE.instance()).forEach(listener->{
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
    MISCUtils.help(sender, label, arguments);
    return false;
  }
}