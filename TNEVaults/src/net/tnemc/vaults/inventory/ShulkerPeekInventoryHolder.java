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