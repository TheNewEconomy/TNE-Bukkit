package net.tnemc.core.commands.module;

import net.tnemc.commands.core.CommandExecution;
import net.tnemc.commands.core.provider.PlayerProvider;
import net.tnemc.core.TNE;
import net.tnemc.core.common.Message;
import net.tnemc.core.common.WorldVariant;
import net.tnemc.core.common.account.WorldFinder;
import net.tnemc.core.common.module.ModuleUpdateChecker;
import net.tnemc.core.common.module.cache.ModuleFile;
import net.tnemc.core.common.utils.MISCUtils;
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
public class ModuleDownloadCommand implements CommandExecution {

  @Override
  public boolean execute(PlayerProvider provider, String label, String[] arguments) {
    CommandSender sender = MISCUtils.getSender(provider);
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
    MISCUtils.help(sender, label, arguments);
    return false;
  }
}