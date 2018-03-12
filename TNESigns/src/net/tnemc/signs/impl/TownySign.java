package net.tnemc.signs.impl;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by Daniel on 2/26/2018.
 */
public class TownySign extends TNESign {

  private String account = null;

  public TownySign(UUID owner, Location location) {
    super(owner, "towny", location, "tne.sign.towny.create", "tne.sign.towny.use");
    this.requiresChest = true;
  }

  /**
   * Called when a player attempts to create a TNE sign.
   *
   * @param player
   * @return Whether or not the action was performed successfully.
   */
  @Override
  public boolean onCreate(Player player) {
    return Bukkit.getPluginManager().getPlugin("Towny") != null && super.onCreate(player);
  }

  @Override
  public boolean onChestOpenAttempt(Player player) {
    boolean town = (account.contains("town-"));
    return false;
  }

  public String getAccount() {
    return account;
  }

  public void setAccount(String account) {
    this.account = account;
  }
}