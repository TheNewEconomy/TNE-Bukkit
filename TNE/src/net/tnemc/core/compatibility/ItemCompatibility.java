package net.tnemc.core.compatibility;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * Created by Daniel on 2/6/2019.
 * <p>
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */
public interface ItemCompatibility {

  ItemStack build(String material);

  Enchantment find(String name);

  boolean isShulker(Material material);
}