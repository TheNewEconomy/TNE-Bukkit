package net.tnemc.core.compatibility.item;

import net.tnemc.core.compatibility.ItemCompatibility;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
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
public class ItemCompatibility13 implements ItemCompatibility {
  @Override
  public ItemStack build(String material) {

    switch(material.toUpperCase()) {
      case "BARRIER":
        return new ItemStack(Material.matchMaterial("BARRIER"), 1);
      case "WHITE_STAINED_GLASS_PANE":
        return new ItemStack(Material.WHITE_STAINED_GLASS_PANE, 1);
      case "BLACK_STAINED_GLASS_PANE":
        return new ItemStack(Material.BLACK_STAINED_GLASS_PANE, 1);
      case "RED_STAINED_GLASS_PANE":
        return new ItemStack(Material.RED_STAINED_GLASS_PANE, 1);
      case "GREEN_STAINED_GLASS_PANE":
        return new ItemStack(Material.GREEN_STAINED_GLASS_PANE, 1);
      case "YELLOW_STAINED_GLASS_PANE":
        return new ItemStack(Material.YELLOW_STAINED_GLASS_PANE, 1);
      case "BLUE_STAINED_GLASS_PANE":
        return new ItemStack(Material.BLUE_STAINED_GLASS_PANE, 1);
      case "HEAVY_WEIGHTED_PRESSURE_PLATE":
        return new ItemStack(Material.HEAVY_WEIGHTED_PRESSURE_PLATE, 1);
      case "LIGHT_WEIGHTED_PRESSURE_PLATE":
        return new ItemStack(Material.LIGHT_WEIGHTED_PRESSURE_PLATE, 1);
      case "PLAYER_HEAD":
        return new ItemStack(Material.PLAYER_HEAD, 1);
      case "BLACK_WOOL":
        return new ItemStack(Material.BLACK_WOOL, 1);
      case "GREEN_WOOL":
        return new ItemStack(Material.GREEN_WOOL, 1);
      case "RED_WOOL":
        return new ItemStack(Material.RED_WOOL, 1);
      case "IRON_NUGGET":
        return new ItemStack(Material.IRON_NUGGET, 1);
    }

    return new ItemStack(Material.AIR);
  }

  @Override
  public Enchantment find(String name) {
    return Enchantment.getByKey(NamespacedKey.minecraft(name.toLowerCase()));
  }

  @Override
  public boolean isShulker(Material material) {
    return material.equals(Material.WHITE_SHULKER_BOX) ||
      material.equals(Material.ORANGE_SHULKER_BOX) ||
      material.equals(Material.MAGENTA_SHULKER_BOX) ||
      material.equals(Material.LIGHT_BLUE_SHULKER_BOX) ||
      material.equals(Material.YELLOW_SHULKER_BOX) ||
      material.equals(Material.LIME_SHULKER_BOX) ||
      material.equals(Material.PINK_SHULKER_BOX) ||
      material.equals(Material.GRAY_SHULKER_BOX) ||
      material.equals(Material.LIGHT_GRAY_SHULKER_BOX) ||
      material.equals(Material.CYAN_SHULKER_BOX) ||
      material.equals(Material.PURPLE_SHULKER_BOX) ||
      material.equals(Material.BLUE_SHULKER_BOX) ||
      material.equals(Material.BROWN_SHULKER_BOX) ||
      material.equals(Material.GREEN_SHULKER_BOX) ||
      material.equals(Material.RED_SHULKER_BOX) ||
      material.equals(Material.BLACK_SHULKER_BOX);
  }
}