package net.tnemc.signs.listeners;

import com.palmergames.bukkit.towny.TownySettings;
import com.palmergames.bukkit.towny.event.RenameNationEvent;
import com.palmergames.bukkit.towny.event.RenameTownEvent;
import net.tnemc.core.TNE;
import net.tnemc.core.common.api.IDFinder;
import net.tnemc.signs.SignsData;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import java.sql.SQLException;
import java.util.UUID;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * Created by Daniel on 12/4/2018.
 * <p>
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */
public class TownyListener implements Listener {

  private TNE plugin;

  public TownyListener(TNE plugin) {
    this.plugin = plugin;
  }

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onTownRename(RenameTownEvent event) {
    Bukkit.getScheduler().runTaskAsynchronously(plugin, ()->{
      final UUID old = IDFinder.getID(TownySettings.getTownAccountPrefix() + event.getOldName());
      final UUID newID = IDFinder.getID(TownySettings.getTownAccountPrefix() + event.getTown().getName());
      try {
        SignsData.changeOwner(old, "town", event.getTown().getName());
        SignsData.updateOwner(old, newID);
      } catch (SQLException e) {
        TNE.debug(e);
      }
    });
  }

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onNationRename(RenameNationEvent event) {
    Bukkit.getScheduler().runTaskAsynchronously(plugin, ()->{
      final UUID old = IDFinder.getID(TownySettings.getTownAccountPrefix() + event.getOldName());
      final UUID newID = IDFinder.getID(TownySettings.getTownAccountPrefix() + event.getNation().getName());
      try {
        SignsData.changeOwner(old, "nation", event.getNation().getName());
        SignsData.updateOwner(old, newID);
      } catch (SQLException e) {
        TNE.debug(e);
      }
    });
  }
}