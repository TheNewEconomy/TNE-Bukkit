package net.tnemc.core.common.currency.recipe;

import net.tnemc.core.TNE;
import net.tnemc.core.common.currency.TNECurrency;
import net.tnemc.core.common.material.MaterialHelper;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;

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
public class CurrencyShapedRecipe extends CurrencyRecipe {

  Recipe recipe = null;

  public CurrencyShapedRecipe(String currency, String tier, ItemStack result) {
    super(currency, tier, result);
  }

  @Override
  public void parseMaterialsConfig() {
    for(String material : getMaterialsRaw()) {
      final String[] split = material.split(":");
      if(split.length >= 2) {
        if (split[1].equalsIgnoreCase("currency")) {
          if(split.length < 5) {
            TNE.logger().warning("Skipping currency crafting material: " + material + " invalid currency ingredient.");
            continue;
          }
          final String world = split[2];
          final String currency = split[3];
          final String tier = split[4];

          if(!TNE.manager().currencyManager().contains(world, currency)) {
            TNE.logger().warning("Skipping currency crafting material: " + material + " invalid currency ingredient.");
            continue;
          }

          TNECurrency currencyObject = TNE.manager().currencyManager().get(world, currency);

          if(!currencyObject.hasTier(tier) || !currencyObject.isItem()) {
            TNE.logger().warning("Skipping currency crafting material: " + material + " invalid currency ingredient.");
            continue;
          }

          final ItemStack tierStack = (currencyObject.getMajorTier(tier).isPresent())? currencyObject.getMajorTier(tier).get().getItemInfo().toStack() :
              currencyObject.getMinorTier(tier).get().getItemInfo().toStack();

          getEntryList().add(new CurrencyRecipeEntry(split[0].charAt(0), 1, tierStack));
        } else {
          getEntryList().add(new CurrencyRecipeEntry(split[0].charAt(0), 1, new ItemStack(MaterialHelper.getMaterial(split[1]))));
        }
      }
    }
  }

  @Override
  public Recipe recipe() {
    if(recipe == null) {
      ShapedRecipe recipe = new ShapedRecipe(key, result);
      recipe.shape(getCraftingMatrix().toArray(new String[getCraftingMatrix().size()]));

      for (CurrencyRecipeEntry entry : getEntryList()) {
        recipe.setIngredient(entry.getCharacter(), new RecipeChoice.ExactChoice(entry.getIngredient()));
      }
      this.recipe = recipe;
    }
    return this.recipe;
  }
}
