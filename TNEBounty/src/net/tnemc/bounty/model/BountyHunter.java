package net.tnemc.bounty.model;

import java.util.UUID;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * Created by creatorfromhell on 6/28/2019.
 * <p>
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */
public class BountyHunter {

  private UUID id;
  private long lastBounty = 0;
  private long experience = 0;
  private long bounties = 0;
  private long lastTrack = 0;
  private String message = "Generic";
  private int level = 1;

  public BountyHunter(UUID id) {
    this.id = id;
  }

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public long getLastBounty() {
    return lastBounty;
  }

  public void setLastBounty(long lastBounty) {
    this.lastBounty = lastBounty;
  }

  public long getExperience() {
    return experience;
  }

  public void setExperience(long experience) {
    this.experience = experience;
  }

  public long getBounties() {
    return bounties;
  }

  public void setBounties(long bounties) {
    this.bounties = bounties;
  }

  public long getLastTrack() {
    return lastTrack;
  }

  public void setLastTrack(long lastTrack) {
    this.lastTrack = lastTrack;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public int getLevel() {
    return level;
  }

  public void setLevel(int level) {
    this.level = level;
  }
}