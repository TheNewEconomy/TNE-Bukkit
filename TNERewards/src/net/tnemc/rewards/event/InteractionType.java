package net.tnemc.rewards.event;

import net.tnemc.rewards.MaterialObject;
import net.tnemc.rewards.RewardsModule;

import java.math.BigDecimal;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
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
