package net.tnemc.core.event.transaction;

import net.tnemc.core.common.transaction.TNETransaction;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by Daniel on 7/7/2017.
 */
public class TNEPreTransaction extends Event implements Cancellable {
  private static final HandlerList handlers = new HandlerList();

  private boolean cancelled = false;
  private TNETransaction transaction;

  public TNEPreTransaction(TNETransaction transaction, boolean async) {
    super(async);
    this.transaction = transaction;
  }

  public TNETransaction getTransaction() {
    return transaction;
  }

  public void setTransaction(TNETransaction transaction) {
    this.transaction = transaction;
  }

  @Override
  public boolean isCancelled() {
    return cancelled;
  }

  @Override
  public void setCancelled(boolean cancelled) {
    this.cancelled = cancelled;
  }

  public static HandlerList getHandlerList() {
    return handlers;
  }

  @Override
  public HandlerList getHandlers() {
    return handlers;
  }
}