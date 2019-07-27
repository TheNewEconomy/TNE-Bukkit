package net.tnemc.core.commands.module;

import com.github.tnerevival.core.db.SQLDatabase;
import net.tnemc.core.TNE;
import net.tnemc.core.commands.TNECommand;
import net.tnemc.core.common.Message;
import net.tnemc.core.common.WorldVariant;
import net.tnemc.core.common.account.WorldFinder;
import net.tnemc.core.common.module.ModuleWrapper;
import net.tnemc.dbupdater.core.TableManager;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

/**
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by Daniel on 7/10/2017.
 */
public class ModuleLoadCommand extends TNECommand {

  public ModuleLoadCommand(TNE plugin) {
    super(plugin);
  }

  @Override
  public String getName() {
    return "load";
  }

  @Override
  public String[] getAliases() {
    return new String[] {
        "l"
    };
  }

  @Override
  public String getNode() {
    return "tne.module.load";
  }

  @Override
  public boolean console() {
    return true;
  }

  @Override
  public String getHelp() {
    return "Messages.Commands.Module.Load";
  }

  @Override
  public boolean execute(CommandSender sender, String command, String[] arguments) {
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
      TNE.loader().load(moduleName);

      ModuleWrapper module = TNE.loader().getModule(moduleName);

      final String author = module.getInfo().author();
      final String version = module.getInfo().version();

      module.getModule().load(TNE.instance());
      module.getModule().initializeConfigurations();
      module.getModule().loadConfigurations();
      module.getModule().configurations().forEach((configuration, identifier)->{
        TNE.configurations().add(configuration, identifier);
      });

      for(TNECommand com : module.getModule().commands()) {
        List<String> accessors = new ArrayList<>(Arrays.asList(com.getAliases()));
        accessors.add(com.getName());
        TNE.instance().getCommandManager().register(accessors.toArray(new String[accessors.size()]), com);
      }
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

      try {
        Field f = ClassLoader.class.getDeclaredField("classes");
        f.setAccessible(true);

        Vector<Class> classes =  (Vector<Class>) f.get(TNE.loader().getModule(moduleName).getLoader());
        for(Class clazz : classes) {
          System.out.println("Loaded: " + clazz.getName());
        }
      } catch (Exception e) {
        e.printStackTrace();
      }

      Message message = new Message("Messages.Module.Loaded");
      message.addVariable("$module", moduleName);
      message.addVariable("$author", author);
      message.addVariable("$version", version);
      message.translate(world, sender);
      return true;
    }
    help(sender);
    return false;
  }
}