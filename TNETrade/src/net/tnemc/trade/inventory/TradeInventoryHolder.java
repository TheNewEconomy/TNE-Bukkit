package net.tnemc.trade.inventory;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import java.util.UUID;

/**
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by Daniel on 11/9/2017.
 */
public class TradeInventoryHolder implements InventoryHolder {

  private UUID initiator;
  private UUID acceptor;

  public TradeInventoryHolder(UUID initiator, UUID acceptor) {
    this.initiator = initiator;
    this.acceptor = acceptor;
  }

  @Override
  public Inventory getInventory() {
    return null;
  }

  public UUID getInitiator() {
    return initiator;
  }

  public void setInitiator(UUID initiator) {
    this.initiator = initiator;
  }

  public UUID getAcceptor() {
    return acceptor;
  }

  public void setAcceptor(UUID acceptor) {
    this.acceptor = acceptor;
  }
}