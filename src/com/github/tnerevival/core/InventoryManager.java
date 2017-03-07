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
package com.github.tnerevival.core;

import com.github.tnerevival.TNE;
import com.github.tnerevival.account.IDFinder;
import com.github.tnerevival.account.credits.InventoryTimeTracking;
import com.github.tnerevival.core.inventory.InventoryType;
import com.github.tnerevival.core.inventory.TNEInventory;
import com.github.tnerevival.core.inventory.impl.VaultInventory;
import com.github.tnerevival.utils.MISCUtils;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

import java.util.*;

/**
 * Created by creatorfromhell on 1/19/2017.
 **/
public class InventoryManager {
  public Map<UUID, InventoryTimeTracking> inventoryTime = new HashMap<>();
  public Map<UUID, UUID> inventoryIDs = new HashMap<>();
  public Map<UUID, TNEInventory> inventories = new HashMap<>();

  public TNEInventory generateInventory(Inventory inventory, Player player, String world) {
    InventoryType type = InventoryType.fromTitle(inventory.getTitle());
    TNEInventory tneInventory = null;
    UUID inventoryID = null;

    if(type != null) {
      inventoryID = UUID.randomUUID();
      switch(type) {
        case AUCTION:
          tneInventory = new TNEInventory(inventoryID, inventory, world);
          break;
        case SHOP:
          tneInventory = new TNEInventory(inventoryID, inventory, world);
          break;
        case VAULT:
          tneInventory = new VaultInventory(inventoryID, inventory, world);
          break;
      }
    }
    if(tneInventory != null) {
      tneInventory.viewers.add(IDFinder.getID(player));
      inventoryIDs.put(IDFinder.getID(player), inventoryID);
      inventories.put(inventoryID, tneInventory);
    }
    return tneInventory;
  }

  public TNEInventory getInventory(Inventory inventory) {
    UUID inventoryID = getInventoryID(inventory);

    if(inventoryID != null) return inventories.get(inventoryID);
    return null;
  }

  public TNEInventory getInventory(UUID player) {
    return inventories.get(getInventoryID(player));
  }

  public UUID getInventoryID(Inventory inventory) {
    for(HumanEntity entity : inventory.getViewers()) {
      if(entity instanceof Player) {
        if(inventoryIDs.containsKey(IDFinder.getID((Player)entity))) return inventoryIDs.get(IDFinder.getID((Player)entity));
      }
    }
    return null;
  }

  public UUID getInventoryID(UUID player) {
    return inventoryIDs.get(player);
  }

  public void removePlayer(UUID player) {
    TNEInventory inventory = getInventory(player);
    if(inventory != null) {
      inventoryIDs.remove(player);
      if (inventory.viewers.size() == 1) {
        inventories.remove(inventory.getInventoryID());
        return;
      }
      inventory.viewers.remove(player);
      inventories.put(inventory.getInventoryID(), inventory);
    }
  }

  public static List<Integer> parseSlots(InventoryAction action, InventoryView view, int slot) {
    List<Integer> slots = new ArrayList<>();
    if(action.equals(InventoryAction.MOVE_TO_OTHER_INVENTORY) && slot >= view.getTopInventory().getSize()) {
      for(int i = 0; i < view.getTopInventory().getSize(); i++) {
        if(view.getTopInventory().getItem(i) == null || view.getTopInventory().getItem(i).getType().equals(Material.AIR)) slots.add(i);
      }
    } else if(action.equals(InventoryAction.COLLECT_TO_CURSOR)) {
      if(view.getTopInventory().getItem(slot) != null && !view.getTopInventory().getItem(slot).getType().equals(Material.AIR)) {
        Material mat = view.getTopInventory().getItem(slot).getType();
        for(int i = 0; i < view.getTopInventory().getSize(); i++) {
          if(view.getTopInventory().getItem(i) != null && view.getTopInventory().getItem(i).getType().equals(mat)) slots.add(i);
        }
      }
    } else {
      slots.add(slot);
    }
    return slots;
  }

  public static Map<Integer, ItemStack> getChanges(List<Integer> slots, Inventory old, Inventory current) {
    Map<Integer, ItemStack> changes = new HashMap<>();
    for(int i : slots) {
      if(i >= old.getSize() || old.getItem(i) == null && current.getItem(i) == null) continue;
      if(old.getItem(i) != null && current.getItem(i) == null) {
        changes.put(i, null);
      } else if(old.getItem(i) == null && current.getItem(i) != null) {
        changes.put(i, current.getItem(i));
      } else if(old.getItem(i).equals(current.getItem(i))) {
        changes.put(i, current.getItem(i));
      }
    }
    return changes;
  }

  public static void handleInventoryChanges(UUID player) {
    Inventory current = IDFinder.getPlayer(player.toString()).getOpenInventory().getTopInventory();
    TNEInventory tneInventory = TNE.instance().inventoryManager.getInventory(player);
    Inventory old = tneInventory.getInventory();

    if(old != null) {
      Map<Integer, ItemStack> changes = new HashMap<>();
      for(int i = 0; i < old.getSize(); i++) {
        String oldItem = (old.getItem(i) != null)? old.getItem(i).getType().toString() + ":" + old.getItem(i).getAmount() : "EMPTY";
        String newItem = (current.getItem(i) != null)? current.getItem(i).getType().toString() + ":" + current.getItem(i).getAmount() : "EMPTY";
        MISCUtils.debug(oldItem + " == " + newItem);
        if(i >= old.getSize() || old.getItem(i) == null && current.getItem(i) == null) continue;
        if(old.getItem(i) != null && current.getItem(i) == null) {
          changes.put(i, null);
        } else if(old.getItem(i) == null && current.getItem(i) != null) {
          changes.put(i, current.getItem(i));
        } else if(!old.getItem(i).equals(current.getItem(i))) {
          changes.put(i, current.getItem(i));
        }
      }
      handleInventoryChanges(player, changes);
    }
  }

  public static void handleInventoryChanges(UUID player, Map<Integer, ItemStack> changed) {
    TNEInventory old = TNE.instance().inventoryManager.getInventory(player);

    if(old != null) {
      if(changed.size() > 0) old.onUpdate(changed, player);
    }
  }
}
