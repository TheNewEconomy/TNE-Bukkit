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

import java.sql.SQLException;
import java.util.Date;
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
public class NationSign implements SignType {
  /**
   * @return The name of this sign type.
   */
  @Override
  public String name() {
    return "nation";
  }

  /**
   * @return The permission node required to use this sign.
   */
  @Override
  public String usePermission() {
    return "tne.sign.nation.use";
  }

  /**
   * @return The permission node required to create this sign.
   */
  @Override
  public String createPermission() {
    return "tne.sign.nation.create";
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

        if(!resident.hasTown() || !resident.isKing()) {
          Bukkit.getPlayer(player).sendMessage(ChatColor.RED + "Must be king to create nation vault.");
          return false;
        }

        try {
          String name = resident.getTown().getNation().getName();
          if(name.length() <= 16) {
            event.setLine(1, name);
          } else {
            event.setLine(1, name.substring(0, 16));
            int end = (name.length() >= 32)? 32 : name.length();
            event.setLine(1, name.substring(16, end));
          }



          UUID owner = IDFinder.getID(TownySettings.getNationAccountPrefix() + name);
          SignsData.saveSign(new TNESign(event.getBlock().getLocation(), attached.getLocation(), "nation", owner, player, new Date().getTime()));
          return true;
        } catch (NotRegisteredException e) {
          //Shouldn't reach this point.
          return false;
        } catch (SQLException e) {
          e.printStackTrace();
        }
      }
    }
    return false;
  }

  @Override
  public boolean onChest(UUID owner, UUID player) {
    Resident resident = null;
    try {
      resident = TownyUniverse.getDataSource().getResident(IDFinder.getUsername(player.toString()));
    } catch (NotRegisteredException e) {
      return false;
    }

    if(resident == null) return false;

    try {
      if(resident.hasTown() && resident.isKing()) {
        UUID nationID = IDFinder.getID(TownySettings.getNationAccountPrefix() + resident.getTown().getNation().getName());
        return owner.equals(nationID);
      }
    } catch (NotRegisteredException e) {
      return false;
    }

    return false;
  }

  @Override
  public boolean onSignDestroy(UUID owner, UUID player) {
    Resident resident = null;
    try {
      resident = TownyUniverse.getDataSource().getResident(IDFinder.getUsername(player.toString()));
    } catch (NotRegisteredException e) {
      return false;
    }

    if(resident == null) return false;

    try {
      if(resident.hasTown() && resident.isKing()) {
        UUID nationID = IDFinder.getID(TownySettings.getNationAccountPrefix() + resident.getTown().getNation().getName());
        return owner.equals(nationID);
      }
    } catch (NotRegisteredException e) {
      return false;
    }

    return false;
  }
}
