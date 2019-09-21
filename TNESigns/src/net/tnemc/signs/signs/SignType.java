package net.tnemc.signs.signs;

import net.tnemc.core.TNE;
import net.tnemc.core.menu.Menu;
import net.tnemc.signs.SignsData;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.event.block.SignChangeEvent;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * Created by Daniel on 5/28/2018.
 * <p>
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */
public interface SignType {

  default Map<Integer, SignStep> steps() {
    return new HashMap<>();
  }

  /**
   * @return The name of this sign type.
   */
  String name();

  /**
   * @return The ChatColor to set the top line to if the sign is created successfully.
   */
  default ChatColor success() {
    return ChatColor.DARK_BLUE;
  }

  default boolean enabled(final UUID player) {
    String formatted = name().substring(0, 1).toUpperCase() + name().substring(1);
    if(TNE.instance().api().getConfiguration("Signs." + formatted + ".Enabled", TNE.instance().defaultWorld, player) != null) {
      return TNE.instance().api().getBoolean("Signs." + formatted + ".Enabled", TNE.instance().defaultWorld, player);
    }
    return false;
  }

  default int max(final UUID player) {
    String formatted = name().substring(0, 1).toUpperCase() + name().substring(1);
    if(TNE.instance().api().getConfiguration("Signs." + formatted + ".Max", TNE.instance().defaultWorld, player) != null) {
      return TNE.instance().api().getInteger("Signs." + formatted + ".Max", TNE.instance().defaultWorld, player);
    }
    return 5;
  }

  default BigDecimal cost(final UUID player) {
    String formatted = name().substring(0, 1).toUpperCase() + name().substring(1);

    if(TNE.instance().api().getConfiguration("Signs." + formatted + ".PlaceCost", TNE.instance().defaultWorld, player) != null) {
      return TNE.instance().api().getBigDecimal("Signs." + formatted + ".PlaceCost", TNE.instance().defaultWorld, player);
    }
    return BigDecimal.ZERO;
  }

  /**
   * @return The permission node required to use this sign.
   */
  String usePermission();

  /**
   * @return The permission node required to create this sign.
   */
  String createPermission();

  default boolean stepped() {
    return steps().size() > 0;
  }

  default Map<String, List<String>> tables() {
    return new HashMap<>();
  }

  default Menu getMenu(final UUID player) {
    return null;
  }

  default boolean hasMenu(final UUID player) {
    return getMenu(player) != null;
  }

  default boolean create(final SignChangeEvent event, final Block attached, final UUID player) {
    if(!enabled(player)) {
      Bukkit.getPlayer(player).sendMessage(ChatColor.RED + "This sign type is not enabled.");
      return false;
    }

    try {
      if(!Bukkit.getPlayer(player).hasPermission("tne.sign." + name() + ".unlimited")) {
        if (max(player) == 0 || SignsData.loadSignsCreator(player.toString(), name()).size() >= max(player)) {
          Bukkit.getPlayer(player).sendMessage(ChatColor.RED + "You have reached your max limit for this sign type.");
          return false;
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return onSignCreate(event, attached, player);
  }

  default boolean onSignCreate(final SignChangeEvent event, final Block attached, final UUID player) {
    return true;
  }

  default boolean onSignInteract(final Sign sign, final UUID player, final boolean rightClick, final boolean shifting) {
    return false;
  }

  default boolean onSignDestroy(final UUID owner, final UUID player) {
    return true;
  }

  default boolean onChest(final UUID owner, final UUID player) {
    return owner.equals(player);
  }

  default boolean onMenuOpen(final Sign sign, final UUID player) {
    return true;
  }

  default boolean onMenuClose(final Sign sign, final UUID player) {
    return true;
  }
}