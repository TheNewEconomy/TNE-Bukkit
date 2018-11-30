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
  private UUID creator;
  private Long creationDate;
  private int step;

  public TNESign(Location location, Location attached, String type, UUID owner, UUID creator, long creationDate, int step) {
    this.location = location;
    this.attached = attached;
    this.type = type;
    this.owner = owner;
    this.creator = creator;
    this.creationDate = creationDate;
    this.step = step;
  }

  public TNESign(Location location, Location attached, String type, UUID owner, UUID creator, long creationDate, int step, String data) {
    this.location = location;
    this.attached = attached;
    this.type = type;
    this.owner = owner;
    this.creator = creator;
    this.creationDate = creationDate;
    this.step = step;
    loadExtraData(data);
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

  public UUID getCreator() {
    return creator;
  }

  public void setCreator(UUID creator) {
    this.creator = creator;
  }

  public Long getCreationDate() {
    return creationDate;
  }

  public void setCreationDate(Long creationDate) {
    this.creationDate = creationDate;
  }

  public int getStep() {
    return step;
  }

  public void setStep(int step) {
    this.step = step;
  }

  public String saveExtraData() {
    return "";
  }

  public void loadExtraData(String data) {

  }
}