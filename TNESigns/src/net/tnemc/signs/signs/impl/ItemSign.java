package net.tnemc.signs.signs.impl;

import net.tnemc.core.menu.Menu;
import net.tnemc.signs.signs.SignType;
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
public class ItemSign implements SignType {
  /**
   * @return The name of this sign type.
   */
  @Override
  public String name() {
    return "item";
  }

  /**
   * @return The permission node required to use this sign.
   */
  @Override
  public String usePermission() {
    return "tne.sign.item.use";
  }

  /**
   * @return The permission node required to create this sign.
   */
  @Override
  public String createPermission() {
    return "tne.sign.item.create";
  }

  @Override
  public boolean onSignCreate(SignChangeEvent event, Block attached, UUID player) {
    event.setLine(1, "Step 1");
    event.setLine(2, "Shop Setup");
    return true;
  }

  @Override
  public Menu getMenu() {
    return null;
  }
}