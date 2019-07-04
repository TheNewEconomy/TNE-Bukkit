package net.tnemc.bounty.model;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * Created by creatorfromhell on 7/4/2019.
 * <p>
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */
public class HunterLevel {

  private int level;
  private long experienceGain = 2;
  private boolean head;
  private String deathMessage;
  private String command;
  private short headChance;

  public HunterLevel(int level, long experienceGain, boolean head, String deathMessage, String command) {
    this.level = level;
    this.experienceGain = experienceGain;
    this.head = head;
    this.deathMessage = deathMessage;
    this.command = command;
    this.headChance = 100;
  }

  public int getLevel() {
    return level;
  }

  public void setLevel(int level) {
    this.level = level;
  }

  public long getExperienceGain() {
    return experienceGain;
  }

  public void setExperienceGain(long experienceGain) {
    this.experienceGain = experienceGain;
  }

  public boolean canHead() {
    return head;
  }

  public void setHead(boolean head) {
    this.head = head;
  }

  public String getDeathMessage() {
    return deathMessage;
  }

  public void setDeathMessage(String deathMessage) {
    this.deathMessage = deathMessage;
  }

  public String getCommand() {
    return command;
  }

  public void setCommand(String command) {
    this.command = command;
  }

  public short getHeadChance() {
    return headChance;
  }

  public void setHeadChance(short headChance) {
    this.headChance = headChance;
  }
}