package net.tnemc.core.commands.module;

import com.github.tnerevival.core.db.SQLDatabase;
import net.tnemc.commands.core.CommandExecution;
import net.tnemc.commands.core.CommandsHandler;
import net.tnemc.commands.core.provider.PlayerProvider;
import net.tnemc.core.TNE;
import net.tnemc.core.common.Message;
import net.tnemc.core.common.WorldVariant;
import net.tnemc.core.common.account.WorldFinder;
import net.tnemc.core.common.module.ModuleWrapper;
import net.tnemc.core.common.utils.MISCUtils;
import net.tnemc.dbupdater.core.TableManager;
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
public class ModuleLoadCommand implements CommandExecution {

  @Override
  public boolean execute(PlayerProvider provider, String label, String[] arguments) {
    CommandSender sender = MISCUtils.getSender(provider);
    if(arguments.length >= 1) {
      String moduleName = arguments[0];
      String world = WorldFinder.getWorld(sender, WorldVariant.ACTUAL);
      boolean loaded = TNE.loader().load(moduleName);

      if(!loaded) {
        Message message = new Message("Messages.Module.Invalid");
        message.addVariable("$module", moduleName);
        message.translate(world, sender);
        return false;
      }

      ModuleWrapper module = TNE.loader().getModule(moduleName);

      final String author = module.getInfo().author();
      final String version = module.getInfo().version();

      module.getModule().load(TNE.instance());
      module.getModule().initializeConfigurations();
      module.getModule().loadConfigurations();
      module.getModule().configurations().forEach((configuration, identifier)->{
        TNE.configurations().add(configuration, identifier);
      });


      //Load Module Commands
      module.getModule().commands().forEach((com)-> CommandsHandler.manager().register(com.getIdentifiers(true), com));

      //Load Module Executors
      module.getModule().commandExecutors().forEach((name, executor)-> CommandsHandler.instance().addExecutor(name, executor));

      module.getModule().listeners(TNE.instance()).forEach(listener->{
        Bukkit.getServer().getPluginManager().registerEvents(listener, TNE.instance());
        TNE.debug("Registering Listener");
      });
      final String tablesFile = module.getModule().tablesFile();

      if(!tablesFile.trim().equalsIgnoreCase("")) {

        SQLDatabase.open();
        TableManager manager = new TableManager(TNE.saveManager().getTNEManager().getFormat().toLowerCase(), TNE.saveManager().getTNEManager().getPrefix());
        manager.generateQueriesAndRun(SQLDatabase.getDb().getConnection(), module.getModule().getResource(tablesFile));
        SQLDatabase.close();
      }

      TNE.loader().getModules().put(module.name(), module);
      module = null;

      Message message = new Message("Messages.Module.Loaded");
      message.addVariable("$module", moduleName);
      message.addVariable("$author", author);
      message.addVariable("$version", version);
      message.translate(world, sender);
      return true;
    }
    MISCUtils.help(sender, label, arguments);
    return false;
  }
}