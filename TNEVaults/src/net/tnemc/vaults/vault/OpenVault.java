package net.tnemc.vaults.vault;

import com.github.tnerevival.user.IDFinder;
import net.tnemc.core.TNE;
import net.tnemc.core.item.SerialItem;
import net.tnemc.vaults.VaultsModule;
import net.tnemc.vaults.inventory.VaultInventoryHolder;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by Daniel on 11/17/2017.
 */
public class OpenVault {
  private Map<Integer, Inventory> openTabs = new HashMap<>();
  private List<UUID> totalViewers = new ArrayList<>();

  private UUID owner;
  private String world;

  private boolean forceClosing = false;

  public OpenVault(UUID owner, String world) {
    this.owner = owner;
    this.world = world;

    initialize();
  }

  public void initialize() {
    Vault vault = VaultsModule.instance().manager().getVault(owner, world);
    vault.getTabs().forEach((order, tab)->openTabs.put(order, tab.buildInventory()));
  }

  public void updateIcon(int tab, SerialItem icon) {
    TNE.debug("OpenVault.updateIcon: Tab: " + tab);
    openTabs.forEach((tabIndex, inventory)->{
      Inventory inv = inventory;
      inv.setItem(tab - 1, VaultTab.buildTabIcon(icon.getStack(), tab, tabIndex));
      openTabs.put(tabIndex, inv);
    });

    openTabs.forEach((order, inventory)->{

      final List<UUID> viewers = ((VaultInventoryHolder)inventory.getHolder()).viewers;
      viewers.forEach(id->IDFinder.getPlayer(id.toString()).getOpenInventory().getTopInventory().setContents(inventory.getContents()));
    });
  }

  public void updateItem(int tab, int slot, ItemStack item) {
    Inventory inventory = openTabs.get(tab);
    inventory.setItem(slot, item);
    openTabs.put(tab, inventory);

    final List<UUID> viewers = ((VaultInventoryHolder)inventory.getHolder()).viewers;
    viewers.forEach(id->IDFinder.getPlayer(id.toString()).getOpenInventory().getTopInventory().setContents(inventory.getContents()));
  }

  public void addViewer(UUID viewer, int tab) {
    Inventory inventory = openTabs.get(tab);
    ((VaultInventoryHolder)inventory.getHolder()).addViewer(viewer);

    openTabs.put(tab, inventory);
    totalViewers.add(viewer);
    Bukkit.getScheduler().runTask(TNE.instance(), ()->IDFinder.getPlayer(viewer.toString()).openInventory(openTabs.get(tab)));
  }

  public void removeViewer(UUID viewer, int tab) {
    if(forceClosing) return;
    Inventory inventory = openTabs.get(tab);
    ((VaultInventoryHolder)inventory.getHolder()).removeViewer(viewer);

    openTabs.put(tab, inventory);
    totalViewers.remove(viewer);
  }

  public void close() {
    if(forceClosing) return;
    Vault vault = VaultsModule.instance().manager().getVault(owner, world);
    forceClosing = true;
    openTabs.forEach((tab, inventory)->{
      vault.setItems(tab, inventory);
      final List<UUID> viewers = ((VaultInventoryHolder)inventory.getHolder()).viewers;
      viewers.forEach(id->{
        Bukkit.getPlayer(id).closeInventory();
      });

    });
    VaultsModule.instance().manager().addVault(vault);
  }

  public boolean shouldClose() {
    System.out.println("Total close.");
    return totalViewers.size() == 0;
  }
}