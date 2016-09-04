package com.github.tnerevival.core.inventory;

/**
 * Created by Daniel on 9/3/2016.
 */
public enum InventoryOperation {

  /**
   * Denotes items added to the inventory.
   */
  ADD,
  /**
   * Denotes items removed from the inventory.
   */
  REMOVE,
  /**
   * Denotes when all items and counts remain the same, but some items are in different slot locations.
   */
  SORT
}