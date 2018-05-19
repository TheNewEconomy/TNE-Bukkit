package net.tnemc.core.common.configurations;

import com.github.tnerevival.TNELib;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public abstract class Configuration {

  public abstract FileConfiguration getConfiguration();

  public abstract List<String> node();

  public Map<String, Object> configurations = new HashMap<>();
  public Map<String, Object> modified = new HashMap<>();

  public void load(FileConfiguration configurationFile) {
    Iterator<java.util.Map.Entry<String, Object>> it = configurations.entrySet().iterator();

    while(it.hasNext()) {
      java.util.Map.Entry<String, Object> entry = it.next();
      if(configurationFile.contains(entry.getKey())) {
        setValue(entry.getKey(), configurationFile.get(entry.getKey()));
      }
    }
  }

  public void save(FileConfiguration configurationFile) {
    if(!new File(TNELib.instance().getDataFolder(), configurationFile.getName()).exists()) {
      Iterator<Map.Entry<String, Object>> iterator = modified.entrySet().iterator();
      while(iterator.hasNext()) {
        Map.Entry<String, Object> entry = iterator.next();
        if(configurationFile.contains(entry.getKey())) {
          configurationFile.set(entry.getKey(), entry.getValue());
        }
        iterator.remove();
      }
    }
  }

  public boolean acceptableValue(String node, Object object) {
    return (configurations.get(node).getClass().equals(object.getClass()));
  }

  public Boolean hasNode(String node) {
    return configurations.get(node) != null;
  }

  public Object getValue(String node) {
    return configurations.get(node);
  }

  public Object getValue(String node, String world) {
    return getValue(node);
  }

  public Object getValue(String node, String world, String player) {
    return getValue(node);
  }

  public void setValue(String node, Object value) {
    modified.put(node, value);
    configurations.put(node, value);
  }

}