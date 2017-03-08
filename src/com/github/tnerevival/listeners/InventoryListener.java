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
import com.github.tnerevival.account.credits.InventoryTimeTracking;
import com.github.tnerevival.core.InventoryManager;
import com.github.tnerevival.core.Message;
import com.github.tnerevival.core.configurations.impl.ObjectConfiguration;
import com.github.tnerevival.core.event.object.InteractionType;
import com.github.tnerevival.core.event.object.TNEObjectInteractionEvent;
import com.github.tnerevival.core.inventory.TNEInventory;
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


    if(inventory.getTitle() != null && inventory.getTitle().toLowerCase().contains("vault")) {
      boolean open = TNE.instance().inventoryManager.getInventory(player) == null;
      if(!open && TNE.instance().inventoryManager.getInventory(inventory) != null) {
        TNEInventory inv = TNE.instance().inventoryManager.getInventory(inventory);
        if(inv.viewers.size() >= TNE.instance().api().getInteger("Core.Vault.MaxViewers")) {
          event.setCancelled(true);
        }
      }

      if(!event.isCancelled()) {
        TNEInventory tneInventory = (TNE.instance().inventoryManager.getInventory(inventory) != null) ? TNE.instance().inventoryManager.getInventory(inventory) : TNE.instance().inventoryManager.generateInventory(inventory, (Player) event.getPlayer(), world);

        if (tneInventory != null && open) {
          event.setCancelled(!tneInventory.onOpen(player));
        }
      }
      if(event.isCancelled()) {
        new Message("Messages.Vault.Occupied").translate(world, event.getPlayer());
      }
    }
  }

  @EventHandler
  public void onClick(InventoryClickEvent event) {
    final Inventory inventory = event.getInventory();
    final Player player = (Player)event.getWhoClicked();
    final UUID id = IDFinder.getID(player);
    String world = IDFinder.getWorld(id);
    int slot = event.getRawSlot();
    boolean top = event.getRawSlot() < event.getView().getTopInventory().getSize();
    InventoryAction action = event.getAction();
    final Inventory clicked = (slot >= inventory.getSize())? player.getInventory() : inventory;
    final int clickedSlot = slot;
    final ItemStack stack = clicked.getItem(clickedSlot);
    final ItemStack cursor = event.getCursor();

    String cursorString = (cursor == null)? "null" : cursor.toString();

    MISCUtils.debug("Cursor Item: " + cursorString);
    MISCUtils.debug("Slot Raw " + clickedSlot);
    MISCUtils.debug("Slot " + event.getSlot());

    if(inventory.getType().equals(InventoryType.ENCHANTING) || inventory.getType().equals(InventoryType.FURNACE)) {
      MISCUtils.debug("Inventory is enchanting OR smelting");
      InteractionType intType = (inventory.getType().equals(InventoryType.ENCHANTING))? InteractionType.ENCHANT : InteractionType.SMELTING;
      String item = (event.getCurrentItem() != null)? event.getCurrentItem().getType().name() : Material.AIR.name();
      TNEObjectInteractionEvent e = new TNEObjectInteractionEvent(player, event.getCurrentItem(), item, intType);
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
    Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
      @Override
      public void run() {
        ItemStack current = clicked.getItem(clickedSlot);
        ItemStack cursor = player.getOpenInventory().getCursor();

        String cursorString = (cursor == null)? "null" : cursor.toString();

        MISCUtils.debug("Cursor Item: " + cursorString);
        String original = (stack == null)? "empty" : stack.toString();
        String currentString = (current == null)? "empty" : current.toString();
        MISCUtils.debug("Original ItemStack: " + original);
        MISCUtils.debug("New ItemStack: " + currentString);
      }
    }, 1L);
  }

  @EventHandler
  public void onDrag(InventoryDragEvent event) {
    final UUID id = IDFinder.getID((Player)event.getWhoClicked());
    final Map<Integer, ItemStack> changed = event.getNewItems();
    final String world = IDFinder.getWorld(id);
    Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
      @Override
      public void run() {
        InventoryManager.handleInventoryChanges(id, changed);
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

    if(TNE.instance().inventoryManager.getInventory(player) != null) {
      TNE.instance().inventoryManager.getInventory(player).onClose(player);
      TNE.instance().inventoryManager.removePlayer(player);
    }
  }
}