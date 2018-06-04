package net.tnemc.signs.signs.impl;

import com.palmergames.bukkit.towny.TownySettings;
import com.palmergames.bukkit.towny.exceptions.NotRegisteredException;
import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.TownyUniverse;
import net.tnemc.core.common.api.IDFinder;
import net.tnemc.signs.SignsData;
import net.tnemc.signs.signs.SignType;
import net.tnemc.signs.signs.TNESign;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.block.SignChangeEvent;

import java.util.UUID;

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
public class TownSign implements SignType {
  /**
   * @return The name of this sign type.
   */
  @Override
  public String name() {
    return "town";
  }

  /**
   * @return The permission node required to use this sign.
   */
  @Override
  public String usePermission() {
    return "tne.sign.town.use";
  }

  /**
   * @return The permission node required to create this sign.
   */
  @Override
  public String createPermission() {
    return "tne.sign.town.create";
  }

  @Override
  public boolean onSignCreate(SignChangeEvent event, Block attached, UUID player) {
    if(attached != null) {
      if(attached.getType().equals(Material.CHEST) || attached.getType().equals(Material.TRAPPED_CHEST)) {
        Resident resident = null;
        try {
          resident = TownyUniverse.getDataSource().getResident(IDFinder.getUsername(player.toString()));
        } catch (NotRegisteredException e) {
          return false;
        }

        if(resident == null) return false;

        if(!resident.isMayor()) {
          Bukkit.getPlayer(player).sendMessage(ChatColor.RED + "Must be mayor to create town vault.");
          return false;
        }

        try {
          String name = resident.getTown().getName();
          if(name.length() <= 16) {
            event.setLine(1, name);
          } else {
            event.setLine(1, name.substring(0, 16));
            int end = (name.length() >= 32)? 32 : name.length();
            event.setLine(1, name.substring(16, end));
          }
          UUID owner = IDFinder.getID(TownySettings.getTownAccountPrefix() + name);
          SignsData.saveSign(new TNESign(event.getBlock().getLocation(), attached.getLocation(), "town", owner));
          return true;
        } catch (NotRegisteredException e) {
          //Shouldn't reach this point.
          return false;
        }
      }
    }
    return false;
  }
}