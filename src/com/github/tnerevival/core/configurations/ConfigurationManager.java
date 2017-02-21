package com.github.tnerevival.core.configurations;

import com.github.tnerevival.TNE;
import com.github.tnerevival.account.IDFinder;
import com.github.tnerevival.core.configurations.impl.*;
import com.github.tnerevival.utils.MISCUtils;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.UUID;

public class ConfigurationManager {

  public boolean save = false;

  public HashMap<String, Configuration> configurations = new HashMap<>();

  public ConfigurationManager() {
    loadAll();
  }

  private void loadAll() {
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
    MISCUtils.debug("ConfigurationManager.playerEnabled(" + id.toString() + ", " + world + "," + player + ")");
    return TNE.instance.api.getBoolean("Mobs.Player.Individual." + id.toString() + ".Enabled", world, player);
  }

  public BigDecimal playerReward(String id, String world, String player) {
    MISCUtils.debug("ConfigurationManager.playerReward(" + id + ", " + world + "," + player + ")");
    return new BigDecimal(TNE.instance.api.getDouble("Mobs.Player.Individual." + id + ".Reward", world, player));
  }

  public Boolean mobAge(String world, String player) {
    MISCUtils.debug("ConfigurationManager.mobAge(" + world + "," + player + ")");
    return TNE.instance.api.getBoolean("Mobs.EnableAge", world, player);
  }

  public Boolean mobEnabled(String mob, String world, String player) {
    MISCUtils.debug("ConfigurationManager.mobEnabled(" + mob + ", " + world + "," + player + ")");
    return TNE.instance.api.getBoolean("Mobs." + mob + ".Enabled", world, player);
  }

  public BigDecimal mobReward(String mob, String world, String player) {
    MISCUtils.debug("ConfigurationManager.mobReward(" + mob + ", " + world + "," + player + ")");
    return new BigDecimal(TNE.instance.api.getDouble("Mobs." + mob + ".Reward", world, player));
  }

  public String mobCurrency(String mob, String world, String player) {
    String currency = TNE.instance.api.getString("Mobs." + mob + ".Currency", world, player);
    return (currency != null)? currency : TNE.instance.manager.currencyManager.get(world).getName();
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
    MISCUtils.debug("ConfigurationManager.getValue(" + node + ", " + configuration + ")");
    return getConfiguration(configuration).getValue(node);
  }

  private void setValue(String node, String configuration, Object value) {
    getConfiguration(configuration).setValue(node, value);
    getConfiguration(configuration).save(getFileConfiguration(configuration));
  }

