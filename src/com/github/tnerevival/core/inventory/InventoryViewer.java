package com.github.tnerevival.core.inventory;

import com.github.tnerevival.serializable.SerializableItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Daniel on 9/3/2016.
 */
public class InventoryViewer {

  /**
   * Used to track the operations this viewer has performed on the inventory for update purposes.
   */
  private Map<SerializableItemStack, InventoryOperation> operations = new HashMap<>();

  /**
   * The UUID of the player viewing the inventory.
   */
  private UUID viewer;

  /**
   * Whether or not the inventory should be saved when the viewer closes it. This helps to prevent saving for admin viewing, etc.
   */
  private boolean save;

  /**
   * Used to track the time this player has been using the inventory for time-based inventory charges.
   */
  private long opened;

  /**
   * Used to determine if the inventory should be closed due to insufficient inventory credits.
   */
  private boolean close;

  public UUID getUUID() {
    return viewer;
  }

  public Map<SerializableItemStack, InventoryOperation> getOperations() {
    return operations;
  }

  public boolean willSave() {
    return save;
  }

  public boolean willClose() {
    return close;
  }
}