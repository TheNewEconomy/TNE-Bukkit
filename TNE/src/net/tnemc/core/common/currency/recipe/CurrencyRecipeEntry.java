package net.tnemc.core.common.currency.recipe;

import org.bukkit.inventory.ItemStack;

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
public class CurrencyRecipeEntry {

  private char character;
  //only used for shapeless recipes
  private int amount;

  private ItemStack ingredient;

  public CurrencyRecipeEntry(char character, int amount, ItemStack ingredient) {
    this.character = character;
    this.amount = amount;
    this.ingredient = ingredient;
  }

  public char getCharacter() {
    return character;
  }

  public void setCharacter(char character) {
    this.character = character;
  }

  public int getAmount() {
    return amount;
  }

  public void setAmount(int amount) {
    this.amount = amount;
  }

  public ItemStack getIngredient() {
    return ingredient;
  }

  public void setIngredient(ItemStack ingredient) {
    this.ingredient = ingredient;
  }
}