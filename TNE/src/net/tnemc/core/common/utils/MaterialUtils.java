package net.tnemc.core.common.utils;

import net.tnemc.core.TNE;
import net.tnemc.core.common.currency.ItemTier;
import net.tnemc.core.item.SerialItem;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.ShulkerBox;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

/*
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 07/01/2017.
 */
public class MaterialUtils {
  public static String formatMaterialName(Material material) {
    String[] wordsSplit = material.name().split("_");
    String sReturn = "";
    for(String w: wordsSplit) {
      String word = w.toUpperCase().replace(w.substring(1), w.substring(1).toLowerCase());
      sReturn += word;
    }
    return sReturn;
  }

  public static String deformatMaterialName(String name) {
    String[] split = name.split("(?=[A-Z])");
    String sReturn = "";
    int count = 1;
    for(String s : split) {
      sReturn += s.toUpperCase();
      if(count < split.length && count > 1) {
        sReturn += "_";
      }
      count++;
    }
    return sReturn;
  }

  public static String formatMaterialNameWithSpace(Material material) {
    String[] wordsSplit = material.name().split("_");
    String sReturn = "";
    int count = 1;
    for(String w: wordsSplit) {
      String word = w.toUpperCase().replace(w.substring(1), w.substring(1).toLowerCase());
      sReturn += word;
      if(count < wordsSplit.length) {
        sReturn += " ";
      }
    }
    return sReturn;
  }

  public static String deformatMaterialNameWithSpace(String name) {
    String upperCase = name.toUpperCase();
    return upperCase.replace(" ", "_");
  }

  public static Boolean itemsEqual(ItemStack original, ItemStack compare) {
    if(compare == null) return false;
    TNE.debug("Enchant Size: " + original.getEnchantments().size());
    TNE.debug("Enchant Size: " + original.getItemMeta().getEnchants().size());
    ItemMeta originalMeta = original.getItemMeta();
    ItemMeta compareMeta = compare.getItemMeta();
    if(compare.hasItemMeta()) {
      TNE.debug("Meta");
      if (compareMeta.hasDisplayName()) {
        TNE.debug("display");
        if (!originalMeta.hasDisplayName()) return false;
        TNE.debug("Compare Display");
        if (!originalMeta.getDisplayName().equalsIgnoreCase(compareMeta.getDisplayName())) return false;
      }
      TNE.debug("lore");
      if (compareMeta.hasLore()) {
        TNE.debug("lorez");
        if (!originalMeta.hasLore()) return false;
        TNE.debug("compare lorez");
        TNE.debug(String.join(", " + originalMeta.getLore()));
        TNE.debug(String.join(", " + compareMeta.getLore()));
        if (!originalMeta.getLore().containsAll(compareMeta.getLore())) return false;
      }

      TNE.debug("enchant");
      TNE.debug("Enchant Size: " + original.getEnchantments().size());
      TNE.debug("Enchant Size: " + originalMeta.getEnchants().size());
      if (compareMeta.hasEnchants()) {
        if (!originalMeta.hasEnchants()) return false;

        TNE.debug("enchantz");
        for (Map.Entry<Enchantment, Integer> entry : compare.getEnchantments().entrySet()) {
          TNE.debug("start");
          if (!original.containsEnchantment(entry.getKey())) return false;
          TNE.debug("contains");
          if (original.getEnchantmentLevel(entry.getKey()) != entry.getValue()) return false;
          TNE.debug("level");
        }
      }
    }

    if(isShulker(original.getType())) {
      if(originalMeta instanceof BlockStateMeta && compareMeta instanceof  BlockStateMeta) {
        BlockStateMeta state = (BlockStateMeta) originalMeta;
        BlockStateMeta stateCompare = (BlockStateMeta) compareMeta;
        if (state.getBlockState() instanceof ShulkerBox && stateCompare.getBlockState() instanceof ShulkerBox) {
          ShulkerBox shulker = (ShulkerBox)state.getBlockState();
          ShulkerBox shulkerCompare = (ShulkerBox)stateCompare.getBlockState();

          for(int i = 0; i < shulker.getInventory().getSize(); i++) {
            final ItemStack stack = shulker.getInventory().getItem(i);
            if(stack != null) {
              if(!MaterialUtils.itemsEqual(stack, shulkerCompare.getInventory().getItem(i))) return false;
            }
          }
          return true;
        }
        return false;
      }
      return false;
    } else if(original.getType().equals(Material.WRITTEN_BOOK) ||
        original.getType().equals(Material.WRITABLE_BOOK)) {
      if(originalMeta instanceof BookMeta && compareMeta instanceof BookMeta) {
        return new SerialItem(original).serialize().equals(new SerialItem(compare).serialize());
      }
      return false;
    } else if(original.getType().equals(Material.ENCHANTED_BOOK)) {
      if(originalMeta instanceof EnchantmentStorageMeta && compareMeta instanceof  EnchantmentStorageMeta) {
        return new SerialItem(original).serialize().equals(new SerialItem(compare).serialize());
      }
      return false;
    }
    TNE.debug("isSimilar: " + compare.isSimilar(original));
    if(!original.getType().equals(compare.getType())) return false;
    if(original.getDurability() != compare.getDurability()) return false;
    return true;
  }

