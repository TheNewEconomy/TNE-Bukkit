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
import com.github.tnerevival.account.InventoryTimeTracking;
import com.github.tnerevival.core.Message;
import com.github.tnerevival.core.configurations.impl.ObjectConfiguration;
import com.github.tnerevival.core.transaction.TransactionType;
import com.github.tnerevival.utils.AccountUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

import java.util.Date;
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