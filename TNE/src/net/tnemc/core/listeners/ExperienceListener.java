package net.tnemc.core.listeners;

import net.tnemc.core.TNE;
import net.tnemc.core.common.api.IDFinder;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerExpChangeEvent;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * Created by Daniel on 7/12/2018.
 * <p>
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */
public class ExperienceListener implements Listener {

  TNE plugin;

  public ExperienceListener(TNE plugin) {
    this.plugin = plugin;
  }

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onExperienceGain(PlayerExpChangeEvent event) {
    TNE.manager().addXPGain(IDFinder.getID(event.getPlayer()));
  }
}