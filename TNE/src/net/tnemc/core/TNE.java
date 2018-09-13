package net.tnemc.core;

import com.github.tnerevival.Metrics;
import com.github.tnerevival.TNELib;
import com.github.tnerevival.core.UpdateChecker;
import com.github.tnerevival.core.collection.EventList;
import com.github.tnerevival.core.collection.EventMap;
import net.milkbowl.vault.economy.Economy;
import net.tnemc.core.commands.CommandManager;
import net.tnemc.core.commands.TNECommand;
import net.tnemc.core.commands.admin.AdminCommand;
import net.tnemc.core.commands.config.ConfigCommand;
import net.tnemc.core.commands.currency.CurrencyCommand;
import net.tnemc.core.commands.dev.DeveloperCommand;
import net.tnemc.core.commands.language.LanguageCommand;
import net.tnemc.core.commands.module.ModuleCommand;
import net.tnemc.core.commands.money.MoneyCommand;
import net.tnemc.core.commands.transaction.TransactionCommand;
import net.tnemc.core.commands.yeti.YetiCommand;
import net.tnemc.core.common.EconomyManager;
import net.tnemc.core.common.TNEUUIDManager;
import net.tnemc.core.common.TransactionManager;
import net.tnemc.core.common.WorldManager;
import net.tnemc.core.common.account.TNEAccount;
import net.tnemc.core.common.api.EconomyPlaceholders;
import net.tnemc.core.common.api.Economy_TheNewEconomy;
import net.tnemc.core.common.api.IDFinder;
import net.tnemc.core.common.api.ReserveEconomy;
import net.tnemc.core.common.api.TNEAPI;
import net.tnemc.core.common.configurations.MainConfigurations;
import net.tnemc.core.common.configurations.MessageConfigurations;
import net.tnemc.core.common.configurations.WorldConfigurations;
import net.tnemc.core.common.data.TNEDataManager;
import net.tnemc.core.common.data.TNESaveManager;
import net.tnemc.core.common.module.ModuleLoader;
import net.tnemc.core.common.utils.MISCUtils;
import net.tnemc.core.common.utils.MaterialUtils;
import net.tnemc.core.event.module.TNEModuleLoadEvent;
import net.tnemc.core.event.module.TNEModuleUnloadEvent;
import net.tnemc.core.listeners.ConnectionListener;
import net.tnemc.core.listeners.ExperienceCancelListener;
import net.tnemc.core.listeners.ExperienceListener;
import net.tnemc.core.listeners.MCMMOListener;
import net.tnemc.core.listeners.PlayerListener;
import net.tnemc.core.listeners.TNEMessageListener;
import net.tnemc.core.menu.MenuManager;
import net.tnemc.core.worker.SaveWorker;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.plugin.ServicePriority;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */
public class TNE extends TNELib {
  private Map<String, WorldManager> worldManagers = new HashMap<>();
  private List<UUID> tnemodUsers = new ArrayList<>();
  public final List<String> developers = Collections.singletonList("5bb0dcb3-98ee-47b3-8f66-3eb1cdd1a881");

  private EconomyManager manager;
  private MenuManager menuManager;
  private static net.tnemc.core.common.configurations.ConfigurationManager configurations;
  protected CommandManager commandManager;

  private ModuleLoader loader;
  public UpdateChecker updater;
  public static boolean consoleDebug = false;
  private String serverName;

  //Economy APIs
  private Economy_TheNewEconomy vaultEconomy;
  private ReserveEconomy reserveEconomy;
  private TNEAPI api;

  // Files & Custom Configuration Files
  private File items;
  private File messagesFile;
  private File players;
  private File worlds;

  private FileConfiguration itemConfigurations;
  private FileConfiguration messageConfigurations;
  private FileConfiguration playerConfigurations;
  private FileConfiguration worldConfigurations;

  private MainConfigurations main;
  private MessageConfigurations messages;
  private WorldConfigurations world;

  //BukkitRunnable Workers
  private SaveWorker saveWorker;

  public static final String build = "7Beta113";

  //Cache-related collections
  private List<EventList> cacheLists = new ArrayList<>();
  private List<EventMap> cacheMaps = new ArrayList<>();

  private boolean blacklisted = false;
  public static boolean useMod = false;

