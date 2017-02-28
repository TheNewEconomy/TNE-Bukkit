package com.github.tnerevival.core.inventory.impl;

import com.github.tnerevival.TNE;
import com.github.tnerevival.account.IDFinder;
import com.github.tnerevival.account.Vault;
import com.github.tnerevival.core.inventory.TNEInventory;
import com.github.tnerevival.utils.MISCUtils;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Map;
import java.util.UUID;

/**
 * Created by creatorfromhell on 2/14/2017.
 * All rights reserved.
 **/
public class VaultInventory extends TNEInventory {
  public VaultInventory(UUID inventoryID, Inventory inventory, String world) {
    super(inventoryID, inventory, world);
  }

  @Override
  public boolean onOpen(UUID id) {
    MISCUtils.debug("Vault has been opened!");
    UUID owner = Vault.parseTitle(inventory.getTitle());
    Vault vault = TNE.instance().manager.accounts.get(owner).getVault(world);
    MISCUtils.debug("Vault owner is " + IDFinder.getPlayer(vault.getOwner().toString()).getName());
    if(!vault.viewers.contains(id)) {
      vault.viewers.add(id);
      TNE.instance().manager.accounts.get(owner).setVault(world, vault);
      MISCUtils.debug("ADDING VAULT VIEWER!!!!!!!!!!!!");
    }
    return super.onOpen(id);
  }

  @Override
  public void onUpdate(Map<Integer, ItemStack> changed, UUID player) {
    UUID owner = Vault.parseTitle(inventory.getTitle());
    Vault vault = TNE.instance().manager.accounts.get(owner).getVault(world);
    for(Map.Entry<Integer, ItemStack> entry : changed.entrySet()) {
      if(entry.getKey() >= inventory.getSize()) continue;
      if(entry.getValue() == null) vault.removeItem(entry.getKey());
      else vault.setItem(entry.getKey(), entry.getValue());
      inventory.setItem(entry.getKey(), entry.getValue());
    }
    vault.update(player);
    TNE.instance().manager.accounts.get(owner).setVault(world, vault);
  }

  @Override
  public void onClose(UUID id) {
    UUID owner = Vault.parseTitle(inventory.getTitle());
    Vault vault = TNE.instance().manager.accounts.get(owner).getVault(world);
    vault.viewers.remove(id);
    TNE.instance().manager.accounts.get(owner).setVault(world, vault);
    super.onClose(id);
  }
}