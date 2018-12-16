package net.tnemc.vaults.vault.member;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by Daniel on 11/10/2017.
 */
public class VaultMember {

  private List<String> permissions = new ArrayList<>();

  private UUID id;
  private UUID vault;
  private String world;

  public VaultMember(UUID id, UUID vault, String world) {
    this.id = id;
    this.vault = vault;
    this.world = world;
  }

  public void addPermission(VaultACL acl) {
    addPermission(acl.getPermission());
  }

  public void addPermission(String permission) {
    permissions.add(permission);
  }

  public List<String> getPermissions() {
    return permissions;
  }

  public void setPermissions(List<String> permissions) {
    this.permissions = permissions;
  }

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
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

  public void setWorld(String world) {
    this.world = world;
  }
}