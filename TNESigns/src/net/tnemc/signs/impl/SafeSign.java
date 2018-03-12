package net.tnemc.signs.impl;

import org.bukkit.Location;

import java.util.UUID;

/**
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by Daniel on 2/13/2018.
 */
public class SafeSign extends TNESign {

  private UUID account = null;

  public SafeSign(UUID owner, Location location) {
    super(owner, "safe", location, "tne.sign.safe.create", "tne.sign.safe.use");
  }

  public UUID getAccount() {
    return account;
  }

  public void setAccount(UUID account) {
    this.account = account;
  }
}