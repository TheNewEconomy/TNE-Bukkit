package net.tnemc.core.event.currency;

import net.tnemc.core.common.currency.recipe.CurrencyRecipe;

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

/**
 * Called when the currency crafting recipe is parsed from the configuration file.
 */
public class TNECurrencyCraftingRecipeEvent extends TNECurrencyTierEvent {

  private CurrencyRecipe recipe;

  public TNECurrencyCraftingRecipeEvent(String world, String currency, String tier, String tierType, CurrencyRecipe recipe, boolean async) {
    super(world, currency, tier, tierType, async);
    this.recipe = recipe;
  }

  public CurrencyRecipe getRecipe() {
    return recipe;
  }

  public void setRecipe(CurrencyRecipe recipe) {
    this.recipe = recipe;
  }
}