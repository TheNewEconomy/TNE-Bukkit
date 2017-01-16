package com.github.tnerevival;

import com.github.tnerevival.commands.CommandManager;
import com.github.tnerevival.commands.TNECommand;
import com.github.tnerevival.core.*;
import com.github.tnerevival.core.api.TNEAPI;
import com.github.tnerevival.core.configurations.ConfigurationManager;
import com.github.tnerevival.core.configurations.impl.ObjectConfiguration;
import com.github.tnerevival.core.inventory.InventoryManager;
import com.github.tnerevival.core.version.ReleaseType;
import com.github.tnerevival.listeners.*;
import com.github.tnerevival.utils.MISCUtils;
import com.github.tnerevival.worker.*;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

public class TNE extends JavaPlugin {

  public List<String> modified = new ArrayList<>();

  public static TNE instance;
  public EconomyManager manager;
  public InventoryManager inventoryManager;
  public SaveManager saveManager;
  private CommandManager commandManager;
  public TNEAPI api = null;

  public SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss.S");
  public static final Pattern uuidCreator = Pattern.compile("(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})");
  public static boolean debugMode = false;

  // Files & Custom Configuration Files
  public File mobs;
  public File messages;
  public File objects;
  public File materials;
  public File players;
  public File worlds;

  public FileConfiguration mobConfigurations;
  public FileConfiguration messageConfigurations;
  public FileConfiguration objectConfigurations;
  public FileConfiguration materialConfigurations;
  public FileConfiguration playerConfigurations;
  public FileConfiguration worldConfigurations;

  public static ConfigurationManager configurations;
  public static UpdateChecker updater;

  public String defaultWorld = "Default";

  /*
   * Instances of the main runnables.
   */
  private AuctionWorker auctionWorker;
  private SaveWorker saveWorker;
  private InterestWorker interestWorker;
  private StatisticsWorker statsWorker;
  private InventoryTimeWorker invWorker;
  private CacheWorker cacheWorker;

  public static Map<String, UUID> uuidCache = new HashMap<>();

  public void onLoad() {
    instance = this;
    api = new TNEAPI(this);
    setupVault();
  }

  public void onEnable() {
    defaultWorld = Bukkit.getServer().getWorlds().get(0).getName();
    updater = new UpdateChecker();

    //Configurations
    initializeConfigurations();
    loadConfigurations();

    debugMode = getConfig().getBoolean("Core.Debug");

    configurations = new ConfigurationManager();

    manager = new EconomyManager();
    inventoryManager = new InventoryManager();
    saveManager = new SaveManager();
    saveManager.initialize();
    commandManager = new CommandManager();

    invWorker = new InventoryTimeWorker(this);
    auctionWorker = new AuctionWorker(this);

    if(configurations.getBoolean("Core.AutoSaver.Enabled")) {
      saveWorker = new SaveWorker(this);
      saveWorker.runTaskTimer(this, configurations.getLong("Core.AutoSaver.Interval") * 20, configurations.getLong("Core.AutoSaver.Interval") * 20);
    }

    if(configurations.getBoolean("Core.Bank.Interest.Enabled")) {
      interestWorker = new InterestWorker(this);
      interestWorker.runTaskTimer(this, configurations.getLong("Core.Bank.Interest.Interval") * 20, configurations.getLong("Core.Bank.Interest.Interval") * 20);
    }

    if(!saveManager.type.equalsIgnoreCase("flatfile") && saveManager.cache) {
      cacheWorker = new CacheWorker(this);
      cacheWorker.runTaskTimer(this, saveManager.update * 20, saveManager.update * 20);
    }

    if((boolean) ObjectConfiguration.configurations.get("Objects.Inventories.Enabled")) {
      invWorker.runTaskTimer(this, 20, 20);
    }
    auctionWorker.runTaskTimer(this, 20, 20);

    //Listeners
    getServer().getPluginManager().registerEvents(new ConnectionListener(this), this);
    getServer().getPluginManager().registerEvents(new InteractionListener(this), this);
    getServer().getPluginManager().registerEvents(new InventoryListener(this), this);
    getServer().getPluginManager().registerEvents(new WorldListener(this), this);

    if(configurations.getBoolean("Core.Metrics")) {
      statsWorker = new StatisticsWorker(this);
      statsWorker.runTaskTimer(this, 24000, 24000);
      Statistics.send();
      try {
          MetricsLite metrics = new MetricsLite(this);
          metrics.start();
      } catch (IOException e) {
          getLogger().severe("Error while enabling plugin metrics.");
      }
    }

    getLogger().info("The New Economy " + updater.getCurrentBuild() + " has been enabled!");

    String updateMessage = "Using the latest version: " + updater.getCurrentBuild();
    if(updater.getRelease().equals(ReleaseType.PRERELEASE)) updateMessage = "Prerelease build, please report any bugs!";
    if(updater.getRelease().equals(ReleaseType.OUTDATED)) updateMessage = "Outdated! The current build is " + updater.getLatestBuild();
    getLogger().info(updateMessage);
  }

