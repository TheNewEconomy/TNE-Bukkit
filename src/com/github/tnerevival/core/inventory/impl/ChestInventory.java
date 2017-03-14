package com.github.tnerevival.core.inventory.impl;

import com.github.tnerevival.core.inventory.TNEInventory;
import com.github.tnerevival.utils.AccountUtils;
import com.github.tnerevival.utils.MISCUtils;
import org.bukkit.Location;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Map;
import java.util.UUID;

/**
 * Created by creatorfromhell on 3/12/2017.
 * All rights reserved.
 **/
public class ChestInventory extends TNEInventory {
  public Location location = null;

  public ChestInventory(UUID inventoryID, Inventory inventory, Location location) {
    super(inventoryID, inventory, location.getWorld().getName());
    this.location = location;
  }

  @Override
  public boolean onOpen(UUID player) {
    MISCUtils.debug("Chest Inventory instance open!");
    return true;
  }

  @Override
  public void onUpdate(Map<Integer, ItemStack> changed, UUID player) {
    String world = location.getWorld().getName();
    for(Map.Entry<Integer, ItemStack> entry : changed.entrySet()) {
      boolean remove = false;
      if(entry.getKey() >= inventory.getSize()) continue;
      if(entry.getValue() == null) remove = true;

      if(remove && AccountUtils.trackedMaterial(location, entry.getKey()) != null) {
        MISCUtils.debug("Removing tracked material");
        AccountUtils.removeTracked(location, entry.getKey());
      }

      if(!remove && AccountUtils.trackedMaterial(world, entry.getValue().getType())) {
        MISCUtils.debug("Adding tracked material.");
        AccountUtils.track(player, location, entry.getKey(), entry.getValue().getType());
      }
    }
  }
}