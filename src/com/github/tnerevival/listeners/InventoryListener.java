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
package com.github.tnerevival.listeners;

import com.github.tnerevival.TNE;
import com.github.tnerevival.account.IDFinder;
import com.github.tnerevival.account.Vault;
import com.github.tnerevival.account.credits.InventoryTimeTracking;
import com.github.tnerevival.core.InventoryManager;
import com.github.tnerevival.core.Message;
import com.github.tnerevival.core.configurations.impl.ObjectConfiguration;
import com.github.tnerevival.core.event.object.InteractionType;
import com.github.tnerevival.core.event.object.TNEObjectInteractionEvent;
import com.github.tnerevival.core.transaction.TransactionType;
import com.github.tnerevival.utils.AccountUtils;
import com.github.tnerevival.utils.MISCUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Date;
import java.util.Map;
import java.util.UUID;

/**
 * Created by creatorfromhell on 1/19/2017.
 **/
public class InventoryListener implements Listener {

  private TNE plugin;

  public InventoryListener(TNE plugin) {
    this.plugin = plugin;
  }

  @EventHandler
  public void onOpen(InventoryOpenEvent event) {
    UUID player = IDFinder.getID((Player)event.getPlayer());
    String world = IDFinder.getWorld(player);
    Inventory inventory = event.getInventory();
    InventoryType type = inventory.getType();

    if(inventory.getTitle() == null || inventory.getTitle() != null && !inventory.getTitle().toLowerCase().contains("shop")
       && !inventory.getTitle().toLowerCase().contains("vault") && !inventory.getTitle().toLowerCase().contains("auction")) {
      ObjectConfiguration config = TNE.configurations.getObjectConfiguration();
      if(config.inventoryEnabled(type, world, player.toString())) {
        if(config.isTimed(type, world, player.toString())) {
          if(!plugin.inventoryManager.inventoryTime.containsKey(player)) {
            if(AccountUtils.getAccount(player).getTimeLeft(world, config.inventoryType(type)) <= 0) {
              Message unable = new Message("Messages.Package.Unable");
              unable.addVariable("$type", config.inventoryType(type));
              unable.translate(world, player);
              return;
            }
            InventoryTimeTracking tracking = new InventoryTimeTracking(player);
            tracking.setClosed(false);
            tracking.setType(type);
            tracking.setOpened(new Date().getTime());
            plugin.inventoryManager.inventoryTime.put(tracking.getPlayer(), tracking);
            return;
          }
          return;
        }

        if(!AccountUtils.transaction(player.toString(), null, config.getInventoryCost(type, world, player.toString()), TransactionType.MONEY_INQUIRY, world)) {
          Message insufficient = new Message("Messages.Money.Insufficient");
          insufficient.addVariable("$amount", config.getInventoryCost(type, world, player.toString()) + "");
          insufficient.translate(world, player);
          return;
        }
        AccountUtils.transaction(player.toString(), null, config.getInventoryCost(type, world, player.toString()), TransactionType.MONEY_REMOVE, world);
        Message charged = new Message("Messages.Inventory.Charge");
        charged.addVariable("$amount", config.getInventoryCost(type, world, player.toString()) + "");
        charged.addVariable("$type", config.inventoryType(type));
        charged.translate(world, player);
      }
    }

    if(inventory.getTitle() != null) {
      if(inventory.getTitle().toLowerCase().contains("shop")) {

      } else if(inventory.getTitle().toLowerCase().contains("vault")) {
        MISCUtils.debug("Vault has been opened!");
        UUID owner = Vault.parseTitle(inventory.getTitle());
        Vault vault = TNE.instance.manager.accounts.get(owner).getVault(world);
        if(!vault.viewers.contains(player)) {
          vault.viewers.add(player);
          TNE.instance.manager.accounts.get(owner).setVault(world, vault);
          MISCUtils.debug("ADDING VAULT VIEWER!!!!!!!!!!!!");
        }
      } else if(inventory.getTitle().toLowerCase().contains("auction")) {

      }
    }
  }

