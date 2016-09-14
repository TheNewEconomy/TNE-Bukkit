package com.github.tnerevival.core.inventory.impl;

import com.github.tnerevival.core.inventory.InventoryViewer;
import com.github.tnerevival.core.inventory.TNEInventory;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Daniel on 9/13/2016.
 */
public class BankInventory extends TNEInventory {

  private UUID owner;

  public BankInventory(UUID owner) {
    this.owner = owner;
  }

  @Override
  public List<Material> getBlacklisted() {
    List<Material> blocked = new ArrayList<>();
    blocked.add(Material.ENCHANTED_BOOK);
    blocked.add(Material.SPECTRAL_ARROW);
    blocked.add(Material.TIPPED_ARROW);
    blocked.add(Material.POTION);
    blocked.add(Material.LINGERING_POTION);
    blocked.add(Material.SPLASH_POTION);

    return blocked;
  }

  @Override
  public List<Integer> getValidSlots() {
    return new ArrayList<>();
  }

  @Override
  public List<Integer> getInvalidSlots() {
    return new ArrayList<>();
  }

  @Override
  public void onClose(InventoryViewer viewer) {
    //TODO: Save bank.
    super.onClose(viewer);
  }
}
