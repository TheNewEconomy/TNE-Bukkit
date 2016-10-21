package com.github.tnerevival.account;

import com.github.tnerevival.TNE;
import com.github.tnerevival.serializable.SerializableItemStack;
import org.bukkit.Material;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class Bank implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private List<SerializableItemStack> items = new ArrayList<>();
	private List<UUID> members = new ArrayList<>();
	private UUID owner;
	private Integer size;
	private Double gold;
	
	public Bank(UUID owner, Integer size) {
	  this(owner, size, 0.0);
	}
	
	public Bank(UUID owner, Integer size, Double gold) {
		this.owner = owner;
		this.size = size;
		this.gold = gold;
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

	/**
	 * @return the gold
	 */
	public Double getGold() {
		return gold;
	}

	public UUID getOwner() {
		return owner;
	}

	/**
	 * @param gold the gold to set
	 */
	public void setGold(Double gold) {
		this.gold = gold;
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
    b.setGold(Double.valueOf(parsed[2]));
    if(parsed.length >= 4) {
      b.itemsFromString(parsed[3]);
    }
		if(parsed.length >= 5) {
			b.membersFromString(parsed[4]);
		}

    return b;
  }
	
	public String toString() {
		return owner.toString() + ":" + size + ":" + gold + ":" + itemsToString() + members.toString();
	}
}