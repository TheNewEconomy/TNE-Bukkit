package com.github.tnerevival.core.event.object;

import com.github.tnerevival.TNE;
import com.github.tnerevival.core.objects.MaterialObject;

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

  public double getCost(String identifier, String world, String player) {
    switch(this) {
      case MINING:
        if(containsMaterial(identifier, world, player)) {
          return getMaterial(identifier, world, player).getMine();
        }
      case PLACING:
        if(containsMaterial(identifier, world, player)) {
          return getMaterial(identifier, world, player).getPlace();
        }
      case ITEM:
        if(containsMaterial(identifier, world, player)) {
          return getMaterial(identifier, world, player).getUse();
        }
      case ENCHANT:
        if(containsMaterial(identifier, world, player)) {
          return getMaterial(identifier, world, player).getEnchant();
        }
      case CRAFTING:
        if(containsMaterial(identifier, world, player)) {
          return getMaterial(identifier, world, player).getCrafting();
        }
      case SMELTING:
        if(containsMaterial(identifier, world, player)) {
          return getMaterial(identifier, world, player).getSmelt();
        }
      default:
        return 0.0;
    }
  }

  private Boolean containsMaterial(String identifier, String world, String player) {
    return TNE.configurations.getMaterialsConfiguration().containsMaterial(identifier, world, player);
  }

  private MaterialObject getMaterial(String identifier, String world, String player) {
    if(TNE.configurations.getMaterialsConfiguration().containsMaterial(identifier, world, player)) {
      return TNE.configurations.getMaterialsConfiguration().getMaterial(identifier, world, player);
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