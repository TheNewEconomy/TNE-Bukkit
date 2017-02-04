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

import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.inventory.ItemStack;

import java.util.Date;
import java.util.UUID;

/**
 * Created by creatorfromhell on 1/31/2017.
 **/
public class TNEInventoryAction {
  private long time;
  private UUID player;
  private boolean addition;
  private int slot;
  private ItemStack stack;

  public TNEInventoryAction(UUID player, boolean addition, int slot, ItemStack stack) {
    this.time = new Date().getTime();
    this.player = player;
    this.addition = addition;
    this.slot = slot;
    this.stack = stack;
  }

  public UUID getPlayer() {
    return player;
  }

  public void setPlayer(UUID player) {
    this.player = player;
  }

  public boolean isAddition() {
    return addition;
  }

  public void setAddition(boolean addition) {
    this.addition = addition;
  }

  public int getSlot() {
    return slot;
  }

  public void setSlot(int slot) {
    this.slot = slot;
  }

  public ItemStack getStack() {
    return stack;
  }

  public void setStack(ItemStack stack) {
    this.stack = stack;
  }

  public static boolean addition(boolean top, ClickType type, InventoryAction action) {
    return top && action.equals(InventoryAction.DROP_ALL_CURSOR);
  }
}