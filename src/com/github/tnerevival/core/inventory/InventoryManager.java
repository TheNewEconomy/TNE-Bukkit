package com.github.tnerevival.core.inventory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Daniel on 9/13/2016.
 */
public class InventoryManager {

  public Map<UUID, Integer> accessing = new HashMap<>();
  public Map<Integer, TNEInventory> viewed = new HashMap<>();

  public void addInventory(TNEInventory inventory, InventoryViewer viewer) {
    for(InventoryViewer view : inventory.getViewers()) {
      if(accessing.containsKey(view.getUUID())) {
        viewed.get(accessing.get(view.getUUID())).addViewer(viewer);
        return;
      }
    }

    Integer id = getFree();
    accessing.put(viewer.getUUID(), id);
    viewed.put(id, inventory);
  }

  public Integer getFree() {
    for(int i = 1; i <= viewed.size(); i++) {
      if(!viewed.containsKey(i)) {
        return i;
      }
    }
    return viewed.size() + 1;
  }

  public InventoryViewer getViewer(UUID id) {
    List<InventoryViewer> viewers =  viewed.get(accessing.get(id)).getViewers();

    for(InventoryViewer viewer : viewers) {
      if(viewer.getUUID().equals(id)) {
        return viewer;
      }
    }
    return null;
  }

  public void removeViewer(UUID id) {
    Integer invID = accessing.get(id);
    viewed.get(invID).removeViewer(id);

    if(viewed.get(invID).viewers.size() == 0) {
      viewed.remove(invID);
    }
    accessing.remove(id);
  }

  public boolean isViewing(UUID id) {
    return accessing.containsKey(id);
  }

  public TNEInventory getViewing(UUID viewer) {
    return viewed.get(accessing.get(viewer));
  }
}
