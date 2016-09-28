package com.github.tnerevival.core.inventory.impl;

import com.github.tnerevival.core.inventory.InventoryViewer;
import com.github.tnerevival.core.shops.Shop;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

/**
 * Created by Daniel on 9/28/2016.
 */
public class ShopInventory extends GenericInventory {

  private Shop s;

  public ShopInventory(Shop s) {
    this.s = s;
  }

  @Override
  public boolean onClick(InventoryViewer viewer, ClickType type, int slot, ItemStack item) {
    return false;
  }
}
