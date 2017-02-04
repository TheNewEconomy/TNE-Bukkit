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

import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.*;

/**
 * Created by creatorfromhell on 1/30/2017.
 **/
public class TNEInventory {

  public List<TNEInventoryAction> actionList = new ArrayList<>();
  public List<UUID> viewers = new ArrayList<>();
  public Map<String, Object> data = new HashMap<>();

  public boolean done = false;
  private String title = null;
  protected Material[] bannedItems = new Material[] {};
  protected int[] bannedSlots = new int[] {};
  protected int[] acceptableSlots = new int[] {};

  public boolean onOpen(UUID id) {
    return true;
  }

  public boolean onClick(UUID player, ClickType type, int slot, ItemStack item) {
    return true;
  }

  public boolean onClose(UUID id) {
    viewers.remove(id);
    if(viewers.size() == 0) {
      done = true;
    }
    return true;
  }
}