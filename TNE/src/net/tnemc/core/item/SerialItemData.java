package net.tnemc.core.item;

import org.bukkit.inventory.ItemStack;
import org.json.simple.JSONObject;

/**
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by Daniel on 11/10/2017.
 */
public interface SerialItemData {

  SerialItemData initialize(ItemStack stack);
  ItemStack build(ItemStack stack);
  JSONObject toJSON();
  void readJSON(JSONHelper json);
}