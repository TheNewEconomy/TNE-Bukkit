package com.github.tnerevival.listeners;

import com.github.tnerevival.TNE;
import com.github.tnerevival.account.IDFinder;
import com.github.tnerevival.core.inventory.InventoryViewer;
import com.github.tnerevival.core.inventory.TNEInventory;
import com.github.tnerevival.core.inventory.impl.BankInventory;
import com.github.tnerevival.core.inventory.impl.GenericInventory;
import com.github.tnerevival.core.inventory.impl.ShopInventory;
import com.github.tnerevival.core.shops.Shop;
import com.github.tnerevival.utils.MISCUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;

import java.util.UUID;

public class InventoryListener implements Listener {

  TNE plugin;

  public InventoryListener(TNE plugin) {
    this.plugin = plugin;
  }

  @EventHandler
  public void onInventoryOpen(InventoryOpenEvent event) {
    Player player = (Player)event.getPlayer();
    InventoryType type = event.getInventory().getType();
    InventoryViewer viewer = new InventoryViewer(IDFinder.getID(player), MISCUtils.getWorld(player));
    TNEInventory inventory = new GenericInventory();

    MISCUtils.debug(player.getDisplayName() + " opened an inventory!" + event.getInventory().getTitle());

    if(TNE.instance.inventoryManager.isViewing(IDFinder.getID(player))) {

      UUID id = IDFinder.getID(player);
      MISCUtils.debug(id.toString());
      TNE.instance.inventoryManager.getViewing(IDFinder.getID(player)).onOpen(
          TNE.instance.inventoryManager.getViewer(IDFinder.getID(player))
      );
      return;
    }

    if(event.getInventory().getTitle() != null && event.getInventory().getTitle().toLowerCase().contains("bank")) {
      String ownerUser = event.getInventory().getTitle().split("]")[1].trim();

      MISCUtils.debug(ChatColor.stripColor(ownerUser));

      UUID owner = IDFinder.getID(ChatColor.stripColor(ownerUser));
      inventory = new BankInventory(owner);
    } else if(event.getInventory().getTitle() != null && event.getInventory().getTitle().toLowerCase().contains("shop")) {
      String name = event.getInventory().getTitle().split("]")[1].trim();
      Shop s = TNE.instance.manager.shops.get(ChatColor.stripColor(name) + ":" + player.getWorld().getName());

      MISCUtils.debug(ChatColor.stripColor(name));
      MISCUtils.debug(player.getUniqueId().toString());
      inventory = new ShopInventory(s);
      s.addShopper(IDFinder.getID(player));
    }
    inventory.addViewer(viewer);
    inventory.setInventory(event.getInventory());
    inventory.setWorld(MISCUtils.getWorld(player));
    for(HumanEntity entity : event.getViewers()) {
      inventory.addViewer(new InventoryViewer(entity.getUniqueId(), MISCUtils.getWorld(entity.getUniqueId())));
    }
    if(!inventory.onOpen(viewer)) {
      event.setCancelled(true);
      return;
    }
    TNE.instance.inventoryManager.addInventory(inventory, viewer);
  }

  @EventHandler
  public void onInventoryClose(InventoryCloseEvent event) {
    Player player = (Player) event.getPlayer();
    if(TNE.instance.inventoryManager.isViewing(IDFinder.getID(player))) {
      InventoryViewer viewer = TNE.instance.inventoryManager.getViewer(IDFinder.getID(player));
      TNE.instance.inventoryManager.getViewing(IDFinder.getID(player)).onClose(viewer);
      TNE.instance.inventoryManager.removeViewer(viewer.getUUID());
    }
  }

  @EventHandler
  public void onInventoryClick(InventoryClickEvent event) {
    Player player = (Player)event.getWhoClicked();
    int slot = event.getRawSlot();

    MISCUtils.debug("" + slot);

    if(TNE.instance.inventoryManager.isViewing(IDFinder.getID(player))) {
      InventoryViewer viewer = TNE.instance.inventoryManager.getViewer(IDFinder.getID(player));
      if(!TNE.instance.inventoryManager.getViewing(IDFinder.getID(player)).onClick(viewer, event.getClick(), slot, event.getCurrentItem())) {
        event.setCancelled(true);
      }
    }
  }
}