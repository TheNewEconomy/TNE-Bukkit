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
import org.bukkit.inventory.ItemFlag;
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
    return formatMaterialNameWithSpace(material.name());
  }

  public static String formatMaterialNameWithSpace(String name) {
    String[] wordsSplit = name.split("_");
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
    TNE.debug("Compare Null: " + (compare == null));
    if(compare == null) return false;
    ItemStack originalClone = original.clone();
    originalClone.setAmount(1);
    
    ItemStack compareClone = compare.clone();
    compareClone.setAmount(1);
    
    TNE.debug("Enchant Size: " + originalClone.getEnchantments().size());
    TNE.debug("Enchant Size: " + originalClone.getItemMeta().getEnchants().size());
    ItemMeta originalCloneMeta = originalClone.getItemMeta();
    ItemMeta compareCloneMeta = compareClone.getItemMeta();
    TNE.debug("Has Meta?: " + originalClone.hasItemMeta());
    TNE.debug("Has Meta?: " + compareClone.hasItemMeta());
    if(compareClone.hasItemMeta()) {
      TNE.debug("Meta");
      if (compareCloneMeta.hasDisplayName()) {
        TNE.debug("display");
        TNE.debug("display1 " + compareCloneMeta.getDisplayName());
        TNE.debug("display2 " + originalCloneMeta.getDisplayName());
        if (!originalCloneMeta.hasDisplayName()) return false;
        TNE.debug("compareClone Display");
        if (!originalCloneMeta.getDisplayName().equalsIgnoreCase(compareCloneMeta.getDisplayName())) return false;
      } else {
        if(originalCloneMeta.hasDisplayName()) return false;
      }

      TNE.debug("lore");
      if (compareCloneMeta.hasLore()) {
        TNE.debug("lorez");
        if (!originalCloneMeta.hasLore()) return false;
        TNE.debug("compareClone lorez");
        TNE.debug(String.join(", " + originalCloneMeta.getLore()));
        TNE.debug(String.join(", " + compareCloneMeta.getLore()));
        if (!originalCloneMeta.getLore().containsAll(compareCloneMeta.getLore())) return false;
      } else {
        if(originalCloneMeta.hasLore()) return false;
      }

      if(MISCUtils.isOneFourteen()) {
        if (compareCloneMeta.hasCustomModelData()) {
          if (!originalCloneMeta.hasCustomModelData()) return false;

          if (compareCloneMeta.getCustomModelData() != originalCloneMeta.getCustomModelData()) return false;
        } else {
          if (originalCloneMeta.hasCustomModelData()) return false;
        }
      }

      for(ItemFlag flag : compareCloneMeta.getItemFlags()) {
        if(!originalCloneMeta.getItemFlags().contains(flag)) return false;
      }

      TNE.debug("enchant");
      TNE.debug("Enchant Size: " + originalClone.getEnchantments().size());
      TNE.debug("Enchant Size: " + originalCloneMeta.getEnchants().size());
      if (compareCloneMeta.hasEnchants()) {
        if (!originalCloneMeta.hasEnchants()) return false;

        TNE.debug("enchantz");
        for (Map.Entry<Enchantment, Integer> entry : compareClone.getEnchantments().entrySet()) {
          TNE.debug("start");
          if (!originalClone.containsEnchantment(entry.getKey())) return false;
          TNE.debug("contains");
          if (originalClone.getEnchantmentLevel(entry.getKey()) != entry.getValue()) return false;
          TNE.debug("level");
        }
      } else {
        if(originalCloneMeta.hasEnchants()) return false;
      }
    } else {
      if(originalClone.hasItemMeta()) return false;
    }

    if(isShulker(originalClone.getType())) {
      if(originalCloneMeta instanceof BlockStateMeta && compareCloneMeta instanceof  BlockStateMeta) {
        BlockStateMeta state = (BlockStateMeta) originalCloneMeta;
        BlockStateMeta statecompareClone = (BlockStateMeta) compareCloneMeta;
        if (state.getBlockState() instanceof ShulkerBox && statecompareClone.getBlockState() instanceof ShulkerBox) {
          ShulkerBox shulker = (ShulkerBox)state.getBlockState();
          ShulkerBox shulkercompareClone = (ShulkerBox)statecompareClone.getBlockState();

          for(int i = 0; i < shulker.getInventory().getSize(); i++) {
            final ItemStack stack = shulker.getInventory().getItem(i);
            if(stack != null) {
              if(!MaterialUtils.itemsEqual(stack, shulkercompareClone.getInventory().getItem(i))) return false;
            }
          }
          return true;
        }
        return false;
      }
      return false;
    } else if(originalClone.getType().equals(Material.WRITTEN_BOOK) ||
        MISCUtils.isOneThirteen() && originalClone.getType().equals(Material.WRITABLE_BOOK)) {
      if(originalCloneMeta instanceof BookMeta && compareCloneMeta instanceof BookMeta) {
        return new SerialItem(originalClone).serialize().equals(new SerialItem(compareClone).serialize());
      }
      return false;
    } else if(originalClone.getType().equals(Material.ENCHANTED_BOOK)) {
      if(originalCloneMeta instanceof EnchantmentStorageMeta && compareCloneMeta instanceof  EnchantmentStorageMeta) {
        TNE.debug("Enchant Book: " + new SerialItem(compareClone).serialize());
        TNE.debug("Enchant Book: " + new SerialItem(originalClone).serialize());
        return new SerialItem(originalClone).serialize().equals(new SerialItem(compareClone).serialize());
      }
      return false;
    }
    //TNE.debug("isSimilar: " + compareClone.isSimilar(originalClone));
    if(!originalClone.getType().equals(compareClone.getType())) return false;
    if(originalClone.getDurability() != compareClone.getDurability()) return false;
    return true;
  }

  public static boolean isShulker(Material material) {
    return TNE.item().isShulker(material);
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