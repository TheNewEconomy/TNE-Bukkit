package net.tnemc.signs.signs;

import com.github.tnerevival.serializable.SerializableLocation;
import net.tnemc.core.TNE;
import net.tnemc.core.common.Message;
import net.tnemc.core.common.WorldVariant;
import net.tnemc.core.common.account.WorldFinder;
import net.tnemc.core.common.api.IDFinder;
import net.tnemc.core.common.currency.CurrencyFormatter;
import net.tnemc.core.common.utils.MISCUtils;
import net.tnemc.core.economy.transaction.type.TransactionType;
import net.tnemc.signs.SignsModule;
import net.tnemc.signs.event.sign.SignEventAction;
import net.tnemc.signs.event.sign.TNESignEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.block.DoubleChest;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public abstract class TNESign {
  protected UUID owner;
  protected SignType type;
  protected SerializableLocation location;
  protected Inventory inventory = null;

  public TNESign(UUID owner, SerializableLocation location) {
    this.owner = owner;
    this.location = location;
  }

  public String getLine(int line) {
    BlockState state = location.getLocation().getBlock().getState();
    if(state instanceof Sign) {
      return ((Sign)state).getLine(line);
    }
    return "";
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

    Integer max = type.max(WorldFinder.getWorld(player, WorldVariant.CONFIGURATION), IDFinder.getID(player).toString());
    if(max > -1 && getOwned(IDFinder.getID(player), type) >= max) {
      new Message("Messages.Sign.Max").translate(WorldFinder.getWorld(player, WorldVariant.CONFIGURATION), player);
      event.setCancelled(true);
    }

    BigDecimal place = type.place(WorldFinder.getWorld(player, WorldVariant.CONFIGURATION), IDFinder.getID(player).toString());

    if(!event.isCancelled() && place != null && place.compareTo(BigDecimal.ZERO) > 0) {
      if (!AccountUtils.transaction(IDFinder.getID(player).toString(), null, place, TransactionType.MONEY_INQUIRY, IDFinder.getWorld(player))) {
        Message insufficient = new Message("Messages.Money.Insufficient");
        insufficient.addVariable("$amount", CurrencyFormatter.format(WorldFinder.getWorld(player, WorldVariant.CONFIGURATION), place));
        insufficient.translate(WorldFinder.getWorld(player, WorldVariant.CONFIGURATION), player);
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
    return (owner.equals(IDFinder.getID(player)) || player.hasPermission("tne.sign.admin")) && (!event.isCancelled());
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

    BigDecimal use = type.use(WorldFinder.getWorld(player, WorldVariant.CONFIGURATION), IDFinder.getID(player).toString());

    if(!type.enabled(WorldFinder.getWorld(player, WorldVariant.CONFIGURATION), IDFinder.getID(player).toString())) {
      new Message("Messages.Objects.SignDisabled").translate(WorldFinder.getWorld(player, WorldVariant.CONFIGURATION), player);
      event.setCancelled(true);
    }

    if(!AccountUtils.transaction(IDFinder.getID(player).toString(), null, use, TransactionType.MONEY_INQUIRY, IDFinder.getWorld(player))) {
      Message insufficient = new Message("Messages.Money.Insufficient");
      insufficient.addVariable("$amount", CurrencyFormatter.format(WorldFinder.getWorld(player, WorldVariant.CONFIGURATION), use));
      insufficient.translate(WorldFinder.getWorld(player, WorldVariant.CONFIGURATION), player);
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
    for(SerializableLocation loc : SignsModule.manager().getSigns().keySet()) {
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

    SignsModule.manager().getSigns().entrySet().removeIf(e -> e.getKey().equals(location));
  }

  public static TNESign getSign(SerializableLocation location) {
    for(Map.Entry<SerializableLocation, TNESign> entry : SignsModule.manager().getSigns().entrySet()) {
      if(entry.getKey().equals(location)) {
        return entry.getValue();
      }
    }
    return null;
  }

  public static int getOwned(UUID id, SignType type) {
    int owned = 0;

    for(TNESign sign : SignsModule.manager().getSigns().values()) {
      if(sign.getOwner().equals(id)) {
        if(type.equals(SignType.UNKNOWN) || sign.getType().equals(type)) owned++;
      }
    }

    return owned;
  }

  public static TNESign getOwningSign(Location location) {
    TNE.debug("TNESign:getOwningSign(" + location.toString() + ")");
    for(Location loc : validChestLocations(location)) {
      TNE.debug("Possibly sign location: " + loc.toString() + " is valid? " + validSign(loc));
      if(validSign(loc)) return getSign(new SerializableLocation(loc));
    }
    return null;
  }

  public SignChest getAttachedChest() {
    for(Location loc : validChestLocations(location.getLocation())) {
      if(loc.getBlock().getState() instanceof Chest || loc.getBlock().getState() instanceof DoubleChest || MISCUtils.isOneTen() && loc.getBlock().getState() instanceof org.bukkit.block.EnderChest) {
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
      case "bank":
        return new BankSign(owner, location);
      case "balance":
        return new BalanceSign(owner, location);
      default:
        TNE.debug("defaulting...");
        return new VaultSign(owner, location);
    }
  }
}