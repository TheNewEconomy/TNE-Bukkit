package net.tnemc.bounty.listeners;

import net.tnemc.bounty.BountyData;
import net.tnemc.core.TNE;
import net.tnemc.core.common.api.IDFinder;
import net.tnemc.core.common.module.ModuleListener;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.UUID;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * Created by creatorfromhell on 6/28/2019.
 * <p>
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */
public class PlayerJoinListener implements ModuleListener {

  private TNE plugin;

  public PlayerJoinListener(TNE plugin) {
    this.plugin = plugin;
  }

  @EventHandler(priority = EventPriority.MONITOR)
  public static void onJoin(PlayerJoinEvent event) {
    final UUID id = IDFinder.getID(event.getPlayer());

    if(BountyData.getRewards(id).getRewards().size() > 0) {
      event.getPlayer().sendMessage(ChatColor.YELLOW + "You have bounty rewards waiting for you! Type \"/bounty rewards\" to claim them.");
    }
  }
}