  public static boolean isShulker(Material material) {
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

  public static void removeItem(Player player, ItemTier info) {
    TNE.debug("===== START MaterialUtils.removeItem =====");
    if(player == null) TNE.debug("Player is null");
    if(info == null) TNE.debug("info is null");
    if(player.getInventory() == null) TNE.debug("Player Inventory is null");

    List<Integer> remove = new ArrayList<>();

    for(int i = 0; i < player.getInventory().getContents().length; i++) {
      ItemStack stack = player.getInventory().getItem(i);

      if(stack == null) {
        //TNE.debug("Stack is null. Index: " + i);
      }
      if(itemsEqual(info.toStack(), stack)) remove.add(i);
    }

    for(Integer i : remove) {
      player.getInventory().setItem(i, null);
    }
    TNE.debug("===== END removedItem.getCount =====");
  }

  public static Integer getCount(Player player, ItemTier info) {
    TNE.debug("===== START MaterialUtils.getCount =====");
    if(player == null) TNE.debug("Player is null");
    if(info == null) TNE.debug("info is null");
    if(player.getInventory() == null) TNE.debug("Player Inventory is null");

    Integer value = 0;
    for(ItemStack stack : player.getInventory().getContents()) {
      if(itemsEqual(info.toStack(), stack)) {
        value += stack.getAmount();
      }
    }
    TNE.debug("===== END MaterialUtils.getCount =====");
    return value;
  }

  public static Integer getCount(Player player, ItemTier info, PlayerInventory inventory) {
    TNE.debug("===== START MaterialUtils.getCount =====");
    if(player == null) TNE.debug("Player is null");
    if(info == null) TNE.debug("info is null");
    Inventory inv = (inventory == null)? player.getInventory() : inventory;

    Integer value = 0;
    for(ItemStack stack : inv.getContents()) {
      if(itemsEqual(info.toStack(), stack)) {
        value += stack.getAmount();
      }
    }
    TNE.debug("===== END MaterialUtils.getCount =====");
    return value;
  }

  public static void spawnRandomFirework(final Location loc) {
    final Firework firework = (Firework) loc.getWorld().spawnEntity(loc, EntityType.FIREWORK);
    final FireworkMeta fireworkMeta = firework.getFireworkMeta();
    final Random random = new Random();
    final FireworkEffect effect = FireworkEffect.builder().flicker(random.nextBoolean()).withColor(getColor(random.nextInt(17) + 1)).withFade(getColor(random.nextInt(17) + 1)).with(FireworkEffect.Type.values()[random.nextInt(FireworkEffect.Type.values().length)]).trail(random.nextBoolean()).build();
    fireworkMeta.addEffect(effect);
    fireworkMeta.setPower(random.nextInt(2) + 1);
    firework.setFireworkMeta(fireworkMeta);
  }

  private static Color getColor(final int i) {
    switch (i) {
      case 1:
        return Color.AQUA;
      case 2:
        return Color.BLACK;
      case 3:
        return Color.BLUE;
      case 4:
        return Color.FUCHSIA;
      case 5:
        return Color.GRAY;
      case 6:
        return Color.GREEN;
      case 7:
        return Color.LIME;
      case 8:
        return Color.MAROON;
      case 9:
        return Color.NAVY;
      case 10:
        return Color.OLIVE;
      case 11:
        return Color.ORANGE;
      case 12:
        return Color.PURPLE;
      case 13:
        return Color.RED;
      case 14:
        return Color.SILVER;
      case 15:
        return Color.TEAL;
      case 16:
        return Color.WHITE;
      case 17:
        return Color.YELLOW;
    }
    return null;
  }
}