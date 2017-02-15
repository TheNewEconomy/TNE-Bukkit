package com.github.tnerevival.core.inventory.impl;

import com.github.tnerevival.TNE;
import com.github.tnerevival.account.IDFinder;
import com.github.tnerevival.account.Vault;
import com.github.tnerevival.core.inventory.TNEInventory;
import com.github.tnerevival.utils.MISCUtils;
import org.bukkit.inventory.Inventory;

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
    Vault vault = TNE.instance.manager.accounts.get(owner).getVault(world);
    MISCUtils.debug("Vault owner is " + IDFinder.getPlayer(vault.getOwner().toString()).getName());
    if(!vault.viewers.contains(id)) {
      vault.viewers.add(id);
      TNE.instance.manager.accounts.get(owner).setVault(world, vault);
      MISCUtils.debug("ADDING VAULT VIEWER!!!!!!!!!!!!");
    }
    return true;
  }
}