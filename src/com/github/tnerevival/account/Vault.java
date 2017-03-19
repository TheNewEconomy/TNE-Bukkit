/*
 * The New Economy Minecraft Server Plugin
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.

 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.github.tnerevival.account;

import com.github.tnerevival.TNE;
import com.github.tnerevival.serializable.SerializableItemStack;
import com.github.tnerevival.utils.AccountUtils;
import com.github.tnerevival.utils.MISCUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by creatorfromhell on 1/17/2017.
 **/
public class Vault implements Serializable {
  public List<UUID> viewers = new ArrayList<>();

  private List<SerializableItemStack> items = new ArrayList<>();
  private List<UUID> members = new ArrayList<>();
  private UUID owner;
  private String world;
  private Integer size;

  public Vault(UUID owner, String world, Integer size) {
    this.owner = owner;
    this.world = world;
    this.size = size;
  }

  public void addItem(int slot, ItemStack stack) {
    items.add(new SerializableItemStack(slot, stack));
  }

  public void setItem(int slot, ItemStack stack) {
    removeItem(slot);
    if(stack != null) {
      addItem(slot, stack);
    }
  }

  public void setItems(Map<Integer, ItemStack> items) {
    for(Map.Entry<Integer, ItemStack> entry : items.entrySet()) {
      if(entry.getKey() >= size) continue;
      setItem(entry.getKey(), entry.getValue());
    }
  }


  public void setItems(ItemStack[] items) {
    for(int i = 0; i < items.length; i++) {
      ItemStack stack = items[i];
      if(stack == null || stack.getType().equals(Material.AIR)) {
        removeItem(i);
      } else {
        setItem(i, stack);
      }
    }
  }

  public SerializableItemStack getItem(int slot) {
    for(SerializableItemStack item : items) {
      if(item.getSlot().equals(slot)) {
        return item;
      }
    }
    return null;
  }

  public void removeAll(Material material) {
    Iterator<SerializableItemStack> i = items.iterator();

    while(i.hasNext()) {
      SerializableItemStack item = i.next();

      if(item.toItemStack().getType().equals(material)) {
        i.remove();
      }
    }
  }

  public void removeItem(int slot) {
    Iterator<SerializableItemStack> i = items.iterator();

    while(i.hasNext()) {
      SerializableItemStack item = i.next();

      if(item.getSlot().equals(slot)) {
        i.remove();
      }
    }
  }

  /**
   * @return the items
   */
  public List<SerializableItemStack> getItems() {
    return items;
  }

  /**
   * @param items the items to set
   */
  public void setItems(List<SerializableItemStack> items) {
    this.items = items;
  }

  public List<UUID> getMembers() {
    return members;
  }

  public void addMember(UUID player) {
    members.add(player);
  }

  public void removeMember(UUID player) {
    members.remove(player);
  }

  /**
   * @return the size
   */
  public Integer getSize() {
    return size;
  }

  /**
   * @param size the size to set
   */
  public void setSize(Integer size) {
    this.size = size;
  }

  public UUID getOwner() {
    return owner;
  }

  public String getWorld() {
    return world;
  }

  public void setWorld(String world) {
    this.world = world;
  }

  private List<Integer> validSlots(String world) {
    List<Integer> valid = new ArrayList<>();

    for(int i = 0; i < size; i++) {
      if(getItem(i) != null || TNE.instance().api().getBoolean("Core.Death.Vault.IncludeEmpty", world, owner)) {
        valid.add(i);
      }
    }
    return valid;
  }

  public List<Integer> generateSlots(String world) {
    List<Integer> valid = validSlots(world);
    List<Integer> generated = new ArrayList<>();
    int remaining = TNE.instance().api().getInteger("Core.Death.Vault.Drop", world, owner);

    if(valid.size() <= remaining) return valid;

    for(int i = 0; i < remaining; i++) {
      int gen = valid.get(ThreadLocalRandom.current().nextInt(0, valid.size()));
      generated.add(gen);
      valid.remove(gen);
    }
    return generated;
  }

