package net.tnemc.rewards.event;

import net.tnemc.rewards.MaterialObject;
import net.tnemc.rewards.RewardsModule;

import java.math.BigDecimal;

/**
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by Daniel on 10/21/2017.
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

  public BigDecimal getCost(String identifier, String world, String player) {
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
        return BigDecimal.ZERO;
    }
  }

  private Boolean containsMaterial(String identifier, String world, String player) {
    return RewardsModule.instance().configuration().containsMaterial(identifier, world, player);
  }

  private MaterialObject getMaterial(String identifier, String world, String player) {
    if(RewardsModule.instance().configuration().containsMaterial(identifier, world, player)) {
      return RewardsModule.instance().configuration().getMaterial(identifier, world, player);
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
