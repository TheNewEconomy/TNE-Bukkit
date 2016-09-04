package com.github.tnerevival.core.inventory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Daniel on 9/2/2016.
 */
public abstract class TNEInventory {
  public List<InventoryViewer> viewers = new ArrayList<>();

  /**
   * An array of valid slots for inventories that don't allow players to use majority slots.
   * @return
   */
  public abstract int[] getValidSlots();

  /**
   * An array of slots that a player cannot interact with due to some restriction and/or feature of the inventory.
   * @return
   */
  public abstract int[] getInvalidSlots();
}