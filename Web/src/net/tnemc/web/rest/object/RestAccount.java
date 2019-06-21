package net.tnemc.web.rest.object;

import java.util.HashMap;
import java.util.Map;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * Created by creatorfromhell on 6/16/2019.
 * <p>
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */
public class RestAccount {

  private Map<String, RestWorldHoldings> holdingsMap = new HashMap<>();

  //Main Attributes
  private String display;
  private String identifier;
  private boolean player;

  //Extra Attributes
  private String language;
  private long joined;
  private long lastOnline;

  public RestAccount(String identifier) {
    this.identifier = identifier;
  }

  public Map<String, RestWorldHoldings> getHoldingsMap() {
    return holdingsMap;
  }

  public void setHoldingsMap(Map<String, RestWorldHoldings> holdingsMap) {
    this.holdingsMap = holdingsMap;
  }

  public String getDisplay() {
    return display;
  }

  public void setDisplay(String display) {
    this.display = display;
  }

  public String getIdentifier() {
    return identifier;
  }

  public void setIdentifier(String identifier) {
    this.identifier = identifier;
  }

  public boolean isPlayer() {
    return player;
  }

  public void setPlayer(boolean player) {
    this.player = player;
  }

  public String getLanguage() {
    return language;
  }

  public void setLanguage(String language) {
    this.language = language;
  }

  public long getJoined() {
    return joined;
  }

  public void setJoined(long joined) {
    this.joined = joined;
  }

  public long getLastOnline() {
    return lastOnline;
  }

  public void setLastOnline(long lastOnline) {
    this.lastOnline = lastOnline;
  }
}