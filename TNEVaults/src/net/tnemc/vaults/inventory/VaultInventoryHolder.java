package net.tnemc.vaults.inventory;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
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