package net.tnemc.core.commands.module;

import com.github.tnerevival.commands.TNECommand;
import com.github.tnerevival.core.Message;
import net.tnemc.core.TNE;
import net.tnemc.core.common.WorldVariant;
import net.tnemc.core.common.account.WorldFinder;
import net.tnemc.core.common.module.ModuleEntry;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.List;

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
      ModuleEntry module = TNE.loader().getModule(moduleName);

      String author = module.getInfo().author();
      String version = module.getInfo().version();

      module.getModule().initializeConfigurations();
      module.getModule().loadConfigurations();
      module.getModule().getMainConfigurations().forEach((node, defaultValue)->{
        TNE.instance().main().configurations.put(node, defaultValue);
      });
      module.getModule().getMessages().forEach((message, defaultValue)->{
        TNE.instance().messages().configurations.put(message, defaultValue);
      });
      module.getModule().getConfigurations().forEach((configuration, identifier)->{
        TNE.configurations().add(configuration, identifier);
      });
      module.getModule().getCommands().forEach((com)->{
        List<String> accessors = Arrays.asList(com.getAliases());
        accessors.add(com.getName());
        TNE.instance().registerCommand((String[])accessors.toArray(), com);
      });
      module.getModule().getListeners(TNE.instance()).forEach(listener->{
        Bukkit.getServer().getPluginManager().registerEvents(listener, TNE.instance());
        TNE.debug("Registering Listener");
      });

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