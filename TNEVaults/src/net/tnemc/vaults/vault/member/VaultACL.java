package net.tnemc.vaults.vault.member;

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
 * Created by Daniel on 11/13/2017.
 */
public enum VaultACL {
  OPEN("Open", "vault.member.open", "Needed to open a certain vault."),
  BLACKLIST("Blacklist", "vault.member.blacklist", "Needed to blacklist certain items from a vault."),
  DEPOSIT("Deposit", "vault.member.deposit", "Needed to deposit items."),
  WITHDRAW("Withdraw", "vault.member.withdraw", "Needed to withdraw items."),
  PEEK("Peek", "vault.member.peek", "Needed to peek inside shulker boxes"),
  ADD("Add", "vault.member.add", "Needed to add members to vault."),
  REMOVE("Remove", "vault.member.remove", "Needed to remove members from vault."),
  TABS("Tabs", "vault.member.tabs", "Needed to modify tab icons"),
  MODIFY("Modify", "vault.member.modify", "Needed to modify members' ACLs."),
  NOMOD("NoMod", "vault.member.nomod", "Only the vault owner may modify this member's ACLs.");

  String name;
  String permission;
  String description;
  VaultACL(String name, String permission, String description) {
    this.name = name;
    this.permission = permission;
    this.description = description;
  }

  public String getName() {
    return name;
  }

  public String getPermission() {
    return permission;
  }

  public String getDescription() {
    return description;
  }

  public VaultACL getFromPermission(String permission){
    for(VaultACL acl : values()) {
      if(acl.getPermission().equalsIgnoreCase(permission)) return acl;
    }
   return null;
  }
}