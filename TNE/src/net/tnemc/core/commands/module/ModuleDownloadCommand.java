package net.tnemc.core.commands.module;

import net.tnemc.core.TNE;
import net.tnemc.core.commands.TNECommand;
import net.tnemc.core.common.Message;
import net.tnemc.core.common.WorldVariant;
import net.tnemc.core.common.account.WorldFinder;
import net.tnemc.core.common.module.ModuleUpdateChecker;
import net.tnemc.core.common.module.cache.ModuleFile;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.util.Optional;

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
  public String name() {
    return "download";
  }

  @Override
  public String[] aliases() {
    return new String[] {
        "dl"
    };
  }

  @Override
  public String node() {
    return "tne.module.download";
  }

  @Override
  public boolean console() {
    return true;
  }

  @Override
  public String helpLine() {
    return "Messages.Commands.Module.Download";
  }

  @Override
  public boolean execute(CommandSender sender, String command, String[] arguments) {
    if(arguments.length >= 1) {
      final String url = (arguments.length > 1)? arguments[1] : TNE.coreURL;
      final String moduleName = arguments[0].toLowerCase().trim();
      final String world = WorldFinder.getWorld(sender, WorldVariant.ACTUAL);

      Bukkit.getScheduler().runTaskAsynchronously(TNE.instance(), ()->{
        TNE.instance().moduleCache().getModules(url);

        Optional<ModuleFile> module = TNE.instance().moduleCache().getModule(url, moduleName);
        if(!module.isPresent()) {
          Message invalid = new Message("Messages.Module.Invalid");
          invalid.addVariable("$module", moduleName);
          invalid.translate(world, sender);
          return;
        }

        if(!ModuleUpdateChecker.download(module.get().getName(), module.get().getUrl())) {
          Message invalid = new Message("Messages.Module.FailedDownload");
          invalid.addVariable("$module", moduleName);
          invalid.translate(world, sender);
          return;
        }
        Message invalid = new Message("Messages.Module.Downloaded");
        invalid.addVariable("$module", moduleName);
        invalid.translate(world, sender);

      });
      return true;
    }
    help(sender);
    return false;
  }
}