package net.tnemc.core.common.currency.calculations;

import net.tnemc.core.common.currency.TNETier;
import net.tnemc.core.common.utils.MaterialUtils;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * Created by creatorfromhell on 2/2/2021.
 * <p>
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */
public interface CalculationProvider {

  /**
   * Used to calculate the holdings of the inventory materials present.
   * @return The {@link BigDecimal} representation of the inventory materials balance value.
   */
  default BigDecimal calculateHoldings(PlayerCurrencyData data) {
    BigDecimal holdings = BigDecimal.ZERO;

    for(Map.Entry<BigDecimal, TNETier> entry : data.getMaterialValues().entrySet()) {
      final int amount = data.getInventoryMaterials().getOrDefault(entry.getValue().getItemInfo().getMaterial(), 0);

      if(amount > 0) {
        holdings = holdings.add(entry.getKey().multiply(new BigDecimal("" + amount)));
      }
    }
    return holdings;
  }

  default void clearItems(PlayerCurrencyData data) {
    for(Map.Entry<String, Integer> entry : data.getInventoryMaterials().entrySet()) {
      final Optional<TNETier> tier = data.getCurrency().getTierByMaterial(entry.getKey());

      tier.ifPresent(tneTier -> removeMaterials(data, tneTier, entry.getValue()));
    }
  }

  default void setItems(PlayerCurrencyData data, BigDecimal amount) {
    final BigDecimal holdings = calculateHoldings(data);

    if(holdings.compareTo(amount) == 0) return;

    if(holdings.compareTo(amount) > 0) {
      calculateChange(data, holdings.subtract(amount));
    }
    provideMaterials(data, amount.subtract(holdings), Optional.empty());
  }

  /**
   * Used to calculate the materials that need to be removed when a player in an item-based economy
   * has money taken from their account.
   * @param change The amount to remove from the player's account.
   * @return The {@link BigDecimal} representation of the leftover amount that couldn't be removed
   * because there's no more materials left to remove.
   */
  default BigDecimal calculateChange(PlayerCurrencyData data, BigDecimal change) {
    BigDecimal workingAmount = change;

    final NavigableMap<BigDecimal, TNETier> values = data.getMaterialValues().descendingMap();
    Map.Entry<BigDecimal, TNETier> lowestWhole = findLowestGreaterThan(data, BigDecimal.ONE);

    int instance = 0;
    do {

      if(instance > 0) {
        if(!data.getInventoryMaterials().containsKey(lowestWhole.getValue().getItemInfo().getMaterial())) {
          lowestWhole = findLowestGreaterThan(data, BigDecimal.ONE);
        }
        provideMaterials(data, workingAmount, Optional.of(new BigDecimal(1)));
        provideMaterials(data, lowestWhole.getKey().subtract(workingAmount), Optional.of(new BigDecimal(1)));
        removeMaterials(data, lowestWhole.getValue(), 1);
      }

      for(Map.Entry<BigDecimal, TNETier> entry : values.entrySet()) {

        if(entry.getKey().compareTo(workingAmount) > 0) {
          continue;
        }

        if(workingAmount.compareTo(BigDecimal.ZERO) == 0) {
          continue;
        }

        if(!data.getInventoryMaterials().containsKey(entry.getValue().getItemInfo().getMaterial())) {
          continue;
        }

        final int max = data.getInventoryMaterials().get(entry.getValue().getItemInfo().getMaterial());

        int amountPossible = workingAmount.divide(entry.getKey(), RoundingMode.DOWN).toBigInteger().intValue();

        if(amountPossible > max) {
          amountPossible = max;
        }

        workingAmount = workingAmount.subtract(entry.getKey().multiply(new BigDecimal(amountPossible)));
        removeMaterials(data, entry.getValue(), amountPossible);
      }
      instance++;
    } while(workingAmount.compareTo(BigDecimal.ZERO) != 0 && data.getInventoryMaterials().size() > 0);
    return workingAmount;
  }

  /**
   * Used to exchange an amount to inventory items. This is mostly used for when a larger denomination
   * needs to be broken into smaller denominations for calculation purposes.
   * @param amount The amount that the items should add up to.
   * @param threshold The threshold that all items should be valued under when given back. I.E. if
   * the threshold is 1 then all denominations given to the inventory should be less than 1. If you don't wish
   * to use a threshold then you should pass Optional.empty().
   */
  default void provideMaterials(PlayerCurrencyData data, BigDecimal amount, Optional<BigDecimal> threshold) {
    BigDecimal workingAmount = amount;
    final NavigableMap<BigDecimal, TNETier> values = data.getMaterialValues().descendingMap();

    for(Map.Entry<BigDecimal, TNETier> entry : values.entrySet()) {

      if(threshold.isPresent() && entry.getKey().compareTo(threshold.get()) >= 0) {
        continue;
      }

      if(entry.getKey().compareTo(workingAmount) > 0) {
        continue;
      }

      if(workingAmount.compareTo(BigDecimal.ZERO) == 0) {
        continue;
      }


      final int amountPossible = workingAmount.divide(entry.getKey(), RoundingMode.DOWN).toBigInteger().intValue();
      workingAmount = workingAmount.subtract(entry.getKey().multiply(new BigDecimal(amountPossible)));
      int holding = data.getInventoryMaterials().getOrDefault(entry.getValue().getItemInfo().getMaterial(), 0);
      data.getInventoryMaterials().put(entry.getValue().getItemInfo().getMaterial(), holding + amountPossible);
      data.provideMaterials(entry.getValue(), amountPossible);
    }
  }

  /**
   * Used to remove tiers from the {@link PlayerCurrencyData} being worked with.
   * @param tier The tier name in String form.
   * @param amount The amount of the material to remove from working materials.
   */
  default void removeMaterials(PlayerCurrencyData data, TNETier tier, Integer amount) {
    data.removeMaterials(tier, amount);
  }

  /**
   * Finds the lowest denomination that is greater than the specified value.
   * @param value The value to utilize for the comparison.
   * @return The Map Entry containing the lowest denomination information.
   */
  default Map.Entry<BigDecimal, TNETier> findLowestGreaterThan(PlayerCurrencyData data, BigDecimal value) {
    Map.Entry<BigDecimal, TNETier> entryLowest = null;
    for(Map.Entry<BigDecimal, TNETier> entry : data.getMaterialValues().entrySet()
        .stream().filter(entry->entry.getKey().compareTo(value) >= 0)
        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)).entrySet()) {

      if(entryLowest == null || entryLowest.getKey().compareTo(entry.getKey()) > 0) {
        if(data.getInventoryMaterials().containsKey(entry.getValue().getItemInfo().getMaterial())) {
          entryLowest = entry;
        }
      }
    }
    return entryLowest;
  }

  default Integer getCount(ItemStack stack, Inventory inventory) {
    ItemStack compare = stack.clone();
    compare.setAmount(1);

    int value = 0;
    for(ItemStack item : inventory.getContents()) {
      //TNE.debug("item: " + compare.getItemMeta().getDisplayName());
      if(MaterialUtils.itemsEqual(compare, item)) {
        value += item.getAmount();
      }
    }
    //TNE.debug("Count: " + value);
    return value;
  }
}