  public void onDisable() {
    configurations.save(getConfig(), "main");
    configurations.save(mobConfigurations, "mob");
    configurations.save(messageConfigurations, "messages");
    configurations.save(objectConfigurations, "objects");
    configurations.save(materialConfigurations, "materials");
    saveConfigurations(true);
    try {
      if(statsWorker != null) statsWorker.cancel();
      if(cacheWorker != null) cacheWorker.cancel();
      if(invWorker != null) invWorker.cancel();
      if(auctionWorker != null) auctionWorker.cancel();
      if(saveWorker != null) saveWorker.cancel();
      if(interestWorker != null) interestWorker.cancel();
    } catch(IllegalStateException e) {
      //Task was not scheduled
    }
    saveManager.save();
    getLogger().info("The New Economy " + updater.getCurrentBuild() + " has been disabled!");
  }

  @Override
  public boolean onCommand(CommandSender sender, Command command, String label, String[] arguments) {
    TNECommand ecoCommand = commandManager.Find(label);
    if(ecoCommand != null) {
      if(!ecoCommand.canExecute(sender)) {
        sender.sendMessage(ChatColor.RED + "I'm sorry, but you're not allowed to use that command.");
        return false;
      }
      return ecoCommand.execute(sender, label, arguments);
    }
    return false;
  }

  private void initializeConfigurations() {
    mobs = new File(getDataFolder(), "mobs.yml");
    messages = new File(getDataFolder(), "messages.yml");
    objects = new File(getDataFolder(), "objects.yml");
    materials = new File(getDataFolder(), "materials.yml");
    players = new File(getDataFolder(), "players.yml");
    worlds = new File(getDataFolder(), "worlds.yml");
    mobConfigurations = YamlConfiguration.loadConfiguration(mobs);
    messageConfigurations = YamlConfiguration.loadConfiguration(messages);
    objectConfigurations = YamlConfiguration.loadConfiguration(objects);
    materialConfigurations = YamlConfiguration.loadConfiguration(materials);
    playerConfigurations = YamlConfiguration.loadConfiguration(players);
    worldConfigurations = YamlConfiguration.loadConfiguration(worlds);
    try {
      setConfigurationDefaults();
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
  }

  public void loadConfigurations() {
    getConfig().options().copyDefaults(true);
    mobConfigurations.options().copyDefaults(true);
    messageConfigurations.options().copyDefaults(true);
    objectConfigurations.options().copyDefaults(true);
    materialConfigurations.options().copyDefaults(true);
    playerConfigurations.options().copyDefaults(true);
    worldConfigurations.options().copyDefaults(true);
    saveConfigurations(false);
  }

  private void saveConfigurations(boolean check) {
    if(!check || !new File(getDataFolder(), "config.yml").exists() || modified.contains("config.yml")) {
      saveConfig();
    }
    try {
      if(!check || !mobs.exists() || modified.contains(mobConfigurations.getName())) {
        mobConfigurations.save(mobs);
      }
      if(!check || !messages.exists() || modified.contains(messageConfigurations.getName())) {
        messageConfigurations.save(messages);
      }
      if(!check || !objects.exists() || modified.contains(objectConfigurations.getName())) {
        objectConfigurations.save(objects);
      }
      if(!check || !materials.exists() || modified.contains(materialConfigurations.getName())) {
        materialConfigurations.save(materials);
      }
      if(!check || !players.exists() || modified.contains(playerConfigurations.getName())) {
        playerConfigurations.save(players);
      }
      if(!check || !worlds.exists() || modified.contains(worldConfigurations.getName())) {
        worldConfigurations.save(worlds);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void setConfigurationDefaults() throws UnsupportedEncodingException {
    Reader mobsStream = new InputStreamReader(this.getResource("mobs.yml"), "UTF8");
    Reader messagesStream = new InputStreamReader(this.getResource("messages.yml"), "UTF8");
    Reader objectsStream = new InputStreamReader(this.getResource("objects.yml"), "UTF8");
    Reader materialsStream = new InputStreamReader(this.getResource("materials.yml"), "UTF8");
    Reader playersStream = new InputStreamReader(this.getResource("players.yml"), "UTF8");
    Reader worldsStream = new InputStreamReader(this.getResource("worlds.yml"), "UTF8");
      if (mobsStream != null) {
          YamlConfiguration config = YamlConfiguration.loadConfiguration(mobsStream);
          mobConfigurations.setDefaults(config);
      }

      if (messagesStream != null) {
          YamlConfiguration config = YamlConfiguration.loadConfiguration(messagesStream);
          messageConfigurations.setDefaults(config);
      }

      if (materialsStream != null) {
          YamlConfiguration config = YamlConfiguration.loadConfiguration(materialsStream);
          materialConfigurations.setDefaults(config);
      }

      if (objectsStream != null) {
          YamlConfiguration config = YamlConfiguration.loadConfiguration(objectsStream);
          objectConfigurations.setDefaults(config);
      }

      if (playersStream != null) {
        YamlConfiguration config = YamlConfiguration.loadConfiguration(playersStream);
        playerConfigurations.setDefaults(config);
      }

      if (worldsStream != null) {
          YamlConfiguration config = YamlConfiguration.loadConfiguration(worldsStream);
          worldConfigurations.setDefaults(config);
      }
  }

  public void setupVault() {
    if(getServer().getPluginManager().getPlugin("Vault") == null) {
      return;
    }
        getServer().getServicesManager().register(Economy.class, new TNEVaultEconomy(this), this, ServicePriority.Highest);
        MISCUtils.debug("Hooked into Vault");
  }
}