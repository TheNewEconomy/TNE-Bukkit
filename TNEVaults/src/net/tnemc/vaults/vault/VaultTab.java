package net.tnemc.vaults.vault;

import com.github.tnerevival.user.IDFinder;
import net.tnemc.core.item.SerialItem;
import net.tnemc.vaults.VaultsModule;
import net.tnemc.vaults.inventory.VaultInventoryHolder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

/**
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by Daniel on 11/15/2017.
 */
public class VaultTab {
  private Map<Integer, SerialItem> items = new HashMap<>();

  private UUID owner;
  private String world;
  private SerialItem icon;
  private int location;

  public VaultTab(UUID owner, String world, SerialItem icon, int location) {
    this.owner = owner;
    this.world = world;
    this.location = location;
    this.icon = icon;
  }

  public Inventory buildInventory() {
    Vault vault = VaultsModule.instance().manager().getVault(owner, world);
    Inventory inventory = Bukkit.createInventory(new VaultInventoryHolder(location, owner, world), vault.getSize() + 9, "Vault " + IDFinder.getUsername(owner.toString()));
    vault.getTabs().forEach((order, tab)->{
      inventory.setItem(order - 1, buildTabIcon(tab.getIcon().getStack(), order, location));
    });
    items.forEach((slot, item)->inventory.setItem(slot, item.getStack()));
    return inventory;
  }

  public static ItemStack buildTabIcon(ItemStack stack, int tab, int currentTab) {
    String title = (tab == currentTab)? "Tab " + tab + ChatColor.GREEN + " (Current)" : "Tab " + tab;

    ItemStack icon = stack;
    ItemMeta meta = Bukkit.getItemFactory().getItemMeta(icon.getType());
    meta.setDisplayName(title);
    List<String> lore = new ArrayList<>();
    lore.add(ChatColor.WHITE + "Left click to view tab.");
    lore.add(ChatColor.WHITE + "Right click with item to change icon.");
    meta.setLore(lore);
    icon.setItemMeta(meta);
    return icon;
  }

  public Map<Integer, SerialItem> getItems() {
    return items;
  }

  public void setItems(Map<Integer, SerialItem> items) {
    this.items = items;
  }

  public UUID getOwner() {
    return owner;
  }

  public void setOwner(UUID owner) {
    this.owner = owner;
  }

  public String getWorld() {
    return world;
  }

  public void setWorld(String world) {
    this.world = world;
  }

  public SerialItem getIcon() {
    return icon;
  }

  public void setIcon(SerialItem icon) {
    this.icon = icon;
  }

  public int getLocation() {
    return location;
  }

  public void setLocation(int location) {
    this.location = location;
  }
}