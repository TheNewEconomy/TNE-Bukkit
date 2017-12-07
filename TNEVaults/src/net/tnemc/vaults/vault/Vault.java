package net.tnemc.vaults.vault;

import net.tnemc.core.TNE;
import net.tnemc.core.item.SerialItem;
import net.tnemc.core.item.data.ShulkerData;
import net.tnemc.vaults.inventory.ShulkerPeekInventoryHolder;
import net.tnemc.vaults.vault.member.VaultMember;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * Created by Daniel on 11/9/2017.
 */
public class Vault {
  private Map<UUID, VaultMember> members = new HashMap<>();
  private Map<Integer, VaultTab> tabs = new HashMap<>();

  private UUID owner;
  private String world;
  private long created;
  private int size;
  private int maxTabs;

  public Vault(UUID owner, String world) {
    this(owner, world, 27, 3);
  }

  public Vault(UUID owner, String world, Integer size, Integer maxTabs) {
    this.owner = owner;
    this.world = world;
    this.size = size;
    this.maxTabs = maxTabs;

    for(int i = 1; i <= maxTabs; i++) {
      tabs.put(i, new VaultTab(owner, world, new SerialItem(new ItemStack(Material.STAINED_GLASS_PANE)), i));
    }

    this.created = new Date().getTime();
  }

  public SerialItem getIcon(int tab) {
    return tabs.get(tab).getIcon();
  }

  public SerialItem getItem(int tab, int slot) {
    return tabs.get(tab).getItems().get(slot);
  }

  public Map<Integer, VaultTab> getTabs() {
    return tabs;
  }

  public void setIcon(int tab, ItemStack stack) {
    tabs.get(tab).setIcon(new SerialItem(stack));
  }

  public void setTabs(Map<Integer, VaultTab> tabs) {
    this.tabs = tabs;
  }

  public int getMaxTabs() {
    return maxTabs;
  }

  public void setMaxTabs(int maxTabs) {
    this.maxTabs = maxTabs;
  }

  public void setItem(int tab, int slot, SerialItem item) {
    this.tabs.get(tab).getItems().put(slot, item);
  }

  public Map<Integer, SerialItem> getItems(int tab) {
    return tabs.get(tab).getItems();
  }

  public void setItems(int tab, Map<Integer, SerialItem> items) {
    tabs.get(tab).setItems(items);
  }

  public void setItems(int tab, Inventory inventory) {
    for(int i = 0; i < size; i++) {
      if(inventory.getItem(i) == null || inventory.getItem(i).getType().equals(Material.AIR)) {
        if(tabs.get(tab).getItems().containsKey(i)) tabs.get(tab).getItems().remove(i);
      } else {
        TNE.debug("Adding new SerialItem");
        tabs.get(tab).getItems().put(i, new SerialItem(inventory.getItem(i)));
      }
    }
  }

  public Map<UUID, VaultMember> getMembers() {
    return members;
  }

  public void setMembers(Map<UUID, VaultMember> members) {
    this.members = members;
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

  public long getCreated() {
    return created;
  }

  public void setCreated(long created) {
    this.created = created;
  }

  public int getSize() {
    return size;
  }

  public void setSize(int size) {
    this.size = size;
  }



  public Inventory buildShulkerInventory(UUID vault, String world, int tab, int shulkerSlot, UUID viewer, boolean readOnly) {
    ShulkerPeekInventoryHolder holder = new ShulkerPeekInventoryHolder(vault, world, shulkerSlot, tab, readOnly);
    holder.addViewer(viewer);
    ShulkerData data = (ShulkerData)tabs.get(tab).getItems().get(shulkerSlot).getData();
    Inventory inventory = Bukkit.createInventory(holder, 36, "Shulker Box View");
    TNE.debug("Building Shulker Box... Size: " + data.getItems().size());

    ItemStack stack = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte)14);
    ItemMeta meta = Bukkit.getItemFactory().getItemMeta(Material.STAINED_GLASS_PANE);
    meta.setDisplayName("Go Back");
    stack.setItemMeta(meta);
    inventory.setItem(0, stack);

    data.getItems().forEach((slot, item)->inventory.setItem((slot + 9), item.getStack()));
    return inventory;
  }
}