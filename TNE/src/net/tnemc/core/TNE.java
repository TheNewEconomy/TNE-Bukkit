package net.tnemc.core;

import com.github.tnerevival.Metrics;
import com.github.tnerevival.TNELib;
import com.github.tnerevival.core.UpdateChecker;
import net.milkbowl.vault.economy.Economy;
import net.tnemc.config.CommentedConfiguration;
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
import net.tnemc.core.common.material.MaterialHelper;
import net.tnemc.core.common.module.ModuleLoader;
import net.tnemc.core.common.utils.MISCUtils;
import net.tnemc.core.common.utils.MaterialUtils;
import net.tnemc.core.compatibility.ItemCompatibility;
import net.tnemc.core.compatibility.item.ItemCompatibility12;
import net.tnemc.core.compatibility.item.ItemCompatibility13;
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
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.plugin.ServicePriority;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
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

  private List<String> dupers;

  private EconomyManager manager;
  private MenuManager menuManager;
  private static net.tnemc.core.common.configurations.ConfigurationManager configurations;
  protected CommandManager commandManager;

  private ModuleLoader loader;
  public UpdateChecker updater;
  public static boolean consoleDebug = false;
  public static boolean maintenance = false;
  private String serverName;

  //Compatibility Classes.
  private ItemCompatibility itemCompatibility;

  //Economy APIs
  private Economy_TheNewEconomy vaultEconomy;
  private ReserveEconomy reserveEconomy;
  private TNEAPI api;

  // Files & Custom Configuration Files
  private File mainConfig;
  private File currencies;
  private File items;
  private File messagesFile;
  private File players;
  private File worlds;

  private CommentedConfiguration mainConfigurations;
  private CommentedConfiguration currencyConfigurations;
  private CommentedConfiguration itemConfigurations;
  private CommentedConfiguration messageConfigurations;
  private CommentedConfiguration playerConfigurations;
  private CommentedConfiguration worldConfigurations;

  private MainConfigurations main;
  private MessageConfigurations messages;
  private WorldConfigurations world;

  //BukkitRunnable Workers
  private SaveWorker saveWorker;

  public static final String build = "11Beta118";

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

    dupers = MISCUtils.dupers();
    super.onEnable();

    if(!getDataFolder().exists()) getDataFolder().mkdirs();

    configurations = new net.tnemc.core.common.configurations.ConfigurationManager();
    commandManager = new CommandManager();

    currentSaveVersion = 1115.0;

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

    TNE.debug("Preparing configuration instances");
    main = new MainConfigurations();
    messages = new MessageConfigurations();
    world = new WorldConfigurations();
    world.load(worldConfigurations);

    TNE.debug("Preparing debug mode");
    this.debugMode = mainConfigurations.getBool("Core.Debug");

    TNE.debug("Preparing module configurations for manager");
    loader.getModules().forEach((key, value)->{
      value.getModule().getConfigurations().forEach((configuration, identifier)->{
        configurations().add(configuration, identifier);
      });
    });
    TNE.debug("Preparing configurations for manager");
    configurations().add(main, "main");
    configurations().add(messages, "messages");
    configurations().add(world, "world");

    TNE.debug("Preparing commands");
    List<String> moneyArguments = new ArrayList<>(Arrays.asList("money", "givemoney", "givebal", "setbal", "setmoney", "takemoney", "takebal"));
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
    TNE.debug("Preparing commands2");
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
    TNE.debug("Preparing managers");
    manager = new EconomyManager();

    //General Variables based on configuration values
    TNE.debug("Preparing variables");
    serverName = (configurations().getString("Core.Server.Name").length() <= 100)? configurations().getString("Core.Server.Name") : "Main Server";
    consoleName = (configurations().getString("Core.Server.Account.Name").length() <= 100)? configurations().getString("Core.Server.Account.Name") : "Server_Account";
    useUUID = configurations().getBoolean("Core.UUID");


    if(!loader.hasModuleWithoutCase(configurations().getString("Core.Database.Type"))) {
      getLogger().log(Level.SEVERE, "Unable to locate module with specified database type.");
    }

    TNE.debug("Preparing save manager");
    TNESaveManager sManager = new TNESaveManager(new TNEDataManager(
        configurations().getString("Core.Database.Type").toLowerCase(),
        configurations().getString("Core.Database.MySQL.Host"),
        configurations().getInt("Core.Database.MySQL.Port"),
        configurations().getString("Core.Database.MySQL.DB"),
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
    saveManager().addVersion(1115.0, true);

    TNE.debug("Initializing Save Manager.");
    try {
      saveManager().initialize();
    } catch (SQLException e) {
      e.printStackTrace();
    }

    TNE.debug("Preparing modules");
    loader.getModules().forEach((key, value)->{
      value.getModule().getTables().forEach((type, tables)->{
        saveManager().registerTables(type, tables);

        if(value.getModule().getTables().containsKey(configurations().getString("Core.Database.Type").toLowerCase())) {
          try {
            TNE.saveManager().getTNEManager().getTNEProvider().createTables(value.getModule().getTables().get(configurations().getString("Core.Database.Type").toLowerCase()));
          } catch (SQLException e) {
            TNE.debug("Failed to create tables on module load.");
          }
        }
      });
    });

    if(saveManager().getTables(configurations().getString("Core.Database.Type").toLowerCase()).size() > 0) {
      try {
        saveManager().getTNEManager().getTNEProvider().createTables(saveManager().getTables(configurations().getString("Core.Database.Type").toLowerCase()));
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }

    TNE.debug("Calling Modules.enableSave");
    loader.getModules().forEach((key, value)->{
      value.getModule().enableSave(saveManager());
    });

    TNE.debug("Loading data.");
    try {
      saveManager().load();
    } catch (SQLException e) {
      e.printStackTrace();
    }

    //Bukkit Runnables & Workers
    TNE.debug("Preparing autosavers");
    if(configurations().getBoolean("Core.AutoSaver.Enabled")) {
      saveWorker = new SaveWorker(this);
      saveWorker.runTaskTimer(this, configurations().getLong("Core.AutoSaver.Interval") * 20, configurations().getLong("Core.AutoSaver.Interval") * 20);
    }

    if(Bukkit.getPluginManager().getPlugin("mcMMO") != null && api().getBoolean("Core.Server.ThirdParty.McMMORewards")) {
      getServer().getPluginManager().registerEvents(new MCMMOListener(this), this);
    }

    TNE.debug("Preparing events");
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

    TNE.debug("Preparing postLoad");
    loader.getModules().forEach((key, value)->
        value.getModule().postLoad(this)
    );

    TNE.debug("Preparing placeholders");
    if(Bukkit.getServer().getPluginManager().isPluginEnabled("PlaceholderAPI")) {
      new EconomyPlaceholders().register();
    }

    //Metrics
    TNE.debug("Preparing metrics");
    if(api.getBoolean("Core.Server.ThirdParty.Stats")) {
      new Metrics(this);
      getLogger().info("Sending plugin statistics.");
    }

    TNE.debug("Preparing server account");
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

    TNE.debug("Preparing TNE Forge Mod support");
    useMod = configurations.getBoolean("Core.Server.TNEMod");

    if(useMod) {
      Bukkit.getMessenger().registerOutgoingPluginChannel(this, "tnemod");
      Bukkit.getMessenger().registerIncomingPluginChannel(this, "tnemod", new TNEMessageListener());
    }

    getLogger().info("The New Economy has been enabled!");

    /*SQLDebug.testLoad(500);
    SQLDebug.loadAccountTest(500);
    SQLDebug.loadSaveAccountTest(500);*/
  }

  public void onDisable() {

    for(Player player : Bukkit.getServer().getOnlinePlayers()) {
      try {
        TNE.saveManager().getTNEManager().getTNEProvider().saveAccount(TNE.manager().getAccount(player.getUniqueId()));
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }

    loader.getModules().forEach((key, value)->{
      value.getModule().disableSave(saveManager());
    });
    loader.getModules().forEach((key, value)->{
      TNEModuleUnloadEvent event = new TNEModuleUnloadEvent(key, value.getInfo().version());
      Bukkit.getServer().getPluginManager().callEvent(event);
      value.getModule().unload(this);
    });
    getLogger().info("The New Economy has been disabled!");
    super.onDisable();
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

  public static boolean isDuper(String requested) {
    TNE.debug("=========== Start[TNE.isDuper] ==============");
    TNE.debug("Requested: " + requested);
    TNE.debug("Requested Hash: " + MISCUtils.md5(requested));
    return instance().dupers.contains(MISCUtils.md5(requested));
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

    TNECommand ecoCommand = commandManager.find(label);
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

  public CommentedConfiguration mainConfigurations() {
    return mainConfigurations;
  }

  public CommentedConfiguration messageConfiguration() {
    return messageConfigurations;
  }

  public CommentedConfiguration itemConfiguration() {
    return itemConfigurations;
  }

  public CommentedConfiguration playerConfiguration() {
    return playerConfigurations;
  }

  public CommentedConfiguration worldConfiguration() {
    return worldConfigurations;
  }

  public CommentedConfiguration getCurrencyConfigurations() {
    return currencyConfigurations;
  }

  private void initializeConfigurations() {
    TNE.logger().info("Loading Configurations.");
    mainConfig = new File(getDataFolder(), "config.yml");
    currencies = new File(getDataFolder(), "currency.yml");
    items = new File(getDataFolder(), "items.yml");
    messagesFile = new File(getDataFolder(), "messages.yml");
    players = new File(getDataFolder(), "players.yml");
    worlds = new File(getDataFolder(), "worlds.yml");

    TNE.logger().info("Initializing Configurations.");
    mainConfigurations = initializeConfiguration(mainConfig, "config.yml");
    TNE.logger().info("Initialized config.yml");
    currencyConfigurations = initializeConfiguration(currencies, "currency.yml");
    TNE.logger().info("Initialized currency.yml");
    messageConfigurations = initializeConfiguration(messagesFile, "messages.yml");
    TNE.logger().info("Initialized messages.yml");
    playerConfigurations = initializeConfiguration(players, "players.yml");
    TNE.logger().info("Initialized players.yml");
    worldConfigurations = initializeConfiguration(worlds, "worlds.yml");
    TNE.logger().info("Initialized worlds.yml");
    Bukkit.getScheduler().runTaskAsynchronously(this, ()-> {
      final String itemsFile = (MISCUtils.isOneThirteen())? "items.yml" : "items-1.12.yml";
      itemConfigurations = initializeConfiguration(items, itemsFile);
      MaterialHelper.initialize();
      itemCompatibility = (MISCUtils.isOneThirteen())? new ItemCompatibility13() : new ItemCompatibility12();
      menuManager = new MenuManager();
      TNE.debug("Preparing menus");
      loader.getModules().forEach((key, value)->
          value.getModule().registerMenus(this).forEach((name, menu)->{
            menuManager.menus.put(name, menu);
          })
      );
      TNE.logger().info("Initialized items.yml");

      loader.getModules().forEach((key, value) -> {
        value.getModule().initializeConfigurations();
      });
    });
  }

  public CommentedConfiguration initializeConfiguration(File file, String defaultFile) {
    TNE.debug("Started copying " + file.getName());
    CommentedConfiguration commentedConfiguration = null;
    try {
      commentedConfiguration = new CommentedConfiguration(file, new InputStreamReader(this.getResource(defaultFile), "UTF8"));
      TNE.debug("Initializing commented configuration");
    } catch (UnsupportedEncodingException ignore) {
    }
    if(commentedConfiguration != null) {
      TNE.debug("Loading commented configuration");
      commentedConfiguration.load();
    }
    TNE.debug("Finished copying " + file.getName());
    return commentedConfiguration;
  }

  public static void debug(StackTraceElement[] stack) {
    for(StackTraceElement element : stack) {
      logger().warning(element.toString());
    }
  }

  public static void debug(String message) {
    if(consoleDebug) System.out.println(message);
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

  public boolean hasWorldManager(String world) {
    return worldManagers.containsKey(world);
  }

  public WorldManager getWorldManager(String world) {
    for(WorldManager manager : this.worldManagers.values()) {
      if(manager.getWorld().equalsIgnoreCase(world)) {
        debug("Return World Manager for world " + world);
        return manager;
      }
    }
    return worldManagers.get(defaultWorld);
  }

  public Collection<WorldManager> getWorldManagers() {
    return worldManagers.values();
  }

  public Map<String, WorldManager> getWorldManagersMap() {
    return worldManagers;
  }

  public File getMessagesFile() {
    return messagesFile;
  }

  public File getWorlds() {
    return worlds;
  }

  public static ItemCompatibility item() {
    return instance().itemCompatibility;
  }

  public static Boolean hasPermssion(CommandSender sender, String permission) {
    if(sender instanceof Player) {
      if(instance().developers.contains(((Player) sender).getUniqueId().toString())) {
        return true;
      }
    }
    return sender.hasPermission(permission);
  }
}