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
package com.github.tnerevival.worker;

import com.github.tnerevival.TNE;
import com.github.tnerevival.account.IDFinder;
import com.github.tnerevival.core.inventory.TNEInventoryAction;
import com.github.tnerevival.core.inventory.TNEInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

/**
 * Created by creatorfromhell on 1/31/2017.
 **/
public class InventoryUpdateWorker extends BukkitRunnable {

  private TNE plugin;
  private TNEInventory inventory;

  public InventoryUpdateWorker(TNE plugin, TNEInventory inventory) {
    this.plugin = plugin;
    this.inventory = inventory;
  }

  @Override
  public void run() {
    updateInventory();
    if(inventory.done) {
      this.cancel();
    }
  }

  private void updateInventory() {
    for(TNEInventoryAction action : inventory.actionList) {
      for(UUID id : inventory.viewers) {
        if(!id.equals(action.getPlayer())) {
          ItemStack stack = (action.isAddition())? action.getStack() : null;
          IDFinder.getPlayer(id.toString()).getOpenInventory().getTopInventory().setItem(action.getSlot(), stack);
        }
      }
    }
  }
}