  public void onLoad() {
    if(MISCUtils.serverBlacklist().contains(getServer().getIp())) {
      blacklisted = true;
      getLogger().info("Unable to load The New Economy as this server has been blacklisted!");
      return;
    }

    if(getServer().getPluginManager().getPlugin("GUIShop") != null) {
      getLogger().info("Unable to load The New Economy as it is incompatible with GUIShop.");
      blacklisted = true;
      return;
    }

    getLogger().info("Loading The New Economy with Java Version: " + System.getProperty("java.version"));
    instance = this;
    api = new TNEAPI(this);

    //Initialize Economy Classes
    if(getServer().getPluginManager().getPlugin("Vault") != null) {
      vaultEconomy = new Economy_TheNewEconomy(this);
      setupVault();
    }

    reserveEconomy = new ReserveEconomy(this);
    if(getServer().getPluginManager().getPlugin("Reserve") != null) {
      setupReserve();
    }
  }

  public void onEnable() {
    if(blacklisted) {
      return;
    }
    super.onEnable();

    configurations = new net.tnemc.core.common.configurations.ConfigurationManager();
    commandManager = new CommandManager();

    //Create Debug Log
    /*try {
      LocalDateTime now = LocalDateTime.now();
      int year = now.getYear();
      int month = now.getMonthValue();
      int day = now.getDayOfMonth();
      new File(getDataFolder(), "debug/").mkdir();
      new File(getDataFolder(), "debug/debug-" + year + "-" + month + "-" + day + ".txt").createNewFile();
    } catch (IOException e) {
      e.printStackTrace();
    }*/

    currentSaveVersion = 11.0;

    setUuidManager(new TNEUUIDManager());

    updater = new UpdateChecker("https://creatorfromhell.com/tne/tnebuild.txt", getDescription().getVersion());

    //Run the ModuleLoader
    loader = new ModuleLoader();
    loader.load();

    if(!loader.hasModule("MySQL") && !loader.hasModule("H2")) {
      new File(getDataFolder(), "modules").mkdir();
      ModuleLoader.downloadModule("h2");
      loader.load("H2");
    }

    //Load modules
    loader.getModules().forEach((key, value)->{
      TNEModuleLoadEvent event = new TNEModuleLoadEvent(key, value.getInfo().version());
      Bukkit.getServer().getPluginManager().callEvent(event);
      if(!event.isCancelled()) {
        value.getModule().load(this, loader.getLastVersion(value.getInfo().name()));
      }
    });

    getServer().getWorlds().forEach(world->{
      worldManagers.put(world.getName(), new WorldManager(world.getName()));
    });

    //Configurations
    initializeConfigurations();
    loadConfigurations();
    main = new MainConfigurations();
    messages = new MessageConfigurations();
    world = new WorldConfigurations();
    loader.getModules().forEach((key, value)->{
      value.getModule().getMainConfigurations().forEach((node, defaultValue)->{
        main.configurations.put(node, defaultValue);
      });
    });
    loader.getModules().forEach((key, value)->{
      value.getModule().getMessages().forEach((message, defaultValue)->{
        messages.configurations.put(message, defaultValue);
      });
    });
    loader.getModules().forEach((key, value)->{
      value.getModule().getConfigurations().forEach((configuration, identifier)->{
        configurations().add(configuration, identifier);
      });
    });
    configurations().add(main, "main");
    configurations().add(messages, "messages");
    configurations().add(world, "world");
    configurations().loadAll();

    List<String> moneyArguments = Arrays.asList("money", "givemoney", "givebal", "setbal", "setmoney", "takemoney", "takebal");
    if(configurations().getBoolean("Core.Commands.PayShort")) {
      moneyArguments.add("pay");
    }

    if(configurations().getBoolean("Core.Commands.BalanceShort")) {
      moneyArguments.add("bal");
      moneyArguments.add("balance");
    }

    if(configurations().getBoolean("Core.Commands.TopShort")) {
      moneyArguments.add("baltop");
    }

    //Commands
    registerCommand(new String[] { "language", "lang" }, new LanguageCommand(this));
    registerCommand(new String[] { "tne", "theneweconomy", "eco" }, new AdminCommand(this));
    registerCommand(new String[] { "tnedev", "theneweconomydev" }, new DeveloperCommand(this));
    registerCommand(new String[] { "tneconfig", "tnec" }, new ConfigCommand(this));
    registerCommand(new String[] { "currency", "cur" }, new CurrencyCommand(this));
    registerCommand(new String[] { "tnemodule", "tnem" }, new ModuleCommand(this));
    registerCommand(moneyArguments.toArray(new String[moneyArguments.size()]), new MoneyCommand(this));
    registerCommand(new String[] { "transaction", "trans" }, new TransactionCommand(this));
    registerCommand(new String[] { "yediot" }, new YetiCommand(this));
    loader.getModules().forEach((key, value)->{
      value.getModule().getCommands().forEach((command)->{
        List<String> accessors = new ArrayList<>();
        for(String string : command.getAliases()) {
          accessors.add(string);
        }
        accessors.add(command.getName());
        TNE.debug("Command Manager Null?: " + (commandManager == null));
        TNE.debug("Accessors?: " + accessors.size());
        TNE.debug("Command Null?: " + (command == null));
        registerCommand(accessors.toArray(new String[accessors.size()]), command);
      });
    });

    //Initialize our plugin's managers.
    manager = new EconomyManager();
    menuManager = new MenuManager();

    //General Variables based on configuration values
    serverName = (configurations().getString("Core.Server.Name").length() <= 100)? configurations().getString("Core.Server.Name") : "Main Server";
    consoleName = (configurations().getString("Core.Server.Account.Name").length() <= 100)? configurations().getString("Core.Server.Account.Name") : "Server_Account";
    useUUID = configurations().getBoolean("Core.UUID");


    if(!loader.hasModuleWithoutCase(configurations().getString("Core.Database.Type"))) {
      getLogger().log(Level.SEVERE, "Unable to locate module with specified database type.");
    }

    TNESaveManager sManager = new TNESaveManager(new TNEDataManager(
        configurations().getString("Core.Database.Type").toLowerCase(),
        configurations().getString("Core.Database.MySQL.Host"),
        configurations().getInt("Core.Database.MySQL.Port"),
        configurations().getString("Core.Database.MySQL.Database"),
        configurations().getString("Core.Database.MySQL.User"),
        configurations().getString("Core.Database.MySQL.Password"),
        configurations().getString("Core.Database.Prefix"),
        new File(getDataFolder(), configurations().getString("Core.Database.File")).getAbsolutePath(),
        true,
        false,
        600,
        true
    ));
    setSaveManager(sManager);

    saveManager().getTNEManager().loadProviders();
    TNE.debug("Finished loading providers");

    TNE.debug("Setting format: " + configurations().getString("Core.Database.Type").toLowerCase());

    TNE.debug("Adding version files.");
    saveManager().addVersion(10.0, true);

    TNE.debug("Initializing Save Manager.");
    saveManager().initialize();

    loader.getModules().forEach((key, value)->{
      value.getModule().getTables().forEach((type, tables)->saveManager().registerTables(type, tables));
    });

    if(saveManager().getTables(configurations().getString("Core.Database.Type").toLowerCase()).size() > 0) {
      saveManager().getTNEManager().getTNEProvider().createTables(saveManager().getTables(configurations().getString("Core.Database.Type").toLowerCase()));
    }

    TNE.debug("Calling Modules.enableSave");
    loader.getModules().forEach((key, value)->{
      value.getModule().enableSave(saveManager());
    });

    TNE.debug("Loading data.");
    saveManager().load();

    //Bukkit Runnables & Workers
    if(configurations().getBoolean("Core.AutoSaver.Enabled")) {
      saveWorker = new SaveWorker(this);
      saveWorker.runTaskTimer(this, configurations().getLong("Core.AutoSaver.Interval") * 20, configurations().getLong("Core.AutoSaver.Interval") * 20);
    }

    if(Bukkit.getPluginManager().getPlugin("mcMMO") != null && api().getBoolean("Core.Server.McMMORewards")) {
      getServer().getPluginManager().registerEvents(new MCMMOListener(this), this);
    }

    getServer().getPluginManager().registerEvents(new ConnectionListener(this), this);
    getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
    if(configurations().getBoolean("Core.Server.ExperienceGain")) {
      getServer().getPluginManager().registerEvents(new ExperienceCancelListener(this), this);
    } else {
      getServer().getPluginManager().registerEvents(new ExperienceListener(this), this);
    }
    loader.getModules().forEach((key, value)->{
      value.getModule().getListeners(this).forEach(listener->{
        getServer().getPluginManager().registerEvents(listener, this);
        TNE.debug("Registering Listener");
      });
    });

    loader.getModules().forEach((key, value)->
        value.getModule().postLoad(this)
    );
    loader.getModules().forEach((key, value)->
        value.getModule().registerMenus(this).forEach((name, menu)->{
          menuManager.menus.put(name, menu);
        })
    );

    if(Bukkit.getServer().getPluginManager().isPluginEnabled("PlaceholderAPI")) {
      new EconomyPlaceholders().register();
    }

    //Metrics
    if(api.getBoolean("Core.Server.ThirdParty.Stats")) {
      new Metrics(this);
      getLogger().info("Sending plugin statistics.");
    }

    if(api.getBoolean("Core.Server.Account.Enabled")) {
      String world = worldManagers.get(defaultWorld).getBalanceWorld();
      UUID id = IDFinder.getID(consoleName);

      if(!manager.exists(id)) {
        special.add(id);
        manager.createAccount(id, consoleName);
        TNEAccount account = manager.getAccount(id);
        TNE.debug("Account Null? " + (account == null));
        TNE.debug("Balance Config Null? " + (api.getBigDecimal("Core.Server.Account.Balance") == null));
        account.setHoldings(world, manager.currencyManager().get(world).name(), api.getBigDecimal("Core.Server.Account.Balance"), true);
        getLogger().info("Created server economy account.");
      }
    }

    useMod = configurations.getBoolean("Core.Server.TNEMod");

    if(useMod) {
      Bukkit.getMessenger().registerOutgoingPluginChannel(this, "tnemod");
      Bukkit.getMessenger().registerIncomingPluginChannel(this, "tnemod", new TNEMessageListener());
    }

    /*try {
      writeMobs();
      //writeItems();
    } catch (IOException e) {
      e.printStackTrace();
    }*/
    getLogger().info("The New Economy has been enabled!");
  }

