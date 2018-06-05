package net.tnemc.tutorial.listeners;

import net.tnemc.core.TNE;
import net.tnemc.tutorial.TutorialModule;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * Created by Daniel on 5/29/2018.
 * <p>
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */
public class PlayerListener implements Listener {

  private TNE plugin;

  public PlayerListener(TNE plugin) {
    this.plugin = plugin;
  }

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onLogout(PlayerQuitEvent event) {
    if(TutorialModule.manager().getLearners().containsKey(event.getPlayer().getUniqueId())) {
      TutorialModule.manager().removeLearner(event.getPlayer().getUniqueId());
    }
  }

  @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
  public void onBreak(BlockBreakEvent event) {
    if(TutorialModule.manager().getLearners().containsKey(event.getPlayer().getUniqueId())) {
      event.setCancelled(true);
    }
  }

  @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
  public void onMove(PlayerMoveEvent event) {
    if(TutorialModule.manager().getLearners().containsKey(event.getPlayer().getUniqueId())) {
      event.setCancelled(true);
    }
  }
}