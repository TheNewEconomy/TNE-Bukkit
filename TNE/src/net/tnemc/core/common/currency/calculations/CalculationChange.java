package net.tnemc.core.common.currency.calculations;

import net.tnemc.core.common.currency.TNETier;

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
public class CalculationChange {

  private TNETier tier;
  private boolean add;
  private int amount;

  public CalculationChange(TNETier tier, boolean add, int amount) {
    this.tier = tier;
    this.add = add;
    this.amount = amount;
  }

  public TNETier getTier() {
    return tier;
  }

  public void setTier(TNETier tier) {
    this.tier = tier;
  }

  public boolean isAdd() {
    return add;
  }

  public void setAdd(boolean add) {
    this.add = add;
  }

  public int getAmount() {
    return amount;
  }

  public void setAmount(int amount) {
    this.amount = amount;
  }
}