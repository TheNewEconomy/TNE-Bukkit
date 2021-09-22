package net.tnemc.conversion.menu;

import net.tnemc.conversion.ConversionModule;
import net.tnemc.conversion.Converter;
import net.tnemc.core.TNE;
import net.tnemc.core.menu.icons.Icon;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;

import java.util.Optional;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * Created by creatorfromhell on 7/25/2021.
 * <p>
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */
public class ConvertIcon extends Icon {
  public ConvertIcon(Integer slot, String plugin) {
    super(slot, Material.PAPER, ChatColor.DARK_PURPLE + "Convert data from: " + plugin);

    this.message = ChatColor.WHITE + "Conversion is now in progress.";

    this.clickAction = (click)->{
      final Optional<Converter> converter = ConversionModule.instance().getConverter(plugin);
      Bukkit.getScheduler().runTaskAsynchronously(TNE.instance(), ()->{
        converter.get().convert();
        click.getPlayer().sendMessage(ChatColor.WHITE + "Conversion has completed. Running restoration command.");
        Bukkit.getScheduler().runTask(TNE.instance(), ()->Bukkit.getServer().dispatchCommand(click.getPlayer(), "tne restore"));
      });
    };
  }
}