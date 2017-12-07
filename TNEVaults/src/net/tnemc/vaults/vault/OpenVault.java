package net.tnemc.vaults.vault;

import com.github.tnerevival.user.IDFinder;
import net.tnemc.core.TNE;
import net.tnemc.core.item.SerialItem;
import net.tnemc.vaults.VaultsModule;
import net.tnemc.vaults.inventory.VaultInventoryHolder;
import org.bukkit.inventory.Inventory;

import java.util.*;

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
 * Created by Daniel on 11/17/2017.
 */
public class OpenVault {
  private Map<Integer, Inventory> openTabs = new HashMap<>();
  private List<UUID> totalViewers = new ArrayList<>();

  private UUID owner;
  private String world;

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
      ((VaultInventoryHolder)inventory.getHolder()).viewers.forEach((id)->{
        IDFinder.getPlayer(id.toString()).openInventory(inventory);
      });
    });
  }

  public void addViewer(UUID viewer, int tab) {
    Inventory inventory = openTabs.get(tab);
    ((VaultInventoryHolder)inventory.getHolder()).addViewer(viewer);

    openTabs.put(tab, inventory);
    totalViewers.add(viewer);
    IDFinder.getPlayer(viewer.toString()).openInventory(openTabs.get(tab));
  }

  public void removeViewer(UUID viewer, int tab) {
    Inventory inventory = openTabs.get(tab);
    ((VaultInventoryHolder)inventory.getHolder()).removeViewer(viewer);

    openTabs.put(tab, inventory);
    totalViewers.remove(viewer);
  }

  public void close() {
    Vault vault = VaultsModule.instance().manager().getVault(owner, world);
    openTabs.forEach((tab, inventory)->vault.setItems(tab, inventory));
    VaultsModule.instance().manager().addVault(vault);
  }

  public boolean shouldClose() {
    return totalViewers.size() == 0;
  }
}