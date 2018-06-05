package net.tnemc.conversion.command;

import com.github.tnerevival.TNELib;
import net.tnemc.conversion.ConversionModule;
import net.tnemc.conversion.Converter;
import net.tnemc.core.TNE;
import net.tnemc.core.commands.TNECommand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

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
public class ConvertCommand extends TNECommand {
  public ConvertCommand(TNELib plugin) {
    super(plugin);
  }

  @Override
  public String getName() {
    return "convert";
  }

  @Override
  public String[] getAliases() {
    return new String[0];
  }

  @Override
  public String getNode() {
    return "tne.command.convert";
  }

  @Override
  public boolean console() {
    return true;
  }

  @Override
  public String getHelp() {
    return "/convert <from> - Converts all data from plugin <from>.";
  }

  @Override
  public boolean execute(CommandSender sender, String command, String[] arguments) {
    if(arguments.length >= 1) {
      final String old = arguments[0];
      final Converter converter = ConversionModule.instance().getConverter(old);
      if(converter == null) {
        sender.sendMessage(ChatColor.RED + "Invalid <from> argument specified.");
        return false;
      }
      Bukkit.getScheduler().runTaskAsynchronously(TNE.instance(), ()->{
        converter.convert();
        sender.sendMessage(ChatColor.WHITE + "Conversion has completed. Running restoration command.");
        Bukkit.getServer().dispatchCommand(sender, "tne restore");
      });
      sender.sendMessage(ChatColor.WHITE + "Conversion is now in progress.");
      return true;
    }
    help(sender);
    return false;
  }
}