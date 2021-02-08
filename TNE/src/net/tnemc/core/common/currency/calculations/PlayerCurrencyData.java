package net.tnemc.core.common.currency.calculations;

import net.tnemc.core.common.currency.TNECurrency;
import net.tnemc.core.common.currency.TNETier;
import org.bukkit.inventory.Inventory;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

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
public class PlayerCurrencyData {

  private Map<String, Integer> inventoryMaterials = new HashMap<>();

  private Map<String, CalculationChange> inventoryChanges = new HashMap<>();

  private TreeMap<BigDecimal, TNETier> materialValues = new TreeMap<>();

  private Inventory inventory;
  private TNECurrency currency;

  /**
   * A class used to hold information regarding amounts of each {@link TNETier} an inventory contains. This information
   * will be in turn used for a balance calculation.
   * @param inventory The inventory associated with the data to collect.
   * @param currency The currency to use for the data collection.
   */
  public PlayerCurrencyData(Inventory inventory, TNECurrency currency) {
    this.inventory = inventory;
    this.currency = currency;
    materialValues.putAll(currency.getTiers());
    initializeInventoryMaterials(inventory);
  }

  /**
   * Used to calculate the amount of each tier the {@link Inventory} contains.
   * @param inventory The inventory to use for the calculation.
   */
  public void initializeInventoryMaterials(Inventory inventory) {
    for(Map.Entry<BigDecimal, TNETier> entry : materialValues.entrySet()) {

      inventoryMaterials.put(entry.getValue().singular(),
          currency.calculation().getCount(entry.getValue().getItemInfo().toStack(), inventory));
    }
  }

  public void addInventoryMaterial(TNETier tier, Integer amount) {
    final int holding = inventoryMaterials.getOrDefault(tier.singular(), 0);
    inventoryMaterials.put(tier.singular(), holding + amount);
  }

  public void addTier(String tier, int amount) {
    int finalAmount = amount;
    if(inventoryChanges.containsKey(tier)) {
      finalAmount = finalAmount + inventoryChanges.get(tier).getAmount();
    }
    inventoryChanges.put(tier, new CalculationChange(currency.getTier(tier).get(), true, finalAmount));
  }

  public void removeTier(String tier, int amount) {
    int finalAmount = amount;
    if(inventoryChanges.containsKey(tier)) {
      finalAmount = finalAmount + inventoryChanges.get(tier).getAmount();
    }
    inventoryChanges.put(tier, new CalculationChange(currency.getTier(tier).get(), false, finalAmount));
  }

  public Map<String, Integer> getInventoryMaterials() {
    return inventoryMaterials;
  }

  public void setInventoryMaterials(Map<String, Integer> inventoryMaterials) {
    this.inventoryMaterials = inventoryMaterials;
  }

  public TreeMap<BigDecimal, TNETier> getMaterialValues() {
    return materialValues;
  }

  public void setMaterialValues(TreeMap<BigDecimal, TNETier> materialValues) {
    this.materialValues = materialValues;
  }

  public Map<String, CalculationChange> getInventoryChanges() {
    return inventoryChanges;
  }

  public void setInventoryChanges(Map<String, CalculationChange> inventoryChanges) {
    this.inventoryChanges = inventoryChanges;
  }

  public Inventory getInventory() {
    return inventory;
  }

  public void setInventory(Inventory inventory) {
    this.inventory = inventory;
  }

  public TNECurrency getCurrency() {
    return currency;
  }

  public void setCurrency(TNECurrency currency) {
    this.currency = currency;
  }
}