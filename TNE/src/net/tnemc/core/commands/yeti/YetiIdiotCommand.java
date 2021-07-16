package net.tnemc.core.commands.yeti;

import net.tnemc.commands.core.CommandExecution;
import net.tnemc.commands.core.provider.PlayerProvider;
import net.tnemc.core.common.api.IDFinder;
import net.tnemc.core.common.utils.MISCUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by Daniel on 10/17/2017.
 */
public class YetiIdiotCommand implements CommandExecution {

  @Override
  public boolean execute(PlayerProvider provider, String label, String[] arguments) {
    CommandSender sender = MISCUtils.getSender(provider);
    Player player = MISCUtils.getPlayer(sender);
    Player yeti = MISCUtils.getPlayer(IDFinder.getID("TheNetYeti"));
    Player growlf = MISCUtils.getPlayer(IDFinder.getID("growlf"));
    Player yediot = MISCUtils.getPlayer(UUID.fromString("66a7e812-fb82-409c-88c4-9edc34bb5c39"));

    if(player.getDisplayName().equalsIgnoreCase("TheNetYeti")) {
      sender.sendMessage(ChatColor.GREEN + "Congratulations you have found the Yediot.");
      return true;
    }

    if(yeti != null) {
      if(yeti.getLocation().distanceSquared(player.getLocation()) <= 25) {
        sender.sendMessage(ChatColor.WHITE + "There is a Yediot near you.");
        return true;
      }
      sender.sendMessage(ChatColor.WHITE + "There is a Yediot on this server.");
      return true;
    }

    if(growlf != null) {
      if(growlf.getLocation().distanceSquared(player.getLocation()) <= 25) {
        sender.sendMessage(ChatColor.WHITE + "There is a Yediot in disguise near you.");
        return true;
      }
      sender.sendMessage(ChatColor.WHITE + "There is a Yediot in disguise on this server.");
      return true;
    }

    if(yediot != null) {
      if(yediot.getLocation().distanceSquared(player.getLocation()) <= 25) {
        sender.sendMessage(ChatColor.WHITE + "There is a Yediot near you");
        return true;
      }
      sender.sendMessage(ChatColor.WHITE + "There is a Yediot on this server.");
      return true;
    }
    sender.sendMessage(ChatColor.WHITE + "There is no Yediot on this server.");
    return true;
  }
}