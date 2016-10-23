package com.github.tnerevival.serializable;

import com.github.tnerevival.utils.MISCUtils;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.Serializable;
import java.util.*;

public class SerializableItemStack implements Serializable {
  private static final long serialVersionUID = 1L;

  HashMap<SerializableEnchantment, Integer> enchantments = new HashMap<SerializableEnchantment, Integer>();
  List<String> lore = new ArrayList<String>();

  String name;
  Integer slot;
  Integer amount;
  Short damage;
  String customName;

  public SerializableItemStack(Integer slot) {
    this.name = "TNENOSTRINGVALUE";
    this.slot = slot;
    this.customName = "TNENOSTRINGVALUE";
  }

  public SerializableItemStack(Integer slot, ItemStack i) {
    this.name = i.getType().name();
    this.slot = slot;
    this.amount = i.getAmount();
    this.damage = i.getDurability();
    this.customName = "TNENOSTRINGVALUE";
    if(i.hasItemMeta()) {
      if(i.getItemMeta().hasDisplayName()) {
        MISCUtils.debug("" + i.getItemMeta().getDisplayName());
        this.customName = i.getItemMeta().getDisplayName();
      }
      if(i.getItemMeta().hasLore()) {
        this.lore = i.getItemMeta().getLore();
      }
    }
    this.enchantments = getEnchantmentsFromStack(i);
    MISCUtils.debug("Name:" + this.getName() + "Lore:" + loreToString() + "damage:" + damage);
  }

  /**
   * @return the enchantments
   */
  public HashMap<SerializableEnchantment, Integer> getEnchantments() {
    return enchantments;
  }

  /**
   * @param enchantments the enchantments to set
   */
  public void setEnchantments(HashMap<SerializableEnchantment, Integer> enchantments) {
    this.enchantments = enchantments;
  }

  /**
   * @return the lore
   */
  public List<String> getLore() {
    return lore;
  }

  /**
   * @param lore the lore to set
   */
  public void setLore(List<String> lore) {
    this.lore = lore;
  }

  /**
   * @return the name
   */
  public String getName() {
    return name;
  }

  /**
   * @param name the name to set
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * @return the slot
   */
  public Integer getSlot() {
    return slot;
  }

  /**
   * @param slot the slot to set
   */
  public void setSlot(Integer slot) {
    this.slot = slot;
  }

  /**
   * @return the amount
   */
  public Integer getAmount() {
    return amount;
  }

  /**
   * @param amount the amount to set
   */
  public void setAmount(Integer amount) {
    this.amount = amount;
  }

  /**
   * @return the damage
   */
  public Short getDamage() {
    return damage;
  }

  /**
   * @param damage the damage to set
   */
  public void setDamage(Short damage) {
    this.damage = damage;
  }

  /**
   * @return the customName
   */
  public String getCustomName() {
    return customName;
  }

  /**
   * @param customName the customName to set
   */
  public void setCustomName(String customName) {
    this.customName = customName;
  }

  public String enchantmentsToString() {
    if(!enchantments.isEmpty()) {
      String toReturn = "";
      Iterator<java.util.Map.Entry<SerializableEnchantment, Integer>> it = enchantments.entrySet().iterator();

      int count = 0;
      while(it.hasNext()) {
        java.util.Map.Entry<SerializableEnchantment, Integer> entry = it.next();
        if(count > 0) { toReturn += "~"; }

        toReturn += entry.getKey().name + "," + entry.getValue();
        count++;
      }
      return toReturn;
    }
    return "TNENOSTRINGVALUE";
  }

  public String loreToString() {
    if(!lore.isEmpty()) {
      String toReturn = "";

      int count = 0;
      for(String s : lore) {
        if(count > 0) { toReturn += "~"; }
        toReturn += s;
        count++;
      }
      return toReturn;
    }
    return "TNENOSTRINGVALUE";
  }

  public ItemMeta getItemMeta() {
    return toItemStack().getItemMeta();
  }

  public HashMap<SerializableEnchantment, Integer> getEnchantmentsFromStack(ItemStack i) {
    Map<Enchantment, Integer> enchantments = i.getEnchantments();
    HashMap<SerializableEnchantment, Integer> serializedEnchantments = new HashMap<SerializableEnchantment, Integer>();
    for(Enchantment e : enchantments.keySet()) {
      serializedEnchantments.put(new SerializableEnchantment(e), enchantments.get(e));
    }
    return serializedEnchantments;
  }

  Map<Enchantment, Integer> getEnchantmentsFromSerializable() {
    Map<Enchantment, Integer> enchants = new HashMap<>();
    for(SerializableEnchantment e : enchantments.keySet()) {
      if(e != null) {
        enchants.put(e.getEnchantment(), enchantments.get(e.name));
      }
    }
    return enchants;
  }

  public ItemStack toItemStack() {
    if(!this.name.equalsIgnoreCase("TNENOSTRINGVALUE")) {
      ItemStack stack = new ItemStack(Material.getMaterial(name), this.amount, this.damage);

      for(SerializableEnchantment enchant : enchantments.keySet()) {
        if(enchant != null) {
          stack.addUnsafeEnchantment(enchant.getEnchantment(), enchantments.get(enchant));
        }
      }

      MISCUtils.debug("Name:" + this.getName() + "Lore:" + loreToString() + "damage:" + damage + "Amount:" + amount);

      ItemMeta stackMeta = stack.getItemMeta();
      if(!this.lore.isEmpty()) {
        stackMeta.setLore(this.lore);
      }
      if(!this.customName.equals(null) && !this.customName.equals("TNENOSTRINGVALUE") && !this.customName.isEmpty()) {
        stackMeta.setDisplayName(this.customName);
      }
      stack.setItemMeta(stackMeta);
      return stack;
    }
    return null;
  }

  @Override
  public  String toString() {
    return name + ";" + slot + ";" + amount + ";" + damage + ";" + customName + ";" + loreToString() + ";" + enchantmentsToString();
  }

  public static SerializableItemStack fromString(String itemString) {
    String[] variables = itemString.split("\\;");
    if(variables.length < 5) return new SerializableItemStack(0, new ItemStack(Material.AIR));
    SerializableItemStack stack = new SerializableItemStack(Integer.valueOf(variables[1]));
    stack.setName(variables[0]);
    stack.setAmount(Integer.valueOf(variables[2]));
    stack.setDamage(Short.valueOf(variables[3]));
    if(variables[4] != null && !variables[4].equals("TNENOSTRINGVALUE")) {
      stack.setCustomName(variables[4]);
    }

    if(variables[5] != null && !variables[5].equals("TNENOSTRINGVALUE")) {
      stack.setLore(Arrays.asList(variables[5].split("\\~")));
    }

    if(variables[6] != null && !variables[6].equals("TNENOSTRINGVALUE")) {
      HashMap<SerializableEnchantment, Integer> enchantments = new HashMap<SerializableEnchantment, Integer>();
      String[] enchantmentsArray = variables[6].split("\\~");

      for(String s : enchantmentsArray) {
        String[] enchantmentVariables = s.split("\\,");
        if(enchantmentVariables.length == 2) {
          enchantments.put(new SerializableEnchantment(enchantmentVariables[0]), Integer.valueOf(enchantmentVariables[1]));
        }
      }
      stack.setEnchantments(enchantments);
    }
    return stack;
  }
}