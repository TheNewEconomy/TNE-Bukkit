package com.github.tnerevival.core.inventory;

import com.github.tnerevival.TNE;
import com.github.tnerevival.account.Account;
import com.github.tnerevival.account.IDFinder;
import com.github.tnerevival.core.Message;
import com.github.tnerevival.core.configurations.impl.ObjectConfiguration;
import com.github.tnerevival.core.currency.CurrencyFormatter;
import com.github.tnerevival.core.event.object.InteractionType;
import com.github.tnerevival.core.event.object.TNEObjectInteractionEvent;
import com.github.tnerevival.core.inventory.impl.AuctionItemInventory;
import com.github.tnerevival.core.inventory.impl.ShopInventory;
import com.github.tnerevival.core.inventory.impl.ShopItemInventory;
import com.github.tnerevival.core.inventory.impl.VaultInventory;
import com.github.tnerevival.core.transaction.TransactionType;
import com.github.tnerevival.serializable.SerializableItemStack;
import com.github.tnerevival.utils.AccountUtils;
import com.github.tnerevival.utils.MISCUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;

/**
 * Created by Daniel on 9/2/2016.
 */
public abstract class TNEInventory {
  /**
   * A List of all players currently viewing this inventory
   */
  protected List<InventoryViewer> viewers = new ArrayList<>();

  protected UUID owner;
  protected String world;
  protected Boolean update = false;

  protected Inventory inventory;

  /**
   * An array of blacklisted materials, that cannot be placed into this inventory.
   * @return
   */
  public abstract List<Material> getBlacklisted();

  /**
   * An array of valid slots for inventories that don't allow players to use majority slots.
   * @return
   */
  public abstract List<Integer> getValidSlots();

  /**
   * An array of slots that a player cannot interact with due to some restriction and/or feature of the inventory.
   * @return
   */
  public abstract List<Integer> getInvalidSlots();

  public void setUpdate(Boolean update) {
    this.update = update;
  }

  public Boolean update() {
    return update;
  }

  /**
   * Returns the Bukkit-based inventory class.
   * @return
   */
  public Inventory getInventory() {
    return inventory;
  }

  /**
   * Used to override the inventory class.
   * @param inventory
   */
  public void setInventory(Inventory inventory) {
    this.inventory = inventory;
  }

  public int getValidSlot() {
    return getValidSlot(0);
  }

  public void setOwner(UUID owner) {
    this.owner = owner;
  }

  public void setWorld(String world) {
    this.world = world;
  }

  public List<InventoryViewer> getViewers() {
    return viewers;
  }

  public void addViewer(InventoryViewer viewer) {
    viewers.add(viewer);
  }

  public void removeViewer(UUID id) {
    Iterator<InventoryViewer> i = viewers.iterator();
    while(i.hasNext()) {
      InventoryViewer viewer = i.next();
      if(viewer.getUUID().equals(id)) {
        i.remove();
      }
    }
  }

  public int getValidSlot(int recommended) {
    if(getValidSlots().size() > 0 && getValidSlots().contains(recommended)) return recommended;
    if(getInvalidSlots().size() > 0 && !getInvalidSlots().contains(recommended)) return recommended;

    return inventory.firstEmpty();
  }

  private String charge(InventoryViewer viewer) {
    Account acc = AccountUtils.getAccount(viewer.getUUID());
    ObjectConfiguration config = TNE.configurations.getObjectConfiguration();

    if(!acc.getStatus().getBalance())
      return "Messages.Account.Locked";

    if(TNE.instance.manager.enabled(viewer.getUUID(), IDFinder.getWorld(viewer.getUUID()))) {
      if (!TNE.instance.manager.confirmed(viewer.getUUID(), IDFinder.getWorld(viewer.getUUID()))) {
        if (acc.getPin().equalsIgnoreCase("TNENOSTRINGVALUE"))
          return "Messages.Account.Set";
        else if (!acc.getPin().equalsIgnoreCase("TNENOSTRINGVALUE"))
          return "Messages.Account.Confirm";
      }
    }

    Player player = MISCUtils.getPlayer(viewer.getUUID());
    MISCUtils.debug(config.inventoryEnabled(inventory.getType(), IDFinder.getWorld(player), IDFinder.getID(player).toString()) + "");
    if(config.inventoryEnabled(inventory.getType(), IDFinder.getWorld(player), IDFinder.getID(player).toString())) {
      if(config.isTimed(inventory.getType(), IDFinder.getWorld(player), IDFinder.getID(player).toString())) {
        if(acc.getTimeLeft(IDFinder.getWorld(viewer.getUUID()), TNE.configurations.getObjectConfiguration().inventoryType(inventory.getType())) <= 0) {
          return "Messages.Package.Unable";
        }
      } else {
        if(!AccountUtils.transaction(viewer.getUUID().toString(), null, config.getInventoryCost(inventory.getType(), IDFinder.getWorld(player), IDFinder.getID(player).toString()), TransactionType.MONEY_INQUIRY, IDFinder.getWorld(viewer.getUUID()))) {
          return "Messages.Money.Insufficient";
        } else {
          AccountUtils.transaction(viewer.getUUID().toString(), null, config.getInventoryCost(inventory.getType(), IDFinder.getWorld(player), IDFinder.getID(player).toString()), TransactionType.MONEY_REMOVE, IDFinder.getWorld(viewer.getUUID()));
          return "Messages.Inventory.Charge";
        }
      }
    }
    return "successful";
  }