  private String itemsToString() {
    StringBuilder builder = new StringBuilder();

    for(SerializableItemStack item : items) {
      if(item != null) {
        if(builder.length() > 0) builder.append("*");
        builder.append(item.toString());
      }
    }
    return builder.toString();
  }

  private String membersToString() {
    StringBuilder builder = new StringBuilder();
    for(UUID id : members) {
      if(builder.length() > 0) builder.append("*");
      builder.append(id.toString());
    }
    return builder.toString();
  }

  public void itemsFromString(String parse) {
    String[] parsed = parse.split("\\*");

    for (String s : parsed) {
      SerializableItemStack item = SerializableItemStack.fromString(s);
      if(!item.toItemStack().getType().equals(Material.AIR)) {
        items.add(SerializableItemStack.fromString(s));
      }
    }
  }

  public void membersFromString(String parse) {
    String[] parsed = parse.split("\\*");

    for(String s : parsed) {
      members.add(UUID.fromString(s));
    }
  }

  public static Vault fromString(String parse) {
    String[] parsed = parse.split(":");
    Vault vault = new Vault(UUID.fromString(parsed[0]), parsed[1], Integer.valueOf(parsed[2]));
    if(parsed.length >= 4) {
      vault.itemsFromString(parsed[3]);
    }
    if(parsed.length >= 5) {
      vault.membersFromString(parsed[4]);
    }

    return vault;
  }

  public String toString() {
    return owner.toString() + ":" + world + ":" + size + ":" + itemsToString() + ":" + membersToString();
  }

  public Inventory getInventory() {
    if(!AccountUtils.getAccount(owner).getStatus().getVault()) {
      return null;
    }
    MISCUtils.debug("OWNER UUID: " + owner.toString());
    MISCUtils.debug((IDFinder.getPlayer(owner.toString()) == null) + "");
    String title = ChatColor.GOLD + "[" + ChatColor.WHITE + "Vault" + ChatColor.GOLD + "]" + ChatColor.WHITE + IDFinder.getPlayer(owner.toString()).getName();
    Inventory inventory = Bukkit.createInventory(null, size, title);
    if(items.size() > 0) {
      for(SerializableItemStack stack : items) {
        inventory.setItem(stack.getSlot(), stack.toItemStack());
      }
    }
    return inventory;
  }

  public void update() {
    update(null);
  }

  public void update(UUID initiator) {
    for(UUID id : viewers) {
      if(initiator != null && !id.equals(initiator) || initiator == null) {
        IDFinder.getPlayer(id.toString()).getOpenInventory().getTopInventory().setContents(getInventory().getContents());
        IDFinder.getPlayer(id.toString()).updateInventory();
      }
    }
  }

  /*
   * Static helper methods
   */
  public static UUID parseTitle(String title) {
    String ownerUser = title.split("]")[1].trim();
    MISCUtils.debug("Vault.parseTitle" + ChatColor.stripColor(ownerUser));
    return IDFinder.getID(ChatColor.stripColor(ownerUser));
  }

  public static Integer size(String world, String player) {
    Integer rows = TNE.instance().api().getInteger("Core.Vault.Rows", world, player);
    return (rows >= 1 && rows <= 6) ? (rows * 9) : 27;
  }

  public static Boolean enabled(String world, String player) {
    return TNE.instance().api().getBoolean("Core.Vault.Enabled", world, player);
  }

  public static Boolean command(String world, String player) {
    return TNE.instance().api().getBoolean("Core.Vault.Command", world, player);
  }

  public static BigDecimal cost(String world, String player) {
    return new BigDecimal(TNE.instance().api().getDouble("Core.Vault.Cost", world, player));
  }

  public static Boolean sign(String world, String player) {
    return TNE.instance().api().getBoolean("Core.Signs.Vault.Enabled", world, player);
  }

  public static Boolean npc(String world) {
    return TNE.instance().api().getBoolean("Core.Vault.NPC", world);
  }
}