package net.tnemc.core.compatibility.item;

import net.tnemc.core.common.utils.MISCUtils;
import net.tnemc.core.compatibility.ItemCompatibility;
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
public class ItemCompatibility7 implements ItemCompatibility {
  @Override
  public ItemStack build(String material) {

    switch(material.toUpperCase()) {
      case "BARRIER":
        return new ItemStack(Material.matchMaterial("STAINED_GLASS_PANE"), 1, (byte)14);
      case "WHITE_STAINED_GLASS_PANE":
        return new ItemStack(Material.matchMaterial("STAINED_GLASS_PANE"), 1, (byte)0);
      case "BLACK_STAINED_GLASS_PANE":
        return new ItemStack(Material.matchMaterial("STAINED_GLASS_PANE"), 1, (byte)15);
      case "RED_STAINED_GLASS_PANE":
        return new ItemStack(Material.matchMaterial("STAINED_GLASS_PANE"), 1, (byte)14);
      case "GREEN_STAINED_GLASS_PANE":
        return new ItemStack(Material.matchMaterial("STAINED_GLASS_PANE"), 1, (byte)13);
      case "YELLOW_STAINED_GLASS_PANE":
        return new ItemStack(Material.matchMaterial("STAINED_GLASS_PANE"), 1, (byte)4);
      case "BLUE_STAINED_GLASS_PANE":
        return new ItemStack(Material.matchMaterial("STAINED_GLASS_PANE"), 1, (byte)11);
      case "HEAVY_WEIGHTED_PRESSURE_PLATE":
        return new ItemStack(Material.matchMaterial("IRON_PLATE"), 1);
      case "LIGHT_WEIGHTED_PRESSURE_PLATE":
        return new ItemStack(Material.matchMaterial("GOLD_PLATE"), 1);
      case "PLAYER_HEAD":
        return new ItemStack(Material.matchMaterial("SKULL_ITEM"), 1, (byte)3);
      case "BLACK_WOOL":
        return new ItemStack(Material.matchMaterial("WOOL"), 1, (byte)15);
      case "IRON_NUGGET":
        if(MISCUtils.isOneEight() || MISCUtils.isOneNine() || MISCUtils.isOneTen()) {
          return new ItemStack(Material.matchMaterial("IRON_FENCE"), 1);
        }
        return new ItemStack(Material.IRON_NUGGET, 1);

    }

    return new ItemStack(Material.AIR);
  }

  @Override
  public boolean isShulker(Material material) {
    return material.name().equalsIgnoreCase("WHITE_SHULKER_BOX") ||
      material.name().equalsIgnoreCase("ORANGE_SHULKER_BOX") ||
      material.name().equalsIgnoreCase("MAGENTA_SHULKER_BOX") ||
      material.name().equalsIgnoreCase("LIGHT_BLUE_SHULKER_BOX") ||
      material.name().equalsIgnoreCase("YELLOW_SHULKER_BOX") ||
      material.name().equalsIgnoreCase("LIME_SHULKER_BOX") ||
      material.name().equalsIgnoreCase("PINK_SHULKER_BOX") ||
      material.name().equalsIgnoreCase("GRAY_SHULKER_BOX") ||
      material.name().equalsIgnoreCase("SILVER_SHULKER_BOX") ||
      material.name().equalsIgnoreCase("CYAN_SHULKER_BOX") ||
      material.name().equalsIgnoreCase("PURPLE_SHULKER_BOX") ||
      material.name().equalsIgnoreCase("BLUE_SHULKER_BOX") ||
      material.name().equalsIgnoreCase("BROWN_SHULKER_BOX") ||
      material.name().equalsIgnoreCase("GREEN_SHULKER_BOX") ||
      material.name().equalsIgnoreCase("RED_SHULKER_BOX") ||
      material.name().equalsIgnoreCase("BLACK_SHULKER_BOX");
  }

  @Override
  public Enchantment find(String name) {
    return Enchantment.getByName(name);
  }
}