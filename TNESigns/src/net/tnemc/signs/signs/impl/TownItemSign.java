package net.tnemc.signs.signs.impl;

import com.palmergames.bukkit.towny.exceptions.NotRegisteredException;
import com.palmergames.bukkit.towny.object.TownyUniverse;
import com.palmergames.bukkit.towny.permissions.PermissionNodes;
import net.tnemc.core.TNE;
import net.tnemc.core.common.api.IDFinder;
import net.tnemc.signs.SignsData;
import net.tnemc.signs.signs.SignStep;
import net.tnemc.signs.signs.SignType;
import net.tnemc.signs.signs.TNESign;
import net.tnemc.signs.signs.impl.toitem.ChestSelectionStep;
import net.tnemc.signs.signs.impl.toitem.ItemSelectionStep;
import net.tnemc.signs.signs.impl.toitem.TradeSelectionStep;
import net.tnemc.signs.signs.impl.toitem.TradeStep;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.event.block.SignChangeEvent;

import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * Created by Daniel on 11/27/2018.
 * <p>
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */
public class TownItemSign implements SignType {

  /**
   * @return The name of this sign type.
   */
  @Override
  public String name() {
    return "toitem";
  }

  /**
   * @return The permission node required to use this sign.
   */
  @Override
  public String usePermission() {
    return "tne.sign.toitem.use";
  }

  /**
   * @return The permission node required to create this sign.
   */
  @Override
  public String createPermission() {
    return "tne.sign.toitem.create";
  }

  @Override
  public Map<Integer, SignStep> steps() {
    Map<Integer, SignStep> steps = new HashMap<>();
    steps.put(1, new ItemSelectionStep());
    steps.put(2, new TradeSelectionStep());
    steps.put(3, new ChestSelectionStep());
    steps.put(4, new TradeStep());
    return steps;
  }

  @Override
  public boolean onSignCreate(SignChangeEvent event, Block attached, UUID player) {
    try {

      if(!hasPerms(player)) {
        return false;
      }

      final UUID townID = IDFinder.getID(TownyUniverse.getDataSource().getResident(IDFinder.getUsername(player.toString())).getTown().getEconomyName());


      SignsData.saveSign(new TNESign(event.getBlock().getLocation(), (attached != null)? attached.getLocation() : event.getBlock().getLocation(), "item", townID, player, new Date().getTime(), false, 1));
      TNE.debug("Created Item Sign");
      Bukkit.getPlayer(player).sendMessage(ChatColor.WHITE + "Left click the sign with an item in the correct amount to buy that item," +
                                             " or right click to sell an item.");
      return true;
    } catch (Exception ignore) {
      ignore.printStackTrace();
    }
    return false;
  }

  @Override
  public boolean onSignInteract(Sign sign, UUID player, boolean rightClick, boolean shifting) {
    TNE.debug("Item Sign Interaction!");
    try {
      final TNESign loaded = SignsData.loadSign(sign.getLocation());
      if(loaded == null) return false;
      TNE.debug("Item Sign Interaction! Step: " + loaded.getStep());
      return steps().get(loaded.getStep()).onSignInteract(sign, player, rightClick, shifting);
    } catch (SQLException e) {
      TNE.debug(e);
    }
    return false;
  }

  @Override
  public boolean onChest(UUID owner, UUID player) {
    return hasPerms(owner, player);
  }

  @Override
  public boolean onSignDestroy(UUID owner, UUID player) {
    return hasPerms(owner, player);
  }

  public static boolean hasPerms(UUID player) {
    return hasPerms(null, player);
  }

  public static boolean hasPerms(UUID owner, UUID player) {

    final String display = IDFinder.getUsername(player.toString());

    try {
      if(!TownyUniverse.getDataSource().getResident(display).hasTown()) {
        Bukkit.getPlayer(player).sendMessage(ChatColor.RED + "You must be in a town to create this sign type.");
        return false;
      }

      if(!TownyUniverse.getPermissionSource().testPermission(Bukkit.getPlayer(player), PermissionNodes.TOWNY_COMMAND_TOWN_DEPOSIT.getNode())) {
        Bukkit.getPlayer(player).sendMessage(ChatColor.RED + "Invalid town permissions.");
        return false;
      }

      if(owner != null && !TNE.manager().getAccount(owner).displayName().equals(TownyUniverse.getDataSource().getResident(display).getTown().getEconomyName())) {
        Bukkit.getPlayer(player).sendMessage(ChatColor.RED + "Invalid town permissions.");
        return false;
      }
    } catch (NotRegisteredException ignore) {
      ignore.printStackTrace();
      return false;
    }
    return true;
  }


}