package net.tnemc.core.event.currency;

import net.tnemc.core.event.TNEEvent;

/**
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by Daniel on 7/7/2017.
 */
public abstract class TNECurrencyEvent extends TNEEvent {

  protected String world;
  protected String currency;

  public TNECurrencyEvent(String world, String currency) {
    super(false);
    this.world = world;
    this.currency = currency;
  }

  public TNECurrencyEvent(String world, String currency, boolean async) {
    super(async);
    this.world = world;
    this.currency = currency;
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
}