  @EventHandler
  public void onClick(InventoryClickEvent event) {
    final Inventory inventory = event.getInventory();
    Player player = (Player)event.getWhoClicked();
    UUID id = IDFinder.getID(player);
    String world = IDFinder.getWorld(id);
    int slot = event.getRawSlot();
    boolean top = event.getRawSlot() < event.getView().getTopInventory().getSize();

    MISCUtils.debug("Slot " + slot);

    if(inventory.getType().equals(InventoryType.ENCHANTING) || inventory.getType().equals(InventoryType.FURNACE)) {
      MISCUtils.debug("Inventory is enchanting OR smelting");
      InteractionType intType = (inventory.getType().equals(InventoryType.ENCHANTING))? InteractionType.ENCHANT : InteractionType.SMELTING;
      TNEObjectInteractionEvent e = new TNEObjectInteractionEvent(player, event.getCurrentItem(), event.getCurrentItem().getType().name(), intType);
      Bukkit.getServer().getPluginManager().callEvent(e);

      MISCUtils.debug("Event called");
      MISCUtils.handleObjectEvent(e);

      MISCUtils.debug("Event handled internally");
      if(e.isCancelled()) {
        MISCUtils.debug("Event Cancelled");
        event.setCancelled(true);
      }
      MISCUtils.debug("Exiting click event");
    }

    boolean tracked = inventory.getTitle() != null && inventory.getTitle().toLowerCase().contains("vault");
    /*if(tracked) {
      if(event.getAction().equals(InventoryAction.MOVE_TO_OTHER_INVENTORY)) {
        if(slot < inventory.getSize()) {
          sendSlotChange(id, world, slot, inventory.getItem(slot));
        } else {
          sendMoveOther(id, world, event.getCurrentItem());
        }
      } else if(event.getAction().equals(InventoryAction.COLLECT_TO_CURSOR)) {
        sendCollectCursor(id, world, slot, event.getCursor(), event.getView().getTopInventory());
      } else if(!event.getAction().equals(InventoryAction.NOTHING) && slot < inventory.getSize()) {
        sendSlotChange(id, world, slot, event.getCurrentItem());
      }
    }*/
    if(inventory.getTitle() != null) {
      if(inventory.getTitle().toLowerCase().contains("vault")) {
        UUID owner = Vault.parseTitle(inventory.getTitle());
        Vault vault = TNE.instance.manager.accounts.get(owner).getVault(world);

        if(!event.getAction().equals(InventoryAction.COLLECT_TO_CURSOR) && slot < inventory.getSize()) {
          final UUID uid = id;
          final int fslot = slot;
          final ItemStack stack = inventory.getItem(slot);
          final String fworld = world;
          Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
            @Override
            public void run() {
              InventoryManager.handleSlotChange(uid, fworld, fslot, stack);
            }
          }, 1L);
        } else if(event.getAction().equals(InventoryAction.COLLECT_TO_CURSOR)) {
          final UUID uid = id;
          final Material material = event.getView().getItem(slot).getType();
          final String fworld = world;
          Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
            @Override
            public void run() {
              InventoryManager.handleAllCursor(uid, fworld, material);
            }
          }, 1L);
        }
      }
    }
  }

  @EventHandler
  public void onDrag(InventoryDragEvent event) {
    final UUID id = IDFinder.getID((Player)event.getWhoClicked());
    final Map<Integer, ItemStack> changed = event.getNewItems();
    final String world = IDFinder.getWorld(id);
    Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
      @Override
      public void run() {
        InventoryManager.handleInventoryDrag(id, changed, world);
      }
    }, 1L);
  }

  @EventHandler
  public void onClose(InventoryCloseEvent event) {
    UUID player = IDFinder.getID((Player)event.getPlayer());
    InventoryType type = event.getInventory().getType();

    if(plugin.inventoryManager.inventoryTime.containsKey(player)) {
      InventoryTimeTracking tracking = plugin.inventoryManager.inventoryTime.get(player);
      tracking.setClosed(true);
      tracking.setCloseTime(new Date().getTime());
    }
  }

}