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
package com.github.tnerevival.core.inventory;

import com.github.tnerevival.account.IDFinder;
import com.github.tnerevival.utils.MISCUtils;
import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

import java.util.*;

/**
 * Created by creatorfromhell on 1/30/2017.
 **/
public class TNEInventory {

  public static List<String> watchList = new ArrayList<> (Arrays.asList("vault", "auction", "shop"));

  public List<UUID> viewers = new ArrayList<>();
  public Map<String, Object> data = new HashMap<>();

  protected UUID inventoryID;
  protected Inventory inventory;
  protected String world;

  protected Material[] bannedItems = new Material[] {
      Material.ENCHANTED_BOOK,
      Material.WRITTEN_BOOK,
      Material.POTION,
  };

  protected int[] bannedSlots = new int[] {};
  protected int[] acceptableSlots = new int[] {};

  public TNEInventory(UUID inventoryID, Inventory inventory, String world) {
    this.inventoryID = inventoryID;
    this.inventory = inventory;
    this.world = world;

    if(MISCUtils.isOneEleven()) {
      bannedItems = new Material[] {
          Material.ENCHANTED_BOOK,
          Material.WRITTEN_BOOK,
          Material.POTION,
          Material.SPLASH_POTION,
          Material.LINGERING_POTION,
          Material.TIPPED_ARROW,
          Material.SPECTRAL_ARROW,
          Material.WHITE_SHULKER_BOX,
          Material.ORANGE_SHULKER_BOX,
          Material.MAGENTA_SHULKER_BOX,
          Material.LIGHT_BLUE_SHULKER_BOX,
          Material.YELLOW_SHULKER_BOX,
          Material.LIME_SHULKER_BOX,
          Material.PINK_SHULKER_BOX,
          Material.GRAY_SHULKER_BOX,
          Material.SILVER_SHULKER_BOX,
          Material.CYAN_SHULKER_BOX,
          Material.PURPLE_SHULKER_BOX,
          Material.BLUE_SHULKER_BOX,
          Material.BROWN_SHULKER_BOX,
          Material.GREEN_SHULKER_BOX,
          Material.RED_SHULKER_BOX,
          Material.BLACK_SHULKER_BOX,
      };
    } else if(MISCUtils.isOneNine()) {
      bannedItems = new Material[] {
          Material.ENCHANTED_BOOK,
          Material.WRITTEN_BOOK,
          Material.POTION,
          Material.SPLASH_POTION,
          Material.LINGERING_POTION,
          Material.TIPPED_ARROW,
          Material.SPECTRAL_ARROW,
      };
    }
  }

  public boolean onOpen(UUID id) {
    if(InventoryType.fromTitle(inventory.getTitle()) != null && !InventoryType.fromTitle(inventory.getTitle()).canOpen(IDFinder.getPlayer(id.toString()))) return false;
    viewers.add(id);
    return true;
  }

  public boolean onClick(UUID player, ClickType type, int slot, ItemStack item) {
    if(bannedSlots.length > 0 && Arrays.asList(bannedSlots).contains(slot) ||
       acceptableSlots.length > 0 && !Arrays.asList(acceptableSlots).contains(slot)) return false;
    return Arrays.asList(bannedItems).contains(item.getType());
  }

  public void onUpdate(Map<Integer, ItemStack> changed, UUID player) {
    for(Map.Entry<Integer, ItemStack> entry : changed.entrySet()) {
      if(entry.getKey() >= inventory.getSize()) continue;
      inventory.setItem(entry.getKey(), entry.getValue());
    }
  }

  /*public Map<Integer, ItemStack> doShift(UUID id, int previousSlot, ItemStack stack) {
    Map<Integer, ItemStack> changes = new HashMap<>();
    InventoryView view = IDFinder.getPlayer(id.toString()).getOpenInventory();
    boolean top = view.convertSlot(previousSlot) == previousSlot;
    int slot = (top)? view.getTopInventory().firstEmpty() : firstEmpty(view);
    if(slot != -1) {
      if(!top) {
        inventory.setItem(slot, stack);
        view.setItem(previousSlot, new ItemStack(Material.AIR));
        IDFinder.getPlayer(id.toString()).updateInventory();
        changes.put(slot, stack);
      } else {
        inventory.setItem(previousSlot, new ItemStack(Material.AIR));
        view.setItem(slot, stack);
        IDFinder.getPlayer(id.toString()).updateInventory();
        changes.put(previousSlot, new ItemStack(Material.AIR));
      }
    }
    return changes;
  }*/

  public Map<Integer, ItemStack> doCollect(UUID id, Material material) {
    Map<Integer, ItemStack> changes = new HashMap<>();
    InventoryView view = IDFinder.getPlayer(id.toString()).getOpenInventory();

    for(int i = 0; i < inventory.getSize(); i++) {
      ItemStack old = inventory.getItem(i);
      ItemStack current = view.getItem(i);
      String oldString = (old == null)? "Air" : old.toString();
      String currentString = (old == null)? "Air" : current.toString();
      MISCUtils.debug("Slot: " + i);
      MISCUtils.debug("Old: " + oldString);
      MISCUtils.debug("Current: " + currentString);
      if(old != null && old.getType().equals(material)) {
        if(current == null || !current.equals(old)) {
          changes.put(i, current);
        }
      }
    }
    return changes;
  }

  public int firstEmpty(InventoryView view) {
    for(int i = view.getTopInventory().getSize(); i < view.countSlots(); i++) {
      ItemStack stack = view.getItem(i);
      if(stack == null || stack.getType().equals(Material.AIR)) return i;
    }
    return -1;
  }

  public void onClose(UUID id) {
    viewers.remove(id);
  }

  public UUID getInventoryID() {
    return inventoryID;
  }

  public void setInventoryID(UUID inventoryID) {
    this.inventoryID = inventoryID;
  }

  public Inventory getInventory() {
    return inventory;
  }

  public void setInventory(Inventory inventory) {
    this.inventory = inventory;
  }
}