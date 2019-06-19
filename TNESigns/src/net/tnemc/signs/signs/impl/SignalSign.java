package net.tnemc.signs.signs.impl;

import net.tnemc.core.TNE;
import net.tnemc.signs.SignsData;
import net.tnemc.signs.signs.SignStep;
import net.tnemc.signs.signs.SignType;
import net.tnemc.signs.signs.TNESign;
import net.tnemc.signs.signs.impl.signal.CostSelectionStep;
import net.tnemc.signs.signs.impl.signal.SignalBlockSelectionStep;
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
 * Created by creatorfromhell on 6/18/2019.
 * <p>
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */
public class SignalSign implements SignType {
  /**
   * @return The name of this sign type.
   */
  @Override
  public String name() {
    return "signal";
  }

  /**
   * @return The permission node required to use this sign.
   */
  @Override
  public String usePermission() {
    return "tne.sign.signal.use";
  }

  /**
   * @return The permission node required to create this sign.
   */
  @Override
  public String createPermission() {
    return "tne.sign.signal.create";
  }

  @Override
  public Map<Integer, SignStep> steps() {
    Map<Integer, SignStep> steps = new HashMap<>();
    steps.put(1, new SignalBlockSelectionStep());
    steps.put(2, new CostSelectionStep());
    return steps;
  }

  @Override
  public boolean onSignCreate(SignChangeEvent event, Block attached, UUID player) {
    try {

      SignsData.saveSign(new TNESign(event.getBlock().getLocation(), (attached != null)? attached.getLocation() : event.getBlock().getLocation(), "signal", player, player, new Date().getTime(), false, 1));
      TNE.debug("Created Signal Sign");
      Bukkit.getPlayer(player).sendMessage(ChatColor.WHITE + "Right click the redstone torch you wish to power.");
      return true;
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return false;
  }

  @Override
  public boolean onSignInteract(Sign sign, UUID player, boolean rightClick, boolean shifting) {
    TNE.debug("Signal Sign Interaction!");
    try {
      final TNESign loaded = SignsData.loadSign(sign.getLocation());
      if(loaded == null) return false;
      TNE.debug("Signal Sign Interaction! Step: " + loaded.getStep());
      return steps().get(loaded.getStep()).onSignInteract(sign, player, rightClick, shifting);
    } catch (SQLException e) {
      TNE.debug(e);
    }
    return false;
  }

  @Override
  public boolean onSignDestroy(UUID owner, UUID player) {
    if(owner.equals(player)) {
      return true;
    }
    return false;
  }
}