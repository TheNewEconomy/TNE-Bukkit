package net.tnemc.web.rest.object;

import java.math.BigDecimal;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * Created by creatorfromhell on 6/16/2019.
 * <p>
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */
public class HoldingsObject {

  private String world;
  private String currency;
  private BigDecimal holdings;

  public HoldingsObject(String world, String currency, BigDecimal holdings) {
    this.world = world;
    this.currency = currency;
    this.holdings = holdings;
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

  public BigDecimal getHoldings() {
    return holdings;
  }

  public void setHoldings(BigDecimal holdings) {
    this.holdings = holdings;
  }
}