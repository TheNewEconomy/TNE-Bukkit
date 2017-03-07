package com.github.tnerevival.core.signs;

import com.github.tnerevival.TNE;
import com.github.tnerevival.account.IDFinder;
import com.github.tnerevival.core.Message;
import com.github.tnerevival.core.currency.CurrencyFormatter;
import com.github.tnerevival.core.event.sign.SignEventAction;
import com.github.tnerevival.core.event.sign.TNESignEvent;
import com.github.tnerevival.core.transaction.TransactionType;
import com.github.tnerevival.serializable.SerializableLocation;
import com.github.tnerevival.utils.AccountUtils;
import com.github.tnerevival.utils.MISCUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Chest;
import org.bukkit.block.EnderChest;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.math.BigDecimal;
import java.util.*;

public abstract class TNESign {
  protected UUID owner;
  protected SignType type;
  protected SerializableLocation location;
  protected Inventory inventory = null;

  public TNESign(UUID owner, SerializableLocation location) {
    this.owner = owner;
    this.location = location;
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
    if(!owner.equals(IDFinder.getID(player)) && !player.hasPermission("tne.sign.admin")) return false;
    return (!event.isCancelled());
  }

  /**
   * Called when this sign is clicked on
   * @param player
   * @return Whether or not the action was performed successfully
   */
  public boolean onClick(Player player, boolean shift) {
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
  public boolean onRightClick(Player player, boolean shift) {
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

  /**
   * Utility Methods
   */


  public static Boolean validSign(Location location) {
    SerializableLocation cerealLoc = new SerializableLocation(location);
    for(SerializableLocation loc : TNE.instance().manager.signs.keySet()) {
      if(loc.equals(cerealLoc)) {
        return true;
      }
    }
    return false;
  }

  public static Location getAttachedSign(Location location) {
    List<Location> validLoc = validSignLocations(location);
    for(Location loc : validLoc) {
      if(loc.getWorld().getBlockAt(loc).getState() instanceof Sign && validSign(loc)) {
        return loc;
      }
    }
    return null;
  }

  public static void removeSign(SerializableLocation location) {
    Iterator<Map.Entry<SerializableLocation, TNESign>> i = TNE.instance().manager.signs.entrySet().iterator();

    while(i.hasNext()) {
      Map.Entry<SerializableLocation, TNESign> e = i.next();

      if(e.getKey().equals(location)) {
        i.remove();
      }
    }
  }

  public static TNESign getSign(SerializableLocation location) {
    for(Map.Entry<SerializableLocation, TNESign> entry : TNE.instance().manager.signs.entrySet()) {
      if(entry.getKey().equals(location)) {
        return entry.getValue();
      }
    }
    return null;
  }

  public static int getOwned(UUID id, SignType type) {
    int owned = 0;

    for(TNESign sign : TNE.instance().manager.signs.values()) {
      if(sign.getOwner().equals(id)) {
        if(type.equals(SignType.UNKNOWN) || sign.getType().equals(type)) owned++;
      }
    }

    return owned;
  }

  public static TNESign getOwningSign(Location location) {
    MISCUtils.debug("TNESign:getOwningSign(" + location.toString() + ")");
    for(Location loc : validChestLocations(location)) {
      MISCUtils.debug("Possibly sign location: " + loc.toString() + " is valid? " + validSign(loc));
      if(validSign(loc)) return getSign(new SerializableLocation(loc));
    }
    return null;
  }

  public SignChest getAttachedChest() {
    for(Location loc : validChestLocations(location.getLocation())) {
      if(loc.getBlock().getState() instanceof Chest || loc.getBlock().getState() instanceof EnderChest) {
        return new SignChest(loc.getBlock());
      }
    }
    return null;
  }

  public static List<Location> validChestLocations(Location location) {
    List<Location> locations = new ArrayList<>();
    locations.add(location.clone().add(1, 0, 0));
    locations.add(location.clone().add(-1, 0, 0));
    locations.add(location.clone().add(0, 1, 0));
    locations.add(location.clone().add(0, -1, 0));
    locations.add(location.clone().add(0, 0, 1));
    locations.add(location.clone().add(0, 0, -1));

    return locations;
  }

  public static List<Location> validSignLocations(Location location) {
    List<Location> locations = new ArrayList<>();
    locations.add(location.clone().add(1, 0, 0));
    locations.add(location.clone().add(-1, 0, 0));
    locations.add(location.clone().add(0, 0, 1));
    locations.add(location.clone().add(0, 0, -1));

    return locations;
  }

  public static TNESign instance(String type, UUID owner, SerializableLocation location) {
    switch(type.toLowerCase()) {
      case "item":
        return new ItemSign(owner, location);
      case "shop":
        return new ShopSign(owner, location);
      case "vault":
        return new VaultSign(owner, location);
      default:
        MISCUtils.debug("defaulting...");
        return new VaultSign(owner, location);
    }
  }
}