  public void onDisable() {
    saveManager().save();
    loader.getModules().forEach((key, value)->{
      value.getModule().disableSave(saveManager());
    });
    loader.getModules().forEach((key, value)->{
      TNEModuleUnloadEvent event = new TNEModuleUnloadEvent(key, value.getInfo().version());
      Bukkit.getServer().getPluginManager().callEvent(event);
      value.getModule().unload(this);
    });
    super.onDisable();
    getLogger().info("The New Economy has been disabled!");
  }

  private void writeMobs() throws IOException {
    BufferedWriter writer = new BufferedWriter(new FileWriter(new File(getDataFolder(), "mobs.txt")));
    final String newLine = System.getProperty("line.separator");
    writer.write("Mobs:" + newLine);
    TreeMap<String, EntityType> entities = new TreeMap<>();

    for (EntityType entityType : EntityType.values()) {
      entities.put(entityType.name(), entityType);
    }
    for(EntityType type : entities.values()) {
      writer.write(newLine);
      writer.write("  " + type.name().toUpperCase() + ":" + newLine);
      writer.write("    #Whether or not this entity drops money on death." + newLine);
      writer.write("    Enabled: true" + newLine + newLine);
      writer.write("    #The currency to use for the money dropped by this entity" + newLine);
      writer.write("    RewardCurrency: Default" + newLine + newLine);
      writer.write("    #The amount of money this mob should drop." + newLine);
      writer.write("    #Negative will take money from player" + newLine);
      writer.write("    Reward: 10.00" + newLine + newLine);
      writer.write("    #Configurations relating to baby versions of this entity." + newLine);
      writer.write("    Baby:" + newLine);
      writer.write("      #Whether or not to enable separate configurations for baby versions of this entity." + newLine);
      writer.write("      Enabled: true" + newLine + newLine);
      writer.write("      #The currency to use for the money dropped by this entity" + newLine);
      writer.write("      RewardCurrency: Default" + newLine + newLine);
      writer.write("      #The amount of money this mob should drop." + newLine);
      writer.write("      #Negative will take money from player" + newLine);
      writer.write("      Reward: 10.00" + newLine);
    }
  }

