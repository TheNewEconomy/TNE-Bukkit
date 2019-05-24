package net.tnemc.vaults.vault;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by Daniel on 11/15/2017.
 */
public class UserVaultManager {
  private Map<String, Vault> vaults = new HashMap<>();

  private UUID user;

  public UserVaultManager(UUID user) {
    this.user = user;
  }

  public UserVaultManager(UUID user, Map<String, Vault> vaults) {
    this.user = user;
    this.vaults = vaults;
  }

  public boolean hasVault(String world) {
    return vaults.containsKey(world);
  }

  public void addVault(Vault vault) {
    vaults.put(vault.getWorld(), vault);
  }

  public Vault getVault(String world) {
    return vaults.get(world);
  }

  public void removeVault(String world) {
    vaults.remove(world);
  }
}