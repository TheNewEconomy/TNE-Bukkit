package com.github.tnerevival.core.inventory;

import org.bukkit.entity.Player;

/**
 * Created by creatorfromhell on 2/14/2017.
 * All rights reserved.
 **/
public enum InventoryType {
  VAULT("vault", "tne.inventory.vault.open"),
  SHOP("shop", "tne.inventory.shop.open"),
  AUCTION("auction", "tne.inventory.auction.open");

  private String identifier;
  private String permission;

  InventoryType(String identifier, String permission) {
    this.identifier = identifier;
    this.permission = permission;
  }

  public static InventoryType fromTitle(String title) {
    for(InventoryType type : values()) {
      if(title.toLowerCase().contains(title.toLowerCase())) return type;
    }
    return null;
  }

  public boolean canOpen(Player player) {
    return player.hasPermission(permission);
  }

  public String getIdentifier() {
    return identifier;
  }

  public void setIdentifier(String identifier) {
    this.identifier = identifier;
  }
}