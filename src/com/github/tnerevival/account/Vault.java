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
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by creatorfromhell on 1/17/2017.
 **/
public class Vault {

  private List<SerializableItemStack> items = new ArrayList<>();
  private List<UUID> members = new ArrayList<>();
  private UUID owner;
  private Integer size;

  public Vault(UUID owner, Integer size) {
    this.owner = owner;
    this.size = size;
  }

  public void addItem(int slot, ItemStack stack) {
    items.add(new SerializableItemStack(slot, stack));
  }

  public SerializableItemStack getItem(int slot) {
    for(SerializableItemStack item : items) {
      if(item.getSlot().equals(slot)) {
        return item;
      }
    }
    return null;
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

  private List<Integer> validSlots(String world) {
    List<Integer> valid = new ArrayList<>();

    for(int i = 0; i < size; i++) {
      if(getItem(i) != null || TNE.instance.api.getBoolean("Core.Death.Bank.IncludeEmpty", world, owner)) {
        valid.add(i);
      }
    }
    return valid;
  }

  public List<Integer> generateSlots(String world) {
    List<Integer> valid = validSlots(world);
    List<Integer> generated = new ArrayList<>();
    int remaining = TNE.instance.api.getInteger("Core.Death.Bank.Drop", world, owner);

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

  public static Bank fromString(String parse) {
    String[] parsed = parse.split(":");
    Bank b = new Bank(UUID.fromString(parsed[0]), Integer.valueOf(parsed[1]));
    if(parsed.length >= 3) {
      b.itemsFromString(parsed[2]);
    }
    if(parsed.length >= 4) {
      b.membersFromString(parsed[3]);
    }

    return b;
  }

  public String toString() {
    return owner.toString() + ":" + size + ":" + itemsToString() + ":" + membersToString();
  }
}