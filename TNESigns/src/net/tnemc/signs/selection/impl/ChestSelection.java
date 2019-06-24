package net.tnemc.signs.selection.impl;

import com.palmergames.bukkit.towny.object.TownyPermission;
import com.palmergames.bukkit.towny.utils.PlayerCacheUtil;
import net.tnemc.core.TNE;
import net.tnemc.core.common.api.IDFinder;
import net.tnemc.signs.SignsData;
import net.tnemc.signs.SignsModule;
import net.tnemc.signs.selection.Selection;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;

import java.sql.SQLException;
import java.util.UUID;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * Created by creatorfromhell on 6/19/2019.
 * <p>
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */
public class ChestSelection implements Selection {
  @Override
  public String name() {
    return "chest";
  }

  @Override
  public void select(UUID identifier, Location location, Object selection) {
    Bukkit.getScheduler().runTaskAsynchronously(TNE.instance(), ()->{
      try {
        if(Bukkit.getPluginManager().getPlugin("Towny") != null) {
          if(!PlayerCacheUtil.getCachePermission(IDFinder.getPlayer(identifier.toString()), location, Material.CHEST, TownyPermission.ActionType.SWITCH)) {
            return;
          }
        }

        SignsData.updateChest(location, (Location)selection);
        SignsData.updateStep(location, 4);
        IDFinder.getPlayer(identifier.toString()).sendMessage(ChatColor.WHITE + "Updated your shop's storage to the chest at X: " + location.getBlockX() + " Y: " + location.getBlockY() + " Z: " + location.getBlockZ());
        SignsModule.manager().getSelectionManager().remove(identifier);
      } catch (SQLException e) {
        IDFinder.getPlayer(identifier.toString()).sendMessage(ChatColor.RED + "Error while updating your shop's storage.");
      }
    });
  }
}