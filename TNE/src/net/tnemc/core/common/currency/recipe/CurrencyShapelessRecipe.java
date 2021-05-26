package net.tnemc.core.common.currency.recipe;

import net.tnemc.core.TNE;
import net.tnemc.core.common.currency.TNECurrency;
import net.tnemc.core.common.material.MaterialHelper;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapelessRecipe;

import java.util.HashMap;
import java.util.Map;

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
public class CurrencyShapelessRecipe extends CurrencyRecipe {

  Recipe recipe = null;

  public CurrencyShapelessRecipe(String currency, String tier, ItemStack result) {
    super(currency, tier, result);
  }

  @Override
  public void parseMaterialsConfig() {
    Map<Character, Integer> materials = new HashMap<>();

    for(String str : getCraftingMatrix()) {
      for(Character character : str.toCharArray()) {
        final int count = materials.getOrDefault(character, 0) + 1;
        materials.put(character, count);
      }
    }

    for(String material : getMaterialsRaw()) {
      final String[] split = material.split(":");
      if (split.length >= 2) {
        final int count = materials.getOrDefault(split[0].charAt(0), 1);
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

          getEntryList().add(new CurrencyRecipeEntry(split[0].charAt(0), count, tierStack));
        } else {
          getEntryList().add(new CurrencyRecipeEntry(split[0].charAt(0), count, new ItemStack(MaterialHelper.getMaterial(split[1]))));
        }
      }
    }
  }

  @Override
  public Recipe recipe() {
    if(recipe == null) {
      ShapelessRecipe recipe = new ShapelessRecipe(key, result);

      for (CurrencyRecipeEntry entry : getEntryList()) {
        int count = entry.getAmount();
        while(count-- > 0) {
          recipe.addIngredient(entry.getIngredient().getType());
        }
      }
      this.recipe = recipe;
    }
    return this.recipe;
  }
}
