package net.tnemc.vaults.vault.member;

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