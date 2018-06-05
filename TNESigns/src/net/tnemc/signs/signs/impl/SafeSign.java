package net.tnemc.signs.signs.impl;

import net.tnemc.core.common.api.IDFinder;
import net.tnemc.signs.SignsData;
import net.tnemc.signs.signs.SignType;
import net.tnemc.signs.signs.TNESign;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.block.SignChangeEvent;

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
public class SafeSign implements SignType {
  /**
   * @return The name of this sign type.
   */
  @Override
  public String name() {
    return "safe";
  }

  /**
   * @return The permission node required to use this sign.
   */
  @Override
  public String usePermission() {
    return "tne.sign.safe.use";
  }

  /**
   * @return The permission node required to create this sign.
   */
  @Override
  public String createPermission() {
    return "tne.sign.safe.create";
  }

  @Override
  public boolean onSignCreate(SignChangeEvent event, Block attached, UUID player) {
    if(attached != null) {
      if(attached.getType().equals(Material.CHEST) || attached.getType().equals(Material.TRAPPED_CHEST)) {

        event.setLine(1, IDFinder.getUsername(player.toString()));
        SignsData.saveSign(new TNESign(event.getBlock().getLocation(), attached.getLocation(), "safe", player, player, new Date().getTime()));
        return true;
      }
    }
    return false;
  }

  @Override
  public boolean onChest(UUID owner, UUID player) {
    return owner.toString().equalsIgnoreCase(player.toString());
  }

  @Override
  public boolean onSignDestroy(UUID owner, UUID player) {
    return owner.toString().equalsIgnoreCase(player.toString());
  }
}
