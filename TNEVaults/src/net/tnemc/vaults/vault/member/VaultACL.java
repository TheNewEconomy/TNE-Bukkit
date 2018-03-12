package net.tnemc.vaults.vault.member;

/**
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
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