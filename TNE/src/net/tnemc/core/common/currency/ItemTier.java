package net.tnemc.core.common.currency;

import net.tnemc.core.TNE;
import net.tnemc.core.common.material.MaterialHelper;
import net.tnemc.core.common.utils.MISCUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

/**
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by Daniel on 7/7/2017.
 */
public class ItemTier {

  private List<String> enchantments = new ArrayList<>();
  private List<String> flags = new ArrayList<>();

  private String material;
  private short damage;
  private String name;
  private String lore;
  private Integer customModel = null;

  public ItemTier(String material) {
    this(material, (short)0);
  }

  public ItemTier(String material, short damage) {
    this.material = material;
    this.damage = damage;
    this.name = null;
    this.lore = "";
  }

  public List<String> getEnchantments() {
    return enchantments;
  }

  public void setEnchantments(List<String> enchantments) {
    this.enchantments = enchantments;
  }

  public List<String> getFlags() {
    return flags;
  }

  public void setFlags(List<String> flags) {
    this.flags = flags;
  }

  public String getMaterial() {
    return material;
  }

  public void setMaterial(String material) {
    this.material = material;
  }

  public short getDamage() {
    return damage;
  }

  public void setDamage(short damage) {
    this.damage = damage;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getLore() {
    return lore;
  }

  public void setLore(String lore) {
    this.lore = lore;
  }

  public int getCustomModel() {
    return customModel;
  }

  public void setCustomModel(int customModel) {
    this.customModel = customModel;
  }

  public ItemStack toStack() {
    //TNE.debug("========= START ItemTier.toStack ===========");
    ItemStack stack = new ItemStack(MaterialHelper.getMaterial(material));
    stack.setDurability(damage);
    ItemMeta meta = (stack.hasItemMeta() && stack.getItemMeta() != null)? stack.getItemMeta() : Bukkit.getServer().getItemFactory().getItemMeta(stack.getType());

    if(meta != null) {
      List<String> itemLore = (meta.getLore() != null) ? meta.getLore() : new ArrayList<>();

      if (name != null && !name.trim().equals(""))
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
      if (lore != null && !lore.trim().equals("")) itemLore.add(ChatColor.translateAlternateColorCodes('&', lore));
      meta.setLore(itemLore);

      if (customModel != null && MISCUtils.isOneFourteen()) {
        meta.setCustomModelData(customModel);
      }

      for (String str : flags) {
        try {
          final ItemFlag flag = ItemFlag.valueOf(str);
          meta.addItemFlags(flag);
        } catch (Exception ignore) {
          TNE.debug("Invalid ItemFlag name: " + str);
        }
      }

      stack.setItemMeta(meta);
    }


    //TNE.debug("ENCHANTMENTS SIZE: " + enchantments.size());
    if(enchantments.size() > 0) {
      enchantments.forEach((name)->{
        Enchantment enchantment = TNE.item().find(name.toLowerCase());
        //TNE.debug("ENCHANTMENT NULL: " + (enchantment == null));
        if(enchantment == null) {
          //TNE.debug("Unable to apply enchantment to item tier: " + name);
        } else {
          stack.addUnsafeEnchantment(enchantment, 1);
        }
      });
    }
    //TNE.debug("========= END ItemTier.toStack ===========");
    return stack;
  }
}