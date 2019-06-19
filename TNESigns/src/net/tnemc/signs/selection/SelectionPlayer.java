package net.tnemc.signs.selection;

import org.bukkit.Location;

import java.util.UUID;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * Created by creatorfromhell on 6/19/2019.
 * <p>
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */
public class SelectionPlayer {

  private UUID identifier;
  private Location sign;
  private String type;

  public SelectionPlayer(UUID identifier, Location sign, String type) {
    this.identifier = identifier;
    this.sign = sign;
    this.type = type;
  }

  public UUID getIdentifier() {
    return identifier;
  }

  public void setIdentifier(UUID identifier) {
    this.identifier = identifier;
  }

  public Location getSign() {
    return sign;
  }

  public void setSign(Location sign) {
    this.sign = sign;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }
}