  public boolean onClick(InventoryViewer viewer, ClickType type, int slot, ItemStack item) {

    MISCUtils.debug("Inventory Click Event");
    if(item != null && getBlacklisted().contains(item.getType())) return false;
    if(getValidSlots().size() > 0 && !getValidSlots().contains(slot)) return false;
    if(getInvalidSlots().size() > 0 && getInvalidSlots().contains(slot)) return false;

    if(inventory.getType().equals(InventoryType.ENCHANTING) || inventory.getType().equals(InventoryType.FURNACE)) {
      MISCUtils.debug("Inventory is enchanting OR smelting");
      InteractionType intType = (inventory.getType().equals(InventoryType.ENCHANTING))? InteractionType.ENCHANT : InteractionType.SMELTING;
      TNEObjectInteractionEvent e = new TNEObjectInteractionEvent(MISCUtils.getPlayer(viewer.getUUID()), item, item.getType().name(), intType);
      Bukkit.getServer().getPluginManager().callEvent(e);

      MISCUtils.debug("Event called");
      MISCUtils.handleObjectEvent(e);

      MISCUtils.debug("Event handled internally");
      if(e.isCancelled()) {
        MISCUtils.debug("Event Cancelled");
        return false;
      }
      MISCUtils.debug("Exiting click event");
    }
    return true;
  }

  public boolean onOpen(InventoryViewer viewer) {
    ObjectConfiguration config = TNE.configurations.getObjectConfiguration();
    Player player = MISCUtils.getPlayer(viewer.getUUID());

    if(!(this instanceof VaultInventory) && !(this instanceof ShopInventory) && !(this instanceof ShopItemInventory)
       && !(this instanceof AuctionItemInventory)) {
      String charge = charge(viewer);
      MISCUtils.debug(charge);
      if(!charge.equalsIgnoreCase("successful") && !charge.equalsIgnoreCase("Messages.Inventory.Charge")) {
        Message m = new Message(charge);
        m.addVariable("$amount", CurrencyFormatter.format(IDFinder.getWorld(viewer.getUUID()), AccountUtils.round(config.getInventoryCost(inventory.getType(), IDFinder.getWorld(player), IDFinder.getID(player).toString()))));
        m.addVariable("$type", config.inventoryType(inventory.getType()));
        m.translate(IDFinder.getWorld(player), player);

        return false;
      }

      if(charge.equalsIgnoreCase("Messages.Inventory.Charge")) {
        Message m = new Message(charge);
        m.addVariable("$amount", CurrencyFormatter.format(IDFinder.getWorld(viewer.getUUID()), AccountUtils.round(config.getInventoryCost(inventory.getType(), IDFinder.getWorld(player), IDFinder.getID(player).toString()))));
        m.addVariable("$type", config.inventoryType(inventory.getType()));
        m.translate(IDFinder.getWorld(player), player);
      }
    } else if(this instanceof VaultInventory) {
      if(!owner.equals(viewer.getUUID()) && !AccountUtils.getAccount(owner).getVault(viewer.getWorld()).getMembers().contains(viewer.getUUID())) {
        return false;
      }
    }
    return true;
  }

  /**
   * Called when a viewer closes the inventory.
   * @param viewer
   */
  public void onClose(InventoryViewer viewer) {
    if(viewer != null) {
      Iterator<Map.Entry<SerializableItemStack, InventoryOperation>> it = viewer.getOperations().entrySet().iterator();
      while (it.hasNext()) {
        Map.Entry<SerializableItemStack, InventoryOperation> entry = it.next();

        switch (entry.getValue()) {
          case ADD:
            inventory.addItem(entry.getKey().toItemStack());
            break;
          case REMOVE:
            inventory.removeItem(entry.getKey().toItemStack());
            break;
        }
      }
    }
    Iterator<InventoryViewer> itr = viewers.iterator();

    while(itr.hasNext()) {
      InventoryViewer entry = itr.next();

      if(entry.getUUID().equals(viewer.getUUID())) {
        itr.remove();
        continue;
      }
      MISCUtils.getPlayer(entry.getUUID()).openInventory(inventory);
    }
  }
}