package net.tnemc.vaults;

import net.tnemc.core.TNE;
import net.tnemc.vaults.vault.OpenVault;
import net.tnemc.vaults.vault.UserVaultManager;
import net.tnemc.vaults.vault.Vault;

import java.util.*;

/**
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by Daniel on 11/9/2017.
 */
public class VaultManager {
  private Map<UUID, UserVaultManager> managers = new HashMap<>();
  private Map<String, OpenVault> open = new HashMap<>();

  private List<UUID> readOnly = new ArrayList<>();

  public VaultManager() {
  }

  public boolean hasVault(UUID owner, String world) {
    return managers.containsKey(owner) && managers.get(owner).hasVault(world);
  }

  public void addVault(Vault vault) {
    if(!managers.containsKey(vault.getOwner())) managers.put(vault.getOwner(), new UserVaultManager(vault.getOwner()));
    managers.get(vault.getOwner()).addVault(vault);
  }

  public Vault getVault(UUID owner, String world) {
    return managers.get(owner).getVault(world);
  }

  public boolean isReadOnly(UUID viewer) {
    return readOnly.contains(viewer);
  }

  public boolean isOpen(UUID owner, String world) {
    return open.containsKey(owner.toString() + ":" + world);
  }

  public void open(UUID player, UUID owner, String world, int tab, boolean read) {
    String key = owner.toString() + ":" + world;
    if(!isOpen(owner, world)) {
      OpenVault openVault = new OpenVault(owner, world);
      open.put(key, openVault);
    }
    open.get(key).addViewer(player, tab);
    if(read) {
      readOnly.add(player);
    }
  }

  public void updateIcon(UUID owner, String world, int tab) {
    String key = owner.toString() + ":" + world;
    OpenVault openVault = open.get(key);
    openVault.updateIcon(tab, getVault(owner, world).getIcon(tab));
    TNE.debug("VaultManager.updateIcon: Tab: " + tab);
    TNE.debug("New Icon: " + getVault(owner, world).getIcon(tab).getStack().toString());
    open.put(key, openVault);
  }

  public void removeViewer(UUID player, UUID owner, String world, int tab) {
    String key = owner.toString() + ":" + world;
    OpenVault openVault = open.get(key);
    open.get(key).removeViewer(player, tab);
    readOnly.remove(player);
    if(openVault.shouldClose()) {
      close(owner, world);
    }
  }

  public void close(UUID owner, String world) {
    String key = owner.toString() + ":" + world;
    open.get(key).close();
  }
}