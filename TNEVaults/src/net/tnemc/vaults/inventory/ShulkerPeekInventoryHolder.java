package net.tnemc.vaults.inventory;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import java.util.ArrayList;
import java.util.List;
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
 * Created by Daniel on 11/12/2017.
 */
public class ShulkerPeekInventoryHolder implements InventoryHolder {

  public List<UUID> viewers = new ArrayList<>();

  private UUID vault;
  private String world;
  private int slot;
  private int previousTab;
  private boolean readOnly;

  public ShulkerPeekInventoryHolder(UUID vault, String world, int slot, int previousTab, boolean readOnly) {
    this.vault = vault;
    this.world = world;
    this.slot = slot;
    this.previousTab = previousTab;
    this.readOnly = readOnly;
  }

  @Override
  public Inventory getInventory() {
    return null;
  }

  public UUID getVault() {
    return vault;
  }

  public void setVault(UUID vault) {
    this.vault = vault;
  }

  public String getWorld() {
    return world;
  }

  public int getPreviousTab() {
    return previousTab;
  }

  public void setPreviousTab(int previousTab) {
    this.previousTab = previousTab;
  }

  public List<UUID> getViewers() {
    return viewers;
  }

  public void setViewers(List<UUID> viewers) {
    this.viewers = viewers;
  }

  public int getSlot() {
    return slot;
  }

  public void setSlot(int slot) {
    this.slot = slot;
  }

  public boolean isReadOnly() {
    return readOnly;
  }

  public void setReadOnly(boolean readOnly) {
    this.readOnly = readOnly;
  }

  public void addViewer(UUID id) {
    viewers.add(id);
  }

  public void removeViewer(UUID id) {
    viewers.remove(id);
  }

  public void clearViewers() {
    viewers.clear();
  }
}