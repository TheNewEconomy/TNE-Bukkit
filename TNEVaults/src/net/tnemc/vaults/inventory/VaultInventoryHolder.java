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
 * Created by Daniel on 11/9/2017.
 */
public class VaultInventoryHolder implements InventoryHolder {

  public List<UUID> viewers = new ArrayList<>();

  private int tab;
  private UUID owner;
  private String world;

  public VaultInventoryHolder(int tab, UUID owner, String world) {
    this.tab = tab;
    this.owner = owner;
    this.world = world;
  }

  @Override
  public Inventory getInventory() {
    return null;
  }

  public int getTab() {
    return tab;
  }

  public void setTab(int tab) {
    this.tab = tab;
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