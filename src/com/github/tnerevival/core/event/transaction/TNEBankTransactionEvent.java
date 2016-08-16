package com.github.tnerevival.core.event.transaction;

import com.github.tnerevival.account.Bank;
import com.github.tnerevival.serializable.SerializableItemStack;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.UUID;

/**
 * Called when a player adds/removes an item from a bank.
 */
public class TNEBankTransactionEvent extends Event implements Cancellable {

  private UUID player;
  private Bank bank;
  private SerializableItemStack item;
  private boolean removed;
  private boolean cancelled = false;

  public TNEBankTransactionEvent(UUID player, Bank bank, SerializableItemStack item) {
    this(player, bank, item, true);
  }

  public TNEBankTransactionEvent(UUID player, Bank bank, SerializableItemStack item, boolean removed) {
    this.player = player;
    this.bank = bank;
    this.item = item;
    this.removed = removed;
  }


  @Override
  public HandlerList getHandlers() {
    return null;
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