  private void writeItems() throws IOException {
    BufferedWriter writer = new BufferedWriter(new FileWriter(new File(getDataFolder(), "items.txt")));
    final String newLine = System.getProperty("line.separator");
    writer.write("Items:" + newLine);
    for(Material material : Material.values()) {
      writer.write(newLine);
      writer.write("  " + material.name().toUpperCase() + ":" + newLine);
      writer.write("    #The permission node required to create a shop sign with this item" + newLine);
      writer.write("    Sign: tne.item." + material.name().toLowerCase() + ".sign" + newLine + newLine);
      writer.write("    #The permission node required to buy this item" + newLine);
      writer.write("    Buy: tne.item." + material.name().toLowerCase() + ".buy" + newLine + newLine);
      writer.write("    #The permission node required to sell this item" + newLine);
      writer.write("    Sell: tne.item." + material.name().toLowerCase() + ".sell" + newLine + newLine);
      writer.write("    #The names supported by shop signs" + newLine);
      writer.write("    Names:" + newLine);
      writer.write("      - " + MaterialUtils.formatMaterialName(material) + newLine);
      writer.write("      - " + MaterialUtils.formatMaterialNameWithSpace(material) + newLine);
    }
  }

  public static TNE instance() {
    return (TNE)instance;
  }

  public TNEAPI api() {
    return api;
  }

