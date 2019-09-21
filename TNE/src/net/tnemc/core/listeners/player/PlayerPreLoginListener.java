package net.tnemc.core.listeners.player;

import net.tnemc.core.TNE;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * Created by creatorfromhell on 8/8/2019.
 * <p>
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */
public class PlayerPreLoginListener implements Listener {

  TNE plugin;

  public PlayerPreLoginListener(TNE plugin) {
    this.plugin = plugin;
  }

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onPreJoin(AsyncPlayerPreLoginEvent event) {
    if(TNE.instance().itemConfiguration() == null) {
      event.setKickMessage(ChatColor.RED + "The New Economy is still initializing.");
      event.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_OTHER);
    }

    if(TNE.isDuper(event.getUniqueId().toString()) || TNE.isDuper(event.getAddress().getHostAddress())) {
      event.setKickMessage(ChatColor.RED + "The New Economy Global Ban: You've been identified as a known currency duper. Appeal at http://discord.tnemc.net");
      event.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_BANNED);
    }
  }
}
