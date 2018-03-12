package net.tnemc.core.item;

import net.tnemc.core.TNE;
import net.tnemc.core.item.data.*;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by Daniel on 11/10/2017.
 */
public class SerialItem {

  private Map<String, Integer> enchantments = new HashMap<>();
  private List<String> lore = new ArrayList<>();

  private Material material;
  private Integer amount;
  private String display;
  private short damage;
  private SerialItemData data;

  //Cache=related variables
  private ItemStack stack;

  public SerialItem(ItemStack item) {
    stack = item;
    material = stack.getType();
    amount = stack.getAmount();
    damage = stack.getDurability();

    if(stack.hasItemMeta()) {
      display = stack.getItemMeta().getDisplayName();
      lore = stack.getItemMeta().getLore();

      if(stack.getItemMeta().hasEnchants()) {
        stack.getItemMeta().getEnchants().forEach(((enchantment, level) ->{
          enchantments.put(enchantment.getName(), level);
        }));
      }
    }
    buildData(stack);
  }

  private void buildData(ItemStack stack) {
    if(isShulker(stack.getType())) {
      TNE.debug("Is shulker!!");
      data = new ShulkerData();
    } else {
      ItemMeta meta = stack.getItemMeta();
      if (meta instanceof SpawnEggMeta) {
        data = new SpawnEggData();
      } else if (meta instanceof PotionMeta) {
        data = new SerialPotionData();
      } else if (meta instanceof BookMeta) {
        data = new BookData();
      } else if (meta instanceof BannerMeta) {
        data = new BannerData();
      } else if (meta instanceof LeatherArmorMeta) {
        data = new LeatherData();
      } else if (meta instanceof SkullMeta) {
        data = new SkullData();
      } else if (meta instanceof MapMeta) {
        data = new MapData();
      } else if (meta instanceof EnchantmentStorageMeta) {
        data = new EnchantStorageData();
      } else if (meta instanceof FireworkMeta) {
        data = new FireworkData();
      } else if (meta instanceof FireworkEffectMeta) {
        data = new FireworkEffectData();
      }
    }
    if(data != null){
      TNE.debug("Data != null");
      data.initialize(stack);
    }
  }

  public Map<String, Integer> getEnchantments() {
    return enchantments;
  }

  public void setEnchantments(Map<String, Integer> enchantments) {
    this.enchantments = enchantments;
  }

  public List<String> getLore() {
    return lore;
  }

  public void setLore(List<String> lore) {
    this.lore = lore;
  }

  public Material getMaterial() {
    return material;
  }

  public void setMaterial(Material material) {
    this.material = material;
  }

  public Integer getAmount() {
    return amount;
  }

  public void setAmount(Integer amount) {
    this.amount = amount;
  }

  public String getDisplay() {
    return display;
  }

  public void setDisplay(String display) {
    this.display = display;
  }

  public short getDamage() {
    return damage;
  }

  public void setDamage(short damage) {
    this.damage = damage;
  }

  public SerialItemData getData() {
    return data;
  }

  public void setData(SerialItemData data) {
    this.data = data;
  }

  public ItemStack getStack() {
    if(stack == null) {
      stack = new ItemStack(material, amount, damage);
      ItemMeta meta = Bukkit.getItemFactory().getItemMeta(material);
      if(display != null) {
        meta.setDisplayName(display);
      }
      meta.setLore(lore);
      enchantments.forEach((name, level)->{
        meta.addEnchant(Enchantment.getByName(name), level, true);
      });
      stack.setItemMeta(meta);
      if(data != null) {
        stack = data.build(stack);
      }
    }
    return stack;
  }

  public void setStack(ItemStack stack) {
    this.stack = stack;
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
           material.equals(Material.SILVER_SHULKER_BOX) ||
           material.equals(Material.CYAN_SHULKER_BOX) ||
           material.equals(Material.PURPLE_SHULKER_BOX) ||
           material.equals(Material.BLUE_SHULKER_BOX) ||
           material.equals(Material.BROWN_SHULKER_BOX) ||
           material.equals(Material.GREEN_SHULKER_BOX) ||
           material.equals(Material.RED_SHULKER_BOX) ||
           material.equals(Material.BLACK_SHULKER_BOX);
  }
}