  public CommandManager getCommandManager() {
    return commandManager;
  }

  public void registerCommand(String[] accessors, TNECommand command) {
    commandManager.commands.put(accessors, command);
    commandManager.registerCommands();
  }

  public void registerCommands(Map<String[], TNECommand> commands) {
    commandManager.commands = commands;
    commandManager.registerCommands();
  }

  public void unregisterCommand(String[] accessors) {
    commandManager.unregister(accessors);
  }

  @Override
  public boolean onCommand(CommandSender sender, Command command, String label, String[] arguments) {
    List<String> triggers = new ArrayList<>(Arrays.asList(TNE.configurations().getString( "Core.Commands.Triggers", "main", "", "").split(",")));

    if(sender instanceof Player && !triggers.contains("/")) return false;
    return customCommand(sender, label, arguments);
  }

  public boolean customCommand(CommandSender sender, String label, String[] arguments) {

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

  public void addModUser(UUID id) {
    tnemodUsers.add(id);
  }

  public boolean isModUser(UUID id) {
    return tnemodUsers.contains(id);
  }

  public void removeModUser(UUID id) {
    tnemodUsers.remove(id);
  }

  public static net.tnemc.core.common.configurations.ConfigurationManager configurations() {
    return configurations;
  }

  public void registerEventList(EventList list) {
    cacheLists.add(list);
  }

  public void registerEventMap(EventMap map) {
    cacheMaps.add(map);
  }

  public Economy_TheNewEconomy vault() {
    return vaultEconomy;
  }

  public ReserveEconomy reserve() {
    return reserveEconomy;
  }

  public static ModuleLoader loader() { return instance().loader; }

  public static EconomyManager manager() {
    return instance().manager;
  }

  public static MenuManager menuManager() {
    return instance().menuManager;
  }

  public static TransactionManager transactionManager() {
    return instance().manager.transactionManager();
  }

  public static TNESaveManager saveManager() {
    return (TNESaveManager)instance().getSaveManager();
  }

  public static Logger logger() {
    return instance().getServer().getLogger();
  }

  public static TNEUUIDManager uuidManager() {
    return (TNEUUIDManager)instance().getUuidManager();
  }

  public String getServerName() {
    return serverName;
  }

  public void setUUIDS(Map<String, UUID> ids) {
    uuidCache.putAll(ids);
  }

  public MainConfigurations main() {
    return main;
  }

  public MessageConfigurations messages() {
    return messages;
  }

  public FileConfiguration messageConfiguration() {
    return messageConfigurations;
  }

  public FileConfiguration itemConfiguration() {
    return itemConfigurations;
  }

  public FileConfiguration playerConfiguration() {
    return playerConfigurations;
  }

  public FileConfiguration worldConfiguration() {
    return worldConfigurations;
  }

  private void initializeConfigurations() {
    items = new File(getDataFolder(), "items.yml");
    messagesFile = new File(getDataFolder(), "messages.yml");
    players = new File(getDataFolder(), "players.yml");
    worlds = new File(getDataFolder(), "worlds.yml");
    itemConfigurations = YamlConfiguration.loadConfiguration(items);
    messageConfigurations = YamlConfiguration.loadConfiguration(messagesFile);
    playerConfigurations = YamlConfiguration.loadConfiguration(players);
    worldConfigurations = YamlConfiguration.loadConfiguration(worlds);
    loader.getModules().forEach((key, value)->{
      value.getModule().initializeConfigurations();
    });
    try {
      setConfigurationDefaults();
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
  }

  public static void debug(StackTraceElement[] stack) {
    for(StackTraceElement element : stack) {
      logger().warning(element.toString());
    }
  }

  public static void debug(String message) {
    /*LocalDateTime now = LocalDateTime.now();
    int year = now.getYear();
    int month = now.getMonthValue();
    int day = now.getDayOfMonth();
    int hour = now.getHour();
    int minute = now.getMinute();
    int second = now.getSecond();
    int mil = now.get(ChronoField.MILLI_OF_SECOND);
    String time = "[" + year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + second + "." + mil + "] ";
    if(consoleDebug) {
      TNE.debug(message);
    } else {
      try {
        BufferedWriter writer = new BufferedWriter(new FileWriter(new File(TNE.instance().getDataFolder(), "debug/debug-" + year + "-" + month + "-" + day + ".txt"), true));
        writer.write(time + message + System.getProperty("line.separator"));

        writer.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }*/
    //System.out.println(message);
  }

  public void loadConfigurations() {
    loader.getModules().forEach((key, value)->{
      value.getModule().loadConfigurations();
    });
    this.saveDefaultConfig();
    if(!new File(getDataFolder(), "config.yml").exists()) {
      getConfig().options().copyDefaults(true);
    }
    itemConfigurations.options().copyDefaults(true);
    messageConfigurations.options().copyDefaults(true);
    playerConfigurations.options().copyDefaults(true);
    worldConfigurations.options().copyDefaults(true);
    saveConfigurations(false);
  }

  private void saveConfigurations(boolean check) {
    if(!check || !new File(getDataFolder(), "config.yml").exists() || configurations().changed.contains("config.yml")) {
      saveConfig();
    }
    try {
      loader.getModules().forEach((key, value)->{
        value.getModule().saveConfigurations();
      });
      if(!check || !items.exists() || configurations().changed.contains(itemConfigurations.getName())) {
        itemConfigurations.save(items);
      }
      if(!check || !messagesFile.exists() || configurations().changed.contains(messageConfigurations.getName())) {
        messageConfigurations.save(messagesFile);
      }
      if(!check || !players.exists() || configurations().changed.contains(playerConfigurations.getName())) {
        playerConfigurations.save(players);
      }
      if(!check || !worlds.exists() || configurations().changed.contains(worldConfigurations.getName())) {
        worldConfigurations.save(worlds);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void setConfigurationDefaults() throws UnsupportedEncodingException {
    Reader itemsStream = new InputStreamReader(this.getResource("items.yml"), "UTF8");
    Reader messagesStream = new InputStreamReader(this.getResource("messages.yml"), "UTF8");
    Reader playersStream = new InputStreamReader(this.getResource("players.yml"), "UTF8");
    Reader worldsStream = new InputStreamReader(this.getResource("worlds.yml"), "UTF8");
    if (itemsStream != null && !items.exists()) {
      YamlConfiguration config = YamlConfiguration.loadConfiguration(itemsStream);
      itemConfigurations.setDefaults(config);
    }

    if (messagesStream != null && !messagesFile.exists()) {
      YamlConfiguration config = YamlConfiguration.loadConfiguration(messagesStream);
      messageConfigurations.setDefaults(config);
    }

    if (playersStream != null && !players.exists()) {
      YamlConfiguration config = YamlConfiguration.loadConfiguration(playersStream);
      playerConfigurations.setDefaults(config);
    }

    if (worldsStream != null && !worlds.exists()) {
      YamlConfiguration config = YamlConfiguration.loadConfiguration(worldsStream);
      worldConfigurations.setDefaults(config);
    }
  }

  private void setupVault() {
    getServer().getServicesManager().register(Economy.class, vaultEconomy, this, ServicePriority.Highest);
    getLogger().info("Hooked into Vault");
  }

  private void setupReserve() {
    Reserve.instance().registerProvider(reserveEconomy);
    getLogger().info("Hooked into Reserve");
  }

  public void addWorldManager(WorldManager manager) {
    TNE.debug("Adding World Manager for world " + manager.getWorld());
    TNE.debug("Configuration World: " + manager.getConfigurationWorld());
    TNE.debug("Balance World: " + manager.getBalanceWorld());
    worldManagers.put(manager.getWorld(), manager);
  }

  public WorldManager getWorldManager(String world) {
    for(WorldManager manager : this.worldManagers.values()) {
      if(manager.getWorld().equalsIgnoreCase(world)) {
        debug("Return World Manager for world " + world);
        return manager;
      }
    }
    return null;
  }

  public Collection<WorldManager> getWorldManagers() {
    return worldManagers.values();
  }

  public Map<String, WorldManager> getWorldManagersMap() {
    return worldManagers;
  }
}