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
import org.bukkit.block.Chest;
import org.bukkit.block.DoubleChest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

import java.util.Date;
import java.util.HashMap;
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
  public void onOpen(final InventoryOpenEvent event) {
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
  public void onClick(final InventoryClickEvent event) {
    if(!(event.getWhoClicked() instanceof Player)) return;

    final Player player = (Player)event.getWhoClicked();
    boolean bottom = (event.getRawSlot() != event.getView().convertSlot(event.getRawSlot()));
    final int slot = event.getView().convertSlot(event.getRawSlot());
    final Inventory inventory = (bottom)? event.getView().getBottomInventory() : event.getView().getTopInventory();
    InventoryAction action = event.getAction();

    final InventoryView view = event.getView();
    final int rawSlot = event.getRawSlot();

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

    if(view.getTitle() != null && view.getTitle().toLowerCase().contains("vault")
       || view.getTopInventory().getHolder() != null && view.getTopInventory().getHolder() instanceof Chest
       || view.getTopInventory().getHolder() != null && view.getTopInventory().getHolder() instanceof DoubleChest) {

      final ItemStack originalItem = event.getCurrentItem();
      final ItemStack originalCursor = event.getCursor();

      Material involved = Material.AIR;

      MISCUtils.debug("Slot: " + rawSlot);
      if(action.equals(InventoryAction.NOTHING)) return;
      MISCUtils.debug("Item: " + originalItem.toString());
      MISCUtils.debug("Cursor: " + originalCursor.toString());
      if(action.equals(InventoryAction.COLLECT_TO_CURSOR)) {
        MISCUtils.debug("Collected to cursor");
      } else if(action.equals(InventoryAction.HOTBAR_MOVE_AND_READD)) {
        MISCUtils.debug("HOTBAR ACTION");
      } else {
        if (originalItem.getType().equals(Material.AIR)) {
          MISCUtils.debug("Player put block in slot");
        } else if (!originalItem.getType().equals(Material.AIR) && originalCursor.getType().equals(Material.AIR)) {
          MISCUtils.debug("Player took item");
        } else if (!originalCursor.getType().equals(Material.AIR) && originalItem.getType().equals(originalCursor.getType())) {
          MISCUtils.debug("Player added to stack from cursor");
        } else if (!originalCursor.getType().equals(Material.AIR) && !originalItem.getType().equals(Material.AIR)) {
          MISCUtils.debug("Player swapped items");
        }
      }
    }
  }

  @EventHandler
  public void onDrag(final InventoryDragEvent event) {
    final Player player = (Player)event.getWhoClicked();
    Map<Integer, ItemStack> original = new HashMap<>();
    for(int i : event.getRawSlots()) {
      ItemStack item = (event.getView().getItem(i) == null)? new ItemStack(Material.AIR) : event.getView().getItem(i);
      original.put(i, item);
    }
    Map<Integer, ItemStack> changed = event.getNewItems();
    MISCUtils.debug("onDrag: original-" + original.size() + " changed-" + changed.size());
  }

  @EventHandler
  public void onClose(final InventoryCloseEvent event) {
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