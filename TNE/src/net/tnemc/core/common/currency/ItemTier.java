package net.tnemc.core.common.currency;

import net.tnemc.core.TNE;
import net.tnemc.core.common.material.MaterialHelper;
import net.tnemc.core.common.utils.MISCUtils;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

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
 * Created by Daniel on 7/7/2017.
 */
public class ItemTier {

  private Map<String, String> enchantments = new HashMap<>();

  private String material;
  private short damage;
  private String name;
  private String lore;

  public ItemTier(String material) {
    this(material, (short)0);
  }

  public ItemTier(String material, short damage) {
    this.material = material;
    this.damage = damage;
    this.name = null;
    this.lore = "";
  }

  public Map<String, String> getEnchantments() {
    return enchantments;
  }

  public void setEnchantments(Map<String, String> enchantments) {
    this.enchantments = enchantments;
  }

  public void addEnchantment(String name, String level) {
    this.enchantments.put(name, level);
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

  public ItemStack toStack() {
    //TNE.debug("========= START ItemTier.toStack ===========");
    ItemStack stack = new ItemStack(MaterialHelper.getMaterial(material));
    stack.setDurability(damage);
    ItemMeta meta = (stack.hasItemMeta())? stack.getItemMeta() : Bukkit.getServer().getItemFactory().getItemMeta(stack.getType());
    List<String> itemLore = (meta != null && meta.getLore() != null)? meta.getLore() : new ArrayList<>();

    /*if(stack == null) TNE.debug("stack is null");
    TNE.debug("Material: " + stack.getType().name());
    if(meta == null) TNE.debug("meta is null");
    if(itemLore == null) TNE.debug("itemLore is null");
    if(name == null) TNE.debug("name is null");
    if(lore == null) TNE.debug("lore is null");*/

    if(name != null && !name.trim().equals("")) meta.setDisplayName(name);
    if(lore != null && !lore.trim().equals("")) itemLore.add(lore);
    meta.setLore(itemLore);
    stack.setItemMeta(meta);

    if(enchantments.size() > 0) {
      enchantments.forEach((name, level)->{
        Enchantment enchantment = Enchantment.getByKey(NamespacedKey.minecraft(name));
        if(enchantment == null || !MISCUtils.isInteger(level)) {
          TNE.logger().info("Unable to apply enchantment to item tier: " + name);
        } else {
          stack.addEnchantment(enchantment, Integer.valueOf(level));
        }
      });
    }
    //TNE.debug("========= END ItemTier.toStack ===========");
    return stack;
  }
}