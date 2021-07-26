package net.tnemc.conversion.command;

import net.tnemc.commands.core.CommandExecution;
import net.tnemc.commands.core.provider.PlayerProvider;
import net.tnemc.conversion.ConversionModule;
import net.tnemc.conversion.Converter;
import net.tnemc.core.TNE;
import net.tnemc.core.common.utils.MISCUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Optional;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * Created by Daniel on 6/5/2018.
 * <p>
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */
public class ConvertCommand implements CommandExecution {
  /*public ConvertCommand(TNELib plugin) {
    super(plugin);
  }

  @Override
  public String name() {
    return "convert";
  }

  @Override
  public String[] aliases() {
    return new String[0];
  }

  @Override
  public String node() {
    return "tne.command.convert";
  }

  @Override
  public boolean console() {
    return true;
  }

  @Override
  public String helpLine() {
    return "/convert <from> - Converts all data from plugin <from>.";
  }*/

  @Override
  public boolean execute(PlayerProvider provider, String label, String[] arguments) {
    CommandSender sender = MISCUtils.getSender(provider);
    if(arguments.length >= 1) {
      final String old = arguments[0].toLowerCase();
      final Optional<Converter> converter = ConversionModule.instance().getConverter(old);
      if(!converter.isPresent()) {
        sender.sendMessage(ChatColor.RED + "Invalid <from> argument specified.");
        return false;
      }
      Bukkit.getScheduler().runTaskAsynchronously(TNE.instance(), ()->{
        converter.get().convert();
        sender.sendMessage(ChatColor.WHITE + "Conversion has completed. Running restoration command.");
        Bukkit.getScheduler().runTask(TNE.instance(), ()->Bukkit.getServer().dispatchCommand(sender, "tne restore"));
      });
      sender.sendMessage(ChatColor.WHITE + "Conversion is now in progress.");
      return true;
    } else {
      if(sender instanceof Player) {
        TNE.menuManager().open("conversion_menu", (Player)sender);
      }
    }
    MISCUtils.help(sender, label, arguments);
    return false;
  }
}