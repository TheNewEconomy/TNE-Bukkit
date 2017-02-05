package com.github.tnerevival.core.signs;

import com.github.tnerevival.account.IDFinder;
import com.github.tnerevival.core.Message;
import com.github.tnerevival.core.currency.CurrencyFormatter;
import com.github.tnerevival.core.event.sign.SignEventAction;
import com.github.tnerevival.core.event.sign.TNESignEvent;
import com.github.tnerevival.core.transaction.TransactionType;
import com.github.tnerevival.serializable.SerializableLocation;
import com.github.tnerevival.utils.AccountUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.math.BigDecimal;
import java.util.UUID;

public abstract class TNESign {
  protected UUID owner;
  protected SignType type;
  protected SerializableLocation location;
  protected Inventory inventory = null;

  public TNESign(UUID owner) {
    this.owner = owner;
  }

  /**
   * Called when a player attempts to create a TNE sign.
   * @param player
   * @return Whether or not the action was performed successfully.
   */
  public boolean onCreate(Player player) {
    TNESignEvent event = new TNESignEvent(IDFinder.getID(player), this, SignEventAction.CREATED);
    if(!player.hasPermission(type.getPlacePermission())) {
      event.setCancelled(true);
    }

    BigDecimal place = type.place(IDFinder.getWorld(player), IDFinder.getID(player).toString());

    if(place != null && place.compareTo(BigDecimal.ZERO) > 0) {
      if (!AccountUtils.transaction(IDFinder.getID(player).toString(), null, place, TransactionType.MONEY_INQUIRY, IDFinder.getWorld(player))) {
        Message insufficient = new Message("Messages.Money.Insufficient");
        insufficient.addVariable("$amount", CurrencyFormatter.format(IDFinder.getWorld(player), place));
        event.setCancelled(true);
      }
    }

    Bukkit.getServer().getPluginManager().callEvent(event);
    return (!event.isCancelled());
  }

  /**
   * Called when a player attempts to destroy a TNE sign.
   * @param player
   * @return Whether or not the action was performed successfully.
   */
  public boolean onDestroy(Player player) {
    TNESignEvent event = new TNESignEvent(IDFinder.getID(player), this, SignEventAction.DESTROYED);
    Bukkit.getServer().getPluginManager().callEvent(event);
    return (!event.isCancelled());
  }

  /**
   * Called when this sign is clicked on
   * @param player
   * @return Whether or not the action was performed successfully
   */
  public boolean onClick(Player player) {
    TNESignEvent event = new TNESignEvent(IDFinder.getID(player), this, SignEventAction.LEFT_CLICKED);
    if(!player.hasPermission(type.getUsePermission())) {
      event.setCancelled(true);
    }
    Bukkit.getServer().getPluginManager().callEvent(event);
    return (!event.isCancelled());
  }

  /**
   * Called when this sign is right clicked on
   * @param player
   * @return Whether or not the action was performed successfully
   */
  public boolean onRightClick(Player player) {
    TNESignEvent event = new TNESignEvent(IDFinder.getID(player), this, SignEventAction.RIGHT_CLICKED);
    if(!player.hasPermission(type.getUsePermission())) {
      event.setCancelled(true);
    }

    BigDecimal use = type.use(IDFinder.getWorld(player), IDFinder.getID(player).toString());

    if(!type.enabled(IDFinder.getWorld(player), IDFinder.getID(player).toString())) {
      new Message("Messages.Objects.SignDisabled").translate(IDFinder.getWorld(player), player);
      event.setCancelled(true);
    }

    if(!AccountUtils.transaction(IDFinder.getID(player).toString(), null, use, TransactionType.MONEY_INQUIRY, IDFinder.getWorld(player))) {
      Message insufficient = new Message("Messages.Money.Insufficient");
      insufficient.addVariable("$amount", CurrencyFormatter.format(IDFinder.getWorld(player), use));
      insufficient.translate(IDFinder.getWorld(player), player);
      event.setCancelled(true);
    }

    Bukkit.getServer().getPluginManager().callEvent(event);
    return (!event.isCancelled());
  }

  /**
   * Called when the inventory(if any) attached to this sign is opened.
   * @param player
   * @return Whether or not the action was performed successfully
   */
  public boolean onOpen(Player player) {
    TNESignEvent event = new TNESignEvent(IDFinder.getID(player), this, SignEventAction.INVENTORY_OPENED);
    Bukkit.getServer().getPluginManager().callEvent(event);
    return (!event.isCancelled());
  }

  /**
   * Called when the inventory(if any) attached to this sign is closed.
   * @param player
   * @return Whether or not the action was performed successfully
   */
  public boolean onClose(Player player) {
    TNESignEvent event = new TNESignEvent(IDFinder.getID(player), this, SignEventAction.INVENTORY_CLOSED);
    Bukkit.getServer().getPluginManager().callEvent(event);
    return (!event.isCancelled());
  }
 
  public UUID getOwner() {
    return owner;
  }

  public void setOwner(UUID owner) {
    this.owner = owner;
  }

  public SignType getType() {
    return type;
  }

  public void setType(SignType type) {
    this.type = type;
  }

  public SerializableLocation getLocation() {
    return location;
  }

  public void setLocation(SerializableLocation location) {
    this.location = location;
  }

  public Inventory getInventory() {
    return inventory;
  }

  public void setInventory(Inventory inventory) {
    this.inventory = inventory;
  }

  public void loadMeta(String data) {
    //TODO: Implement as needed in child classes.
  }

  public String getMeta() {
    //TODO: Implement as needed in child classes.
    return "";
  }
}