  private Boolean hasNode(String node, String configuration) {
    MISCUtils.debug("ConfigurationManager.hasNode(" + node + ", " + configuration + ")");
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
    MISCUtils.debug("ConfigurationManager.getConfigurations(" + configuration + ", " + world + "," + player + ")");
    String[] exploded = configuration.split("\\.");
    String path = configuration;
    String prefix = "Core";
    if(ConfigurationType.fromPrefix(exploded[0]) != ConfigurationType.UNKNOWN) {
      prefix = exploded[0];
      if(ConfigurationType.fromPrefix(prefix) == ConfigurationType.MAIN) {
        path = path.replace(prefix + ".", "");
      }
    }

    if(player != null && !player.trim().equals("") && playerEnabled(path, player)) return getPlayerConfiguration(path, player);
    if(worldEnabled(path, world)) return getWorldConfiguration(path, world);
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

  private boolean playerEnabled(String node, String player) {
    MISCUtils.debug("ConfigurationManager.playerEnabled(" + node + ", " + player + ")");
    String path = ConfigurationType.PLAYERS.getPrefix() + "." + player + "." + node;
    return TNE.instance.playerConfigurations.contains(path);
  }

  private Object getPlayerConfiguration(String node, String player) {
    MISCUtils.debug("ConfigurationManager.getPlayerConfiguration(" + node + ", " + player + ")");
    String path = ConfigurationType.PLAYERS.getPrefix() + "." + player + "." + node;
    return TNE.instance.playerConfigurations.get(path);
  }

  private boolean worldEnabled(String node, String world) {
    MISCUtils.debug("ConfigurationManager.worldEnabled(" + node + ", " + IDFinder.getConfigurationShare(world) + ")");
    String path = ConfigurationType.WORLDS.getPrefix() + "." + IDFinder.getConfigurationShare(world) + "." + node;
    return TNE.instance.worldConfigurations.contains(path);
  }

  private Object getWorldConfiguration(String node, String world) {
    MISCUtils.debug("ConfigurationManager.getWorldConfigurations(" + node + ", " + IDFinder.getConfigurationShare(world) + ")");
    String path = ConfigurationType.WORLDS.getPrefix() + "." + IDFinder.getConfigurationShare(world) + "." + node;
    return TNE.instance.worldConfigurations.get(path);
  }

  /*
   * Reload methods
   */


  public static void reloadConfigurations(String type) {
    if(type.equalsIgnoreCase("all")) {
      TNE.instance.reloadConfig();
      TNE.instance.manager.currencyManager.loadCurrencies();
      reloadConfigsMaterials();
      reloadConfigsMessages();
      reloadConfigsMobs();
      reloadConfigsObjects();
      reloadConfigPlayers();
      reloadConfigsWorlds();
      TNE.instance.manager.currencyManager.loadCurrencies();
    } else if(type.equalsIgnoreCase("config")) {
      TNE.instance.reloadConfig();
      TNE.configurations.load(TNE.instance.getConfig(), "main");
    } else if(type.equalsIgnoreCase("currencies")) {
      TNE.instance.manager.currencyManager.loadCurrencies();
    } else if(type.equalsIgnoreCase("materials")) {
      reloadConfigsMaterials();
    } else if(type.equalsIgnoreCase("messages")) {
      reloadConfigsMessages();
    } else if(type.equalsIgnoreCase("mobs")) {
      reloadConfigsMobs();
    } else if(type.equalsIgnoreCase("objects")) {
      reloadConfigsObjects();
    } else if(type.equalsIgnoreCase("players")) {
      reloadConfigPlayers();
    } else if(type.equalsIgnoreCase("worlds")) {
      reloadConfigsWorlds();
    }
  }

  private static void reloadConfigsMaterials() {
    if(TNE.instance.materials == null) {
      TNE.instance.materials = new File(TNE.instance.getDataFolder(), "materials.yml");
    }
    TNE.instance.materialConfigurations = YamlConfiguration.loadConfiguration(TNE.instance.materials);
    TNE.configurations.load(TNE.instance.materialConfigurations, "materials");
  }

  private static void reloadConfigsMobs() {
    if(TNE.instance.mobs == null) {
      TNE.instance.mobs = new File(TNE.instance.getDataFolder(), "mobs.yml");
    }
    TNE.instance.mobConfigurations = YamlConfiguration.loadConfiguration(TNE.instance.mobs);
    TNE.configurations.load(TNE.instance.mobConfigurations, "mob");
  }

  private static void reloadConfigsMessages() {
    if(TNE.instance.messages == null) {
      TNE.instance.messages = new File(TNE.instance.getDataFolder(), "messages.yml");
    }
    TNE.instance.messageConfigurations = YamlConfiguration.loadConfiguration(TNE.instance.messages);
    TNE.configurations.load(TNE.instance.messageConfigurations, "messages");
  }

  private static void reloadConfigsObjects() {
    if(TNE.instance.objects == null) {
      TNE.instance.objects = new File(TNE.instance.getDataFolder(), "objects.yml");
    }
    TNE.instance.objectConfigurations = YamlConfiguration.loadConfiguration(TNE.instance.objects);
    TNE.configurations.load(TNE.instance.objectConfigurations, "objects");
  }

  private static void reloadConfigPlayers() {
    if(TNE.instance.players == null) {
      TNE.instance.players = new File(TNE.instance.getDataFolder(), "players.yml");
    }
    TNE.instance.playerConfigurations = YamlConfiguration.loadConfiguration(TNE.instance.players);
  }

  private static void reloadConfigsWorlds() {
    if(TNE.instance.worlds == null) {
      TNE.instance.worlds = new File(TNE.instance.getDataFolder(), "worlds.yml");
    }
    TNE.instance.worldConfigurations = YamlConfiguration.loadConfiguration(TNE.instance.worlds);
  }
}