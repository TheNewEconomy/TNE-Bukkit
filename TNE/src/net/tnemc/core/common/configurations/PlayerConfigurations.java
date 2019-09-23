package net.tnemc.core.common.configurations;

import net.tnemc.config.CommentedConfiguration;
import net.tnemc.core.TNE;
import net.tnemc.core.common.configurations.player.PlayerValues;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
public class PlayerConfigurations extends Configuration {

  private Map<String, PlayerValues> values = new HashMap<>();

  @Override
  public CommentedConfiguration getConfiguration() {
    return TNE.instance().playerConfiguration();
  }

  @Override
  public List<String> node() {
    return Collections.singletonList("Players");
  }

  @Override
  public File getFile() {
    return TNE.instance().getPlayers();
  }


  @Override
  public void load(CommentedConfiguration configurationFile) {

    Set<String> players = configurationFile.getSection("Players").getKeys(false);

    for(String player : players) {
      PlayerValues pValues = new PlayerValues(player);

      final String base = "Players." + player;
      Set<String> configurations = configurationFile.getSection(base).getKeys(true);

      for(String s : configurations) {
        String node = base + "." + s;
        if(!configurationFile.isConfigurationSection(node)) {
          pValues.addNode(s, configurationFile.getString(node));
        }
      }
      values.put(player, pValues);
    }
  }

  public String getValue(String identifier, String node) {
    if(values.containsKey(identifier)) {
      return values.get(identifier).getValue(node);
    }
    return null;
  }

  public boolean hasValue(String identifier, String node) {
    return getValue(identifier, node) != null;
  }
}
