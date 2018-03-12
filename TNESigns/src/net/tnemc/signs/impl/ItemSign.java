package net.tnemc.signs.impl;

import net.tnemc.signs.impl.offer.ItemEntry;
import org.bukkit.Location;

import java.util.TreeMap;
import java.util.UUID;

/**
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by Daniel on 2/13/2018.
 */
public class ItemSign extends TNESign {

  public TreeMap<Integer, ItemEntry> offers = new TreeMap<>();

  protected boolean unlimited = false;

  public ItemSign(UUID owner, Location location) {
    super(owner, "item", location, "tne.sign.item.create", "tne.sign.item.use");
    this.requiresChest = !unlimited;
  }
}