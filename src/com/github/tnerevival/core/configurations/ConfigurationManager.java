package com.github.tnerevival.core.configurations;

import com.github.tnerevival.TNE;
import com.github.tnerevival.core.configurations.impl.*;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;
import java.util.UUID;

public class ConfigurationManager {

  public boolean save = false;

  public HashMap<String, Configuration> configurations = new HashMap<>();

  public ConfigurationManager() {
    loadAll();
  }

  public void loadAll() {
    MainConfiguration main = new MainConfiguration();
    MessageConfiguration message = new MessageConfiguration();
    MobConfiguration mob = new MobConfiguration();
    ObjectConfiguration objects = new ObjectConfiguration();
    MaterialsConfiguration materials = new MaterialsConfiguration();
    main.load(TNE.instance.getConfig());
    message.load(TNE.instance.messageConfigurations);
    mob.load(TNE.instance.mobConfigurations);
    objects.load(TNE.instance.objectConfigurations);
    materials.load(TNE.instance.materialConfigurations);

    configurations.put("main", main);
    configurations.put("mob", mob);
    configurations.put("messages", message);
    configurations.put("objects", objects);
    configurations.put("materials", materials);
  }

  public Boolean playerEnabled(UUID id, String world, String player) {
    return TNE.instance.api.getBoolean("Mobs.Player.Individual." + id.toString() + ".Enabled", world, player);
  }

  public Double playerReward(String id, String world, String player) {
    return TNE.instance.api.getDouble("Mobs.Player.Individual." + id + ".Reward", world, player);
  }

  public Boolean mobAge(String world, String player) {
    return TNE.instance.api.getBoolean("Mobs.EnableAge", world, player);
  }

  public Boolean mobEnabled(String mob, String world, String player) {
    return TNE.instance.api.getBoolean("Mobs." + mob + ".Enabled", world, player);
  }

  public Double mobReward(String mob, String world, String player) {
    return TNE.instance.api.getDouble("Mobs." + mob + ".Reward", world, player);
  }

  private FileConfiguration getFileConfiguration(String id) {
    switch(id) {
      case "messages":
        return TNE.instance.messageConfigurations;
      case "mob":
        return TNE.instance.mobConfigurations;
      case "objects":
        return TNE.instance.objectConfigurations;
      case "materials":
        return TNE.instance.materialConfigurations;
      default:
        return TNE.instance.getConfig();
    }
  }

  private Configuration getConfiguration(String id) {
    return configurations.get(id);
  }

  public ObjectConfiguration getObjectConfiguration() {
    return (ObjectConfiguration)getConfiguration("objects");
  }

  public MaterialsConfiguration getMaterialsConfiguration() {
    return (MaterialsConfiguration)getConfiguration("materials");
  }

  public void load(FileConfiguration configurationFile, String configID) {
    getConfiguration(configID).load(configurationFile);
  }

  public void save(FileConfiguration configurationFile, String configID) {
    getConfiguration(configID).save(configurationFile);
  }

  public Object getValue(String node, String configuration) {
    return getConfiguration(configuration).getValue(node);
  }

  public void setValue(String node, String configuration, Object value) {
    getConfiguration(configuration).setValue(node, value);
    getConfiguration(configuration).save(getFileConfiguration(configuration));
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
    return (Boolean)getConfiguration(configID).getValue(node);
  }

  public Integer getInt(String node, String configID) {
    return (Integer)getValue(node, configID);
  }

  public Double getDouble(String node, String configID) {
    return (Double)getValue(node, configID);
  }

  public Long getLong(String node, String configID) {
    return Long.valueOf(getInt(node, configID));
  }

  public String getString(String node, String configID) {
    return (String)getValue(node, configID);
  }

  /*
   * Helper methods for configurations.
   */
  public Object getConfiguration(String configuration, String world, String player) {
    String[] exploded = configuration.split("\\.");
    String path = configuration;
    String prefix = "Core";
    if(ConfigurationType.fromPrefix(exploded[0]) != ConfigurationType.UNKNOWN) {
      prefix = exploded[0];
      if(ConfigurationType.fromPrefix(prefix) == ConfigurationType.MAIN) {
        path = path.replace(prefix + ".", "");
      }
    }

    if(!player.trim().equals("") && playerEnabled(path, player)) return getPlayerConfiguration(path, player);
    if(getBoolean("Core.Multiworld") && worldEnabled(path, world)) return getWorldConfiguration(path, world);
    return getValue(configuration, ConfigurationType.fromPrefix(prefix).getIdentifier());
  }

  public void setConfiguration(String configuration, Object value) {
    String[] exploded = configuration.split("\\.");
    String prefix = "Core";
    if(ConfigurationType.fromPrefix(exploded[0]) != ConfigurationType.UNKNOWN) {
      prefix = exploded[0];
    }
    setValue(configuration, ConfigurationType.fromPrefix(prefix).getIdentifier(), value);
  }

  public Boolean hasConfiguration(String configuration) {
    String[] exploded = configuration.split("\\.");
    String prefix = "Core";
    if(ConfigurationType.fromPrefix(exploded[0]) != ConfigurationType.UNKNOWN) {
      prefix = exploded[0];
    }
    return hasNode(configuration, ConfigurationType.fromPrefix(prefix).getIdentifier());
  }

  public boolean playerEnabled(String node, String player) {
    String path = ConfigurationType.PLAYERS.getPrefix() + "." + player + "." + node;
    return TNE.instance.playerConfigurations.contains(path);
  }

  public Object getPlayerConfiguration(String node, String player) {
    String path = ConfigurationType.PLAYERS.getPrefix() + "." + player + "." + node;
    return TNE.instance.playerConfigurations.get(path);
  }

  public boolean worldEnabled(String node, String world) {
    String path = ConfigurationType.WORLDS.getPrefix() + "." + world + "." + node;
    return TNE.instance.worldConfigurations.contains(path);
  }

  public Object getWorldConfiguration(String node, String world) {
    String path = ConfigurationType.WORLDS.getPrefix() + "." + world + "." + node;
    return TNE.instance.worldConfigurations.get(path);
  }
}