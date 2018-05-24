package net.tnemc.core.common.utils;

import net.tnemc.core.TNE;
import net.tnemc.core.common.currency.ItemTier;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
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
    ItemMeta originalMeta = original.getItemMeta();
    ItemMeta compareMeta = compare.getItemMeta();
    if(compare.hasItemMeta()) {
      if (compareMeta.hasDisplayName()) {
        if (!originalMeta.hasDisplayName()) return false;
        if (!originalMeta.getDisplayName().equalsIgnoreCase(compareMeta.getDisplayName())) return false;
      }

      if (compareMeta.hasLore()) {
        if (!originalMeta.hasLore()) return false;
        if (!originalMeta.getLore().containsAll(compareMeta.getLore())) return false;
      }

      if (compareMeta.hasEnchants()) {
        if (!originalMeta.hasEnchants()) return false;

        for (Map.Entry<Enchantment, Integer> entry : compare.getEnchantments().entrySet()) {
          if (!original.containsEnchantment(entry.getKey())) return false;
          if (original.getEnchantmentLevel(entry.getKey()) != entry.getValue()) return false;
        }
      }
    }
    return compare.isSimilar(original);
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
    if(inv == null) TNE.debug("Player Inventory is null");

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