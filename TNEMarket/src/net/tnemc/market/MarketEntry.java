package net.tnemc.market;

import org.bukkit.inventory.ItemStack;

import java.util.UUID;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * Created by Daniel on 12/3/2018.
 * <p>
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */
public class MarketEntry {

  private final ItemStack stack;
  private final UUID id;

  public MarketEntry(ItemStack stack, UUID id) {
    this.stack = stack;
    this.id = id;
  }

  public ItemStack getStack() {
    return stack;
  }

  public UUID getId() {
    return id;
  }
}