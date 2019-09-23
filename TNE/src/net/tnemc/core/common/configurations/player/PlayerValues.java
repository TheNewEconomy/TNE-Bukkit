package net.tnemc.core.common.configurations.player;

import java.util.HashMap;
import java.util.Map;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * Created by creatorfromhell on 9/23/2019.
 * <p>
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */
public class PlayerValues {

  Map<String, String> nodes = new HashMap<>();

  private String identifier;

  public PlayerValues(String identifier) {
    this.identifier = identifier;
  }

  public String addNode(String node, String value) {
    return nodes.put(node, value);
  }

  public String getValue(String node) {
    return nodes.getOrDefault(node, null);
  }

  public Map<String, String> getNodes() {
    return nodes;
  }

  public void setNodes(Map<String, String> nodes) {
    this.nodes = nodes;
  }

  public String getIdentifier() {
    return identifier;
  }

  public void setIdentifier(String identifier) {
    this.identifier = identifier;
  }
}