package com.github.tnerevival.core.event.object;

import com.github.tnerevival.TNE;
import com.github.tnerevival.core.objects.BlockObject;
import com.github.tnerevival.core.objects.ItemObject;

/**
 * Created by Daniel on 10/12/2016.
 */
public enum InteractionType {
  MINING("MiningCharged", "MiningPaid"),
  PLACING("PlacingCharged", "PlacingPaid"),
  ITEM("ItemUseCharged", "ItemUsePaid"),
  ENCHANT("EnchantingCharged", "EnchantingPaid"),
  SMELTING("SmeltingCharged", "SmeltingPaid"),
  CRAFTING("CraftingCharged", "CraftingPaid");

  private String charged;
  private String paid;

  InteractionType(String charged, String paid) {
    this.charged = charged;
    this.paid = paid;
  }

  public double getCost(String identifier) {
    switch(this) {
      case MINING:
        if(TNE.configurations.getMaterialsConfiguration().containsBlock(identifier)) {
          return TNE.configurations.getMaterialsConfiguration().getBlock(identifier).getMine();
        }
        return 0.0;
      case PLACING:
        if(TNE.configurations.getMaterialsConfiguration().containsBlock(identifier)) {
          return TNE.configurations.getMaterialsConfiguration().getBlock(identifier).getPlace();
        }
        return 0.0;
      case ITEM:
        if(TNE.configurations.getMaterialsConfiguration().containsItem(identifier)) {
          return TNE.configurations.getMaterialsConfiguration().getItem(identifier).getUse();
        }
        return 0.0;
      case ENCHANT:
        if(TNE.configurations.getMaterialsConfiguration().containsItem(identifier)) {
          return TNE.configurations.getMaterialsConfiguration().getItem(identifier).getEnchant();
        }
        return 0.0;
      case CRAFTING:
        if(getBlock(identifier) != null) {
          return getBlock(identifier).getCrafting();
        }

        if(getItem(identifier) != null) {
          return getItem(identifier).getCrafting();
        }
        return 0.0;
      case SMELTING:
        if(getBlock(identifier) != null) {
          return getBlock(identifier).getSmelt();
        }

        if(getItem(identifier) != null) {
          return getItem(identifier).getSmelt();
        }
        return 0.0;
      default:
        return 0.0;
    }
  }

  private BlockObject getBlock(String identifier) {
    if(TNE.configurations.getMaterialsConfiguration().containsBlock(identifier)) {
      return TNE.configurations.getMaterialsConfiguration().getBlock(identifier);
    }
    return null;
  }

  private ItemObject getItem(String identifier) {
    if(TNE.configurations.getMaterialsConfiguration().containsItem(identifier)) {
      return TNE.configurations.getMaterialsConfiguration().getItem(identifier);
    }
    return null;
  }

  public String getCharged() {
    return charged;
  }

  public String getPaid() {
    return paid;
  }
}