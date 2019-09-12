package net.tnemc.core.event.currency;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

import java.math.BigDecimal;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * Created by creatorfromhell on 9/12/2019.
 * <p>
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */
public class TNECurrencyNoteClaimedEvent extends TNECurrencyEvent implements Cancellable {

  private Player player;
  private BigDecimal amount;

  private boolean cancelled = false;

  public TNECurrencyNoteClaimedEvent(String world, String currency, Player player, BigDecimal amount, boolean async) {
    super(world, currency, async);
    this.player = player;
    this.amount = amount;
  }

  public Player getPlayer() {
    return player;
  }

  public void setPlayer(Player player) {
    this.player = player;
  }

  public BigDecimal getAmount() {
    return amount;
  }

  public void setAmount(BigDecimal amount) {
    this.amount = amount;
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