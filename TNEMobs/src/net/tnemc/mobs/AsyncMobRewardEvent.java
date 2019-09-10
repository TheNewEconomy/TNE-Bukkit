package net.tnemc.mobs;

import net.tnemc.core.event.TNEEvent;

import java.math.BigDecimal;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * Created by creatorfromhell on 9/10/2019.
 * <p>
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */
public class AsyncMobRewardEvent extends TNEEvent {

  protected String mobType;
  protected String mobName;
  protected String world;
  protected String currency;
  protected String currencyType;
  protected BigDecimal reward;

  public AsyncMobRewardEvent(String mobType, String mobName, String world, String currency, String currencyType, BigDecimal reward) {
    super(true);
    this.mobType = mobType;
    this.mobName = mobName;
    this.world = world;
    this.currency = currency;
    this.currencyType = currencyType;
    this.reward = reward;
  }

  /**
   * @return This is the mob type of the mob as outlined in {@EntityType entity type}.
   */
  public String getMobType() {
    return mobType;
  }

  public void setMobType(String mobType) {
    this.mobType = mobType;
  }

  /**
   * @return The name attached to the mob if applicable, could be null.
   */
  public String getMobName() {
    return mobName;
  }

  public void setMobName(String mobName) {
    this.mobName = mobName;
  }

  public String getWorld() {
    return world;
  }

  public void setWorld(String world) {
    this.world = world;
  }

  public String getCurrency() {
    return currency;
  }

  public void setCurrency(String currency) {
    this.currency = currency;
  }

  /**
   * @return The type of currency item, virtual or experience.
   */
  public String getCurrencyType() {
    return currencyType;
  }

  public void setCurrencyType(String currencyType) {
    this.currencyType = currencyType;
  }

  public BigDecimal getReward() {
    return reward;
  }

  public void setReward(BigDecimal reward) {
    this.reward = reward;
  }
}