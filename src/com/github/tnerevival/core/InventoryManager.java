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
import com.github.tnerevival.account.Vault;
import com.github.tnerevival.account.credits.InventoryTimeTracking;
import com.github.tnerevival.utils.MISCUtils;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by creatorfromhell on 1/19/2017.
 **/
public class InventoryManager {
  public Map<UUID, InventoryTimeTracking> inventoryTime = new HashMap<>();

  public static void handleInventoryDrag(UUID player, Map<Integer, ItemStack> changed, String world) {
    Inventory inventory = IDFinder.getPlayer(player.toString()).getOpenInventory().getTopInventory();
    if(inventory.getTitle() != null && inventory.getTitle().toLowerCase().contains("vault")) {
      UUID owner = Vault.parseTitle(inventory.getTitle());
      Vault vault = TNE.instance.manager.accounts.get(owner).getVault(world);
      vault.setItems(changed);
      vault.update(player);
      TNE.instance.manager.accounts.get(owner).setVault(world, vault);
    }
  }

  public static void handleAllCursor(UUID player, String world, Material material, Inventory previous) {
    Inventory inventory = IDFinder.getPlayer(player.toString()).getOpenInventory().getTopInventory();
    if(inventory.getTitle() != null && inventory.getTitle().toLowerCase().contains("vault")) {
      UUID owner = Vault.parseTitle(inventory.getTitle());
      Vault vault = TNE.instance.manager.accounts.get(owner).getVault(world);

      vault.removeAll(material);
      for(int i = 0; i < inventory.getSize(); i++) {
        if(previous.getItem(i) != null && previous.getItem(i).equals(material)) {
          if(inventory.getItem(i) == null || inventory.getItem(i) != null && !compareItems(previous.getItem(i), inventory.getItem(i))) {
            vault.setItem(i, inventory.getItem(i));
          }
        }
      }

      vault.update(player);
      TNE.instance.manager.accounts.get(owner).setVault(world, vault);
    }
  }

  public static void handleSlotChange(UUID player, String world, int slot, ItemStack previous) {
    Inventory inventory = IDFinder.getPlayer(player.toString()).getOpenInventory().getTopInventory();
    if(inventory.getTitle() != null && inventory.getTitle().toLowerCase().contains("vault")) {
      if(slot >= inventory.getSize()) return;
      UUID owner = Vault.parseTitle(inventory.getTitle());
      ItemStack current = inventory.getItem(slot);
      boolean change = false;
      if(current == null && previous == null) return;
      if(current != null && previous == null) {
        change = true;
      } else if(current == null) {
        change = true;
      } else {
        if(!compareItems(previous, current)) {
          change = true;
        }
      }

      if(change) {
        MISCUtils.debug("INVENTORY CHANGED!!!!!!!!!!!!!!!!!");
        Vault vault = TNE.instance.manager.accounts.get(owner).getVault(world);
        MISCUtils.debug("VAULT VIEWERS: " + vault.viewers.size());
        vault.setItem(slot, current);
        vault.update(player);
        TNE.instance.manager.accounts.get(owner).setVault(world, vault);
      }
    }
  }

  private static boolean compareItems(ItemStack previous, ItemStack current) {
    if(!previous.getType().equals(current.getType())) return false;
    if(previous.getAmount() != current.getAmount()) return false;
    if(previous.getDurability() != current.getDurability()) return false;
    if(!previous.getItemMeta().equals(current.getItemMeta())) return false;
    if(!previous.getEnchantments().equals(current.getEnchantments())) return false;
    return true;
  }
}
