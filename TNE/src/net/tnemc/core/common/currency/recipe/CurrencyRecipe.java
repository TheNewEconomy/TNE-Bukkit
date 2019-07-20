package net.tnemc.core.common.currency.recipe;

import net.tnemc.core.TNE;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

import java.util.ArrayList;
import java.util.List;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * Created by creatorfromhell on 7/19/2019.
 * <p>
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */
public abstract class CurrencyRecipe {

  private List<CurrencyRecipeEntry> entryList = new ArrayList<>();

  private List<String> craftingMatrix = new ArrayList<>();
  private List<String> materialsRaw = new ArrayList<>();

  protected final NamespacedKey key;

  protected String currency;
  protected String tier;

  protected ItemStack result;

  public CurrencyRecipe(String currency, String tier, ItemStack result) {
    this.currency = currency;
    this.tier = tier;
    this.result = result;
    this.key = new NamespacedKey(TNE.instance(), "tne_" + currency + "_" + tier);
  }

  public void register() {
    parseMaterialsConfig();
    if(recipe() != null) {
      Bukkit.addRecipe(recipe());
    }
  }

  public abstract void parseMaterialsConfig();

  public abstract Recipe recipe();

  public List<CurrencyRecipeEntry> getEntryList() {
    return entryList;
  }

  public void setEntryList(List<CurrencyRecipeEntry> entryList) {
    this.entryList = entryList;
  }

  public List<String> getCraftingMatrix() {
    return craftingMatrix;
  }

  public void setCraftingMatrix(List<String> craftingMatrix) {
    this.craftingMatrix = craftingMatrix;
  }

  public List<String> getMaterialsRaw() {
    return materialsRaw;
  }

  public void setMaterialsRaw(List<String> materialsRaw) {
    this.materialsRaw = materialsRaw;
  }

  public String getCurrency() {
    return currency;
  }

  public void setCurrency(String currency) {
    this.currency = currency;
  }

  public String getTier() {
    return tier;
  }

  public void setTier(String tier) {
    this.tier = tier;
  }

  public void setResultAmount(int resultAmount) {
    result.setAmount(resultAmount);
  }

  public ItemStack getResult() {
    return result;
  }

  public void setResult(ItemStack result) {
    this.result = result;
  }

  public NamespacedKey getKey() {
    return key;
  }
}