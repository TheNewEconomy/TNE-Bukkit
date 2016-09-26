package com.github.tnerevival.core.inventory.impl;

import com.github.tnerevival.account.Bank;
import com.github.tnerevival.core.inventory.InventoryViewer;
import com.github.tnerevival.serializable.SerializableItemStack;
import com.github.tnerevival.utils.BankUtils;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Daniel on 9/13/2016.
 */
public class BankInventory extends GenericInventory {

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

    Bank b = BankUtils.getBank(owner, world);

    List<SerializableItemStack> items = new ArrayList<>();

    for(int i = 0; i < b.getSize(); i++) {
      if(inventory.getItem(i) != null && !inventory.getItem(i).getType().equals(Material.AIR))
      items.add(new SerializableItemStack(i, inventory.getItem(i)));
    }
    b.setItems(items);
    super.onClose(viewer);
  }
}
