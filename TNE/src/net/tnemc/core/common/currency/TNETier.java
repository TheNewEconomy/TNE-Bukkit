package net.tnemc.core.common.currency;

import net.tnemc.core.economy.currency.Tier;

import java.math.BigInteger;

/**
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 10/22/2016.
 */
public class TNETier implements Tier {
  private boolean major;
  private String single;
  private String plural;
  private BigInteger weight;
  private ItemTier itemInfo;

  public static TNETier fromReserve(Tier tier) {
    TNETier tneTier = new TNETier();
    tneTier.setPlural(tier.plural());
    tneTier.setSingle(tier.singular());
    tneTier.setWeight(new BigInteger(tier.weight() + ""));
    tneTier.setMajor(tier.isMajor());
    return tneTier;
  }

  public ItemTier getItemInfo() {
    return itemInfo;
  }

  public void setItemInfo(ItemTier itemInfo) {
    this.itemInfo = itemInfo;
  }

  public void setSingle(String single) {
    this.single = single;
  }

  public void setPlural(String plural) {
    this.plural = plural;
  }

  public void setWeight(BigInteger weight) {
    this.weight = weight;
  }

  public BigInteger getTNEWeight() {
    return weight;
  }

  public void setMajor(boolean major) {
    this.major = major;
  }

  @Override
  public String singular() {
    return single;
  }

  @Override
  public String plural() {
    return plural;
  }

  @Override
  public boolean isMajor() {
    return major;
  }

  @Override
  public double weight() {
    return weight.doubleValue();
  }
}
