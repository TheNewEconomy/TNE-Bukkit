/*
 * The New Economy Minecraft Server Plugin
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.

 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.tnemc.core.common.configurations;

import com.github.tnerevival.TNELib;
import net.tnemc.config.CommentedConfiguration;
import net.tnemc.core.TNE;
import net.tnemc.core.common.WorldVariant;
import net.tnemc.core.common.account.WorldFinder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by creatorfromhell on 1/5/2017.
 **/
public class ConfigurationManager {

  public boolean save = false;
  public List<String> loaded = new ArrayList<>();

  public HashMap<String, Configuration> configurations = new HashMap<>();

  public ConfigurationManager() {
    loadAll();
  }

  public void add(Configuration configuration, String identifier) {
    configurations.put(identifier, configuration);
  }

  public void loadAll() {
    for(Map.Entry<String, Configuration> entries : configurations.entrySet()) {
      entries.getValue().load(entries.getValue().getConfiguration());
      loaded.add(entries.getKey());
    }
  }

  public void updateLoad() {
    for(Map.Entry<String, Configuration> entries : configurations.entrySet()) {
      if(!loaded.contains(entries.getKey())) {
        entries.getValue().load(entries.getValue().getConfiguration());
        loaded.add(entries.getKey());
      }
    }
  }

  private CommentedConfiguration getFileConfiguration(String id) {
    return configurations.get(id).getConfiguration();
  }

  private Configuration getConfiguration(String id) {
    return configurations.get(id);
  }

  public void load(CommentedConfiguration configurationFile, String configID) {
    Configuration config = getConfiguration(configID);
    config.load(configurationFile);

    add(config, configID);
  }

  public void save(CommentedConfiguration configurationFile, String configID) {
    getConfiguration(configID).save(configurationFile);
  }

  public void saveAll() {
    for(Configuration configuration : configurations.values()) {
      configuration.save(configuration.getConfiguration());
    }
  }

  public void save(Configuration configuration) {
    configuration.save(configuration.getConfiguration());
  }


  public boolean reload(String configID) {
    if(configID.equalsIgnoreCase("all")) {
      TNE.instance().loadConfigurations();
      return true;
    } else if(configID.equalsIgnoreCase("currency")) {
      TNE.manager().currencyManager().loadCurrencies();
      TNE.manager().currencyManager().loadRecipes();
    } else if(loaded.contains(configID)) {
      load(configurations.get(configID).getConfiguration(), configID);
      return true;
    }
    return false;
  }

  public Object getValue(String node) {
    String prefix = getPrefix(node);
    return getConfiguration(fromPrefix(prefix)).getValue(node);
  }

  public Object getValue(String node, String configuration, String world, String player) {

    if(TNE.instance().playerConfigurations().hasValue(player, node)) {
      return TNE.instance().playerConfigurations().getValue(player, node);
    }

    if(TNE.instance().hasWorldManager(world) && TNE.instance().getWorldManager(WorldFinder.getWorldName(world, WorldVariant.CONFIGURATION)).configExists(node)) {
      return TNE.instance().getWorldManager(WorldFinder.getWorldName(world, WorldVariant.CONFIGURATION)).getConfiguration(node);
    }

    return getConfiguration(configuration).getValue(node, world, player);
  }

  public void setValue(String node, Object value) {
    String prefix = getPrefix(node);
    getConfiguration(fromPrefix(prefix)).setValue(node, value);
  }

  public void setValue(String node, String configuration, Object value) {
    getConfiguration(configuration).setValue(node, value);
  }

  public Boolean hasNode(String node, String configuration) {
    return getConfiguration(configuration).hasNode(node);
  }

  public Boolean getBoolean(String node) {
    return getBoolean(node, "main");
  }

  public Integer getInt(String node) {
    return getInt(node, "main");
  }

  public Double getDouble(String node) {
    return getDouble(node, "main");
  }

  public Long getLong(String node) {
    return getLong(node, "main");
  }

  public String getString(String node) {
    return getString(node, "main");
  }

  public Boolean getBoolean(String node, String configID) {
    return Boolean.valueOf(getConfiguration(configID).getValue(node).toString());
  }

  public Integer getInt(String node, String configID) {
    return Integer.valueOf(getValue(node, configID, TNELib.instance().defaultWorld, "").toString());
  }

  public Double getDouble(String node, String configID) {
    return Double.valueOf(getValue(node, configID, TNELib.instance().defaultWorld, "").toString());
  }

  public Long getLong(String node, String configID) {
    return Long.valueOf(getInt(node, configID));
  }

  public String getString(String node, String configID) {
    if(getValue(node, configID, TNELib.instance().defaultWorld, "") == null) return null;
    return getValue(node, configID, TNELib.instance().defaultWorld, "").toString();
  }

  public String getString(String node, String configID, String world, String player) {
    if(getValue(node, configID, world, player) == null) return null;
    return getValue(node, configID, world, player).toString();
  }

  /*
   * Helper methods for configurations.
   */
  public Object getConfiguration(String configuration, String world, String player) {
    String[] exploded = configuration.split("\\.");
    String path = configuration;
    String prefix = "Core";
    if(containsPrefix(exploded[0])) {
      prefix = exploded[0];
      if(fromPrefix(prefix).equalsIgnoreCase("main")) {
        path = path.replace(prefix + ".", "");
      }
    }
    if(prefix.equalsIgnoreCase("messages")) {
      return TNE.instance().messages().getValue(configuration, world, player);
    }
    return getValue(configuration, fromPrefix(prefix), world, player);
  }

  public void setConfiguration(String configuration, Object value) {
    String prefix = getPrefix(configuration);
    setValue(configuration, fromPrefix(prefix), value);
  }

  public Boolean hasConfiguration(String configuration) {
    String[] exploded = configuration.split("\\.");
    String prefix = "Core";
    if(containsPrefix(exploded[0])) {
      prefix = exploded[0];
    }
    return hasNode(configuration, fromPrefix(prefix));
  }

  public Boolean containsPrefix(String prefix) {
    for(Configuration c : configurations.values()) {
      if(c.node().contains(prefix)) {
        return true;
      }
    }
    return false;
  }

  public String fromPrefix(String prefix) {
    for(Map.Entry<String, Configuration> entry : configurations.entrySet()) {
      if(entry.getValue().node().contains(prefix)) {
        return entry.getKey();
      }
    }
    return "main";
  }

  public String getPrefix(String node) {
    String[] exploded = node.split("\\.");
    String prefix = "Core";
    if(this.containsPrefix(exploded[0])) {
      prefix = exploded[0];
    }
    return prefix;
  }
}
