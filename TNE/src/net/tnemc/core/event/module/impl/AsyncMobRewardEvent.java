package net.tnemc.core.event.module.impl;

import net.tnemc.core.event.TNEEvent;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

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
public class AsyncMobRewardEvent extends TNEEvent implements Cancellable {

  protected final LivingEntity entity;
  protected final Player killer;
  protected String world;
  protected String currency;
  protected String currencyType;
  protected BigDecimal reward;

  private boolean cancelled = false;

  public AsyncMobRewardEvent(final LivingEntity entity, final Player killer, String world, String currency, String currencyType, BigDecimal reward) {
    super(true);
    this.entity = entity;
    this.killer = killer;
    this.world = world;
    this.currency = currency;
    this.currencyType = currencyType;
    this.reward = reward;
  }

  public LivingEntity getEntity() {
    return entity;
  }

  public Player getKiller() {
    return killer;
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

  @Override
  public boolean isCancelled() {
    return cancelled;
  }

  @Override
  public void setCancelled(boolean cancelled) {
    this.cancelled = cancelled;
  }
}