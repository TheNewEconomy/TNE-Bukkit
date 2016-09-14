package com.github.tnerevival.core.inventory;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Daniel on 9/13/2016.
 */
public class InventoryManager {

  private Map<UUID, Integer> accessing = new HashMap<>();
  private Map<Integer, TNEInventory> viewed = new HashMap<>();

  public Integer getFree() {
    for(int i = 1; i <= viewed.size(); i++) {
      if(!viewed.containsKey(i)) {
        return i;
      }
    }
    return viewed.size() + 1;
  }

  public TNEInventory getViewing(UUID viewer) {
    return viewed.get(accessing.get(viewer));
  }
}
