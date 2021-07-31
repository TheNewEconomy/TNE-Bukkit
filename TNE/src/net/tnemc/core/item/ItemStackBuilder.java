package net.tnemc.core.item;

import com.google.common.collect.Multimap;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.Map;

public class ItemStackBuilder {

  private ItemStack stack;
  private ItemMeta meta;

  public ItemStackBuilder(Material material) {
    this.stack = new ItemStack(material);
    this.meta = stack.getItemMeta();
  }

  public ItemStack getStack() {
    return stack;
  }

  public static ItemStackBuilder create(Material material) {
    return new ItemStackBuilder(material);
  }

  public ItemStackBuilder setAmount(int amount) {
    stack.setAmount(amount);
    return this;
  }

  public ItemStackBuilder setDamage(short damage) {
    stack.setDurability(damage);
    return this;
  }

  public ItemStackBuilder setName(String name) {
    meta.setDisplayName(name);
    stack.setItemMeta(meta);
    return this;
  }

  public ItemStackBuilder setLore(List<String> lore) {
    meta.setLore(lore);
    stack.setItemMeta(meta);
    return this;
  }

  public ItemStackBuilder setCustomModelData(int modelData) {
    meta.setCustomModelData(modelData);
    stack.setItemMeta(meta);
    return this;
  }

  public ItemStackBuilder addEnchantment(Enchantment enchant, int level, boolean unsafe) {
    if(unsafe) {
      stack.addUnsafeEnchantment(enchant, level);
      return this;
    }
    stack.addEnchantment(enchant, level);
    return this;
  }

  public ItemStackBuilder addEnchantments(Map<Enchantment, Integer> enchantments, boolean unsafe) {
    if(unsafe) {
      stack.addUnsafeEnchantments(enchantments);
      return this;
    }
    stack.addEnchantments(enchantments);
    return this;
  }

  public ItemStackBuilder addItemFlags(ItemFlag... flags) {
    meta.addItemFlags(flags);
    stack.setItemMeta(meta);
    return this;
  }

  public ItemStackBuilder addAttributeModifier(Attribute attribute, AttributeModifier modifier) {
    meta.addAttributeModifier(attribute, modifier);
    stack.setItemMeta(meta);
    return this;
  }

  public ItemStackBuilder setAttributeModifiers(Multimap<Attribute, AttributeModifier> modifiers) {
    meta.setAttributeModifiers(modifiers);
    stack.setItemMeta(meta);
    return this;
  }
}