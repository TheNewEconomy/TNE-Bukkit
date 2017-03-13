package com.github.tnerevival.core.inventory.impl;

import com.github.tnerevival.core.inventory.TNEInventory;
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
    //TODO: Check for any tracked items in chest and change accordingly.
  }
}