package net.tnemc.core.event.currency;

/**
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by Daniel on 7/7/2017.
 */
public abstract class TNECurrencyTierEvent extends TNECurrencyEvent {

  protected String tier;
  protected String tierType;

  public TNECurrencyTierEvent(String world, String currency, String tier, String tierType) {
    super(world, currency, false);
    this.tier = tier;
    this.tierType = tierType;
  }

  public TNECurrencyTierEvent(String world, String currency, String tier, String tierType, boolean async) {
    super(world, currency, async);
    this.tier = tier;
    this.tierType = tierType;
  }

  public String getTier() {
    return tier;
  }

  public void setTier(String tier) {
    this.tier = tier;
  }

  public String getTierType() {
    return tierType;
  }

  public void setTierType(String tierType) {
    this.tierType = tierType;
  }
}