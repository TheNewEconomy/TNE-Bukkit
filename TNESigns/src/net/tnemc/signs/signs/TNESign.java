package net.tnemc.signs.signs;

import org.bukkit.Location;

import java.util.UUID;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * Created by Daniel on 5/28/2018.
 * <p>
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */
public class TNESign {

  private Location location;
  private Location attached;
  private String type;
  private UUID owner;

  public TNESign(Location location, Location attached, String type, UUID owner) {
    this.location = location;
    this.attached = attached;
    this.type = type;
    this.owner = owner;
  }

  public Location getLocation() {
    return location;
  }

  public void setLocation(Location location) {
    this.location = location;
  }

  public Location getAttached() {
    return attached;
  }

  public void setAttached(Location attached) {
    this.attached = attached;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public UUID getOwner() {
    return owner;
  }

  public void setOwner(UUID owner) {
    this.owner = owner;
  }
}