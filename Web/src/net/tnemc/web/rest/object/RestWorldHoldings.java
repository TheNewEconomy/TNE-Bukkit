package net.tnemc.web.rest.object;

import java.math.BigDecimal;
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
public class RestWorldHoldings {

  private Map<String, BigDecimal> holdings = new HashMap<>();

  private String world;

  public RestWorldHoldings(String world) {
    this.world = world;
  }

  public Map<String, BigDecimal> getHoldings() {
    return holdings;
  }

  public void setHoldings(Map<String, BigDecimal> holdings) {
    this.holdings = holdings;
  }

  public String getWorld() {
    return world;
  }

  public void setWorld(String world) {
    this.world = world;
  }
}