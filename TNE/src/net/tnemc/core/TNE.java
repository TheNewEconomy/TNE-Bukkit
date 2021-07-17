package net.tnemc.core;

import com.github.tnerevival.TNELib;
import com.github.tnerevival.core.UpdateChecker;
import com.github.tnerevival.core.db.SQLDatabase;
import com.hellyard.cuttlefish.grammar.yaml.YamlValue;
import net.milkbowl.vault.economy.Economy;
import net.tnemc.commands.bukkit.BukkitCommandsHandler;
import net.tnemc.commands.bukkit.provider.BukkitPlayerProvider;
import net.tnemc.commands.core.CommandInformation;
import net.tnemc.commands.core.CommandsHandler;
import net.tnemc.config.CommentedConfiguration;
import net.tnemc.core.commands.ExecutorsRegistry;
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
import net.tnemc.core.common.configurations.PlayerConfigurations;
import net.tnemc.core.common.configurations.WorldConfigurations;
import net.tnemc.core.common.data.TNEDataManager;
import net.tnemc.core.common.data.TNESaveManager;
import net.tnemc.core.common.material.MaterialHelper;
import net.tnemc.core.common.module.ModuleLoader;
import net.tnemc.core.common.module.cache.ModuleFileCache;
import net.tnemc.core.common.utils.BStats;
import net.tnemc.core.common.utils.MISCUtils;
import net.tnemc.core.common.utils.MaterialUtils;
import net.tnemc.core.common.utils.TNETranslator;
import net.tnemc.core.common.uuid.UUIDAPI;
import net.tnemc.core.common.uuid.impl.AshconAPI;
import net.tnemc.core.compatibility.ItemCompatibility;
import net.tnemc.core.compatibility.item.ItemCompatibility12;
import net.tnemc.core.compatibility.item.ItemCompatibility13;
import net.tnemc.core.compatibility.item.ItemCompatibility7;
import net.tnemc.core.event.module.TNEModuleLoadEvent;
import net.tnemc.core.event.module.TNEModuleUnloadEvent;
import net.tnemc.core.listeners.entity.EntityDeathListener;
import net.tnemc.core.listeners.entity.EntityPortalListener;
import net.tnemc.core.listeners.inventory.InventoryClickListener;
import net.tnemc.core.listeners.item.CraftItemListener;
import net.tnemc.core.listeners.mcmmo.PlayerFishingTreasureListener;
import net.tnemc.core.listeners.message.TNEMessageListener;
import net.tnemc.core.listeners.player.PlayerChangedWorldListener;
import net.tnemc.core.listeners.player.PlayerChannelListener;
import net.tnemc.core.listeners.player.PlayerChatListener;
import net.tnemc.core.listeners.player.PlayerInteractEntityListener;
import net.tnemc.core.listeners.player.PlayerInteractListener;
import net.tnemc.core.listeners.player.PlayerJoinListener;
import net.tnemc.core.listeners.player.PlayerPreLoginListener;
import net.tnemc.core.listeners.player.PlayerQuitListener;
import net.tnemc.core.listeners.player.PlayerTeleportListener;
import net.tnemc.core.listeners.world.WorldLoadListener;
import net.tnemc.core.menu.MenuManager;
import net.tnemc.core.worker.SaveWorker;
import net.tnemc.dbupdater.core.TableManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.plugin.ServicePriority;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.UUID;
import java.util.logging.Logger;
import java.util.regex.Pattern;

/**
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */
public class TNE extends TNELib implements TabCompleter {

  //constants
  public static final String coreURL = "https://tnemc.net/files/module-version.xml";

  public static final Pattern USERNAME_PATTERN = Pattern.compile("^\\w*$");

  public static final UUIDAPI uuidAPI = new AshconAPI();

  public static final String build = "0.1.1.13";
  public final List<String> developers = Collections.singletonList("5bb0dcb3-98ee-47b3-8f66-3eb1cdd1a881");

  private Map<String, WorldManager> worldManagers = new HashMap<>();
  private List<UUID> tnemodUsers = new ArrayList<>();

  private List<String> dupers;
  public List<String> exclusions;

  private EconomyManager manager;
  private MenuManager menuManager;
  private static net.tnemc.core.common.configurations.ConfigurationManager configurations;

  private CommandsHandler handler;

  protected ModuleFileCache moduleCache;

  private ModuleLoader loader;
  public UpdateChecker updater;
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
  private File commands;
  private File items;
  private File messagesFile;
  private File players;
  private File worlds;

  private CommentedConfiguration mainConfigurations;
  private FileConfiguration commandsConfigurations;
  private CommentedConfiguration itemConfigurations;
  private CommentedConfiguration messageConfigurations;
  private CommentedConfiguration playerConfigurations;
  private CommentedConfiguration worldConfigurations;

  private MainConfigurations main;
  private MessageConfigurations messages;
  private WorldConfigurations world;
  private PlayerConfigurations player;

  private Thread autoSaver;

  private boolean blacklisted = false;
  public static boolean useMod = false;
  public static boolean fawe = true;

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

    fawe = true;

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

    currentSaveVersion = 1116.0;

    setUuidManager(new TNEUUIDManager());

    updater = new UpdateChecker("https://tnemc.net/files/tnebuild.txt", getDescription().getVersion());

    //Run the ModuleLoader
    loader = new ModuleLoader();
    loader.load();

    //Load modules
    loader.getModules().forEach((key, value)->{
      TNEModuleLoadEvent event = new TNEModuleLoadEvent(key, value.getInfo().version(), !Bukkit.getServer().isPrimaryThread());
      Bukkit.getServer().getPluginManager().callEvent(event);
      if(!event.isCancelled()) {
        value.getModule().load(this);
      }
    });

    //Configurations
    loadConfigurations();



    if(!mainConfigurations.getString("Core.DefaultWorld", "TNE_SYSTEM").equalsIgnoreCase("TNE_SYSTEM")) {
      addWorldManager(new WorldManager(mainConfigurations.getString("Core.DefaultWorld")));
    }

    getServer().getWorlds().forEach(world-> worldManagers.put(world.getName(), new WorldManager(world.getName(), mainConfigurations.getBool("Core.Multiworld"))));

    world = new WorldConfigurations();
    world.load(worldConfigurations);

    TNE.debug("Preparing debug mode");
    this.debugMode = mainConfigurations.getBool("Core.Debug");

    if(!mainConfigurations.getString("Core.DefaultWorld", "TNE_SYSTEM").equalsIgnoreCase("TNE_SYSTEM")) {
      defaultWorld = mainConfigurations.getString("Core.DefaultWorld");
      addWorldManager(new WorldManager(defaultWorld));
    }

    if(!mainConfigurations.contains("Core.Currency.Basic.Identifier")) {
      LinkedList<String> comments = new LinkedList<>();
      comments.add("The identifier for the basic currency, used for data handling.");
      mainConfigurations.setOrCreate("Core.Currency.Basic.Identifier", 0, new YamlValue(comments, "Dollar", "String"));
      mainConfigurations.save(main.getFile());
    }

    TNE.debug("Preparing module configurations for manager");
    loader.getModules().forEach((key, value)->{
      value.getModule().loadConfigurations();
      value.getModule().configurations().forEach((configuration, identifier)->{
        configurations().add(configuration, identifier);
      });
    });

    TNE.debug("Preparing configurations for manager");
    configurations().add(main, "main");
    configurations().add(messages, "messages");
    configurations().add(player, "player");
    configurations().add(world, "world");

    TNE.debug("Preparing commands");
    handler = new BukkitCommandsHandler(commandsConfigurations, this).withTranslator(new TNETranslator());


    //Load Module Sub Commands
    loader.getModules().forEach((key, value)-> {
      value.getModule().subCommands().forEach((command, moduleSubs)->{
        Optional<CommandInformation> find = CommandsHandler.manager().find(command);
        if(find.isPresent()) {
          moduleSubs.forEach((sub)->find.get().addSub(sub));
        }

        CommandsHandler.manager().register(find.get().getIdentifiers(true), find.get());
      });
    });

    //Executors
    ExecutorsRegistry.register();

    //Load Module Commands
    loader.getModules().forEach((key, value)-> value.getModule().commands().forEach((command)->{
      CommandsHandler.manager().register(command.getIdentifiers(true), command);
    }));

    //Load Module Executors
    loader.getModules().forEach((key, value)-> value.getModule().commandExecutors().forEach((name, executor)->{
      CommandsHandler.instance().addExecutor(name, executor);
    }));
    handler.load();

    //Check to see if the currencies directory exists, and if not create it and add the default USD currency.
    final File currenciesDirectory = new File(getDataFolder(), "currencies");
    if(!currenciesDirectory.exists()) {
      final File tiersDirectory = new File(currenciesDirectory, "USD");
      tiersDirectory.mkdirs();

      final CommentedConfiguration usd = new CommentedConfiguration(new File(currenciesDirectory, "USD.yml"), new InputStreamReader(this.getResource("currency/USD.yml"), StandardCharsets.UTF_8));
      usd.load();

      final CommentedConfiguration one = new CommentedConfiguration(new File(tiersDirectory, "one.yml"), new InputStreamReader(this.getResource("currency/USD/one.yml"), StandardCharsets.UTF_8));
      one.load();

      final CommentedConfiguration penny = new CommentedConfiguration(new File(tiersDirectory, "penny.yml"), new InputStreamReader(this.getResource("currency/USD/penny.yml"), StandardCharsets.UTF_8));
      penny.load();

    }

    //Initialize our plugin's managers.
    TNE.debug("Preparing managers");
    manager = new EconomyManager();

    //General Variables based on configuration values
    TNE.debug("Preparing variables");
    serverName = mainConfigurations.getString("Core.Server.Name", "Main Server");
    consoleName = mainConfigurations.getString("Core.Server.Account.Name", "Server_Account");
    useUUID = configurations().getBoolean("Core.UUID");

    if(MISCUtils.isOneSix()) useUUID = false;

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
    saveManager().addVersion(1116.0, true);

    TNE.debug("Initializing Save Manager.");
    try {
      saveManager().initialize();
    } catch (SQLException e) {
      TNE.debug(e);
    }

    TNE.debug("Preparing modules");
    loader.getModules().forEach((key, value)->{

      final String tablesFile = value.getModule().tablesFile();

      if(!tablesFile.trim().equalsIgnoreCase("")) {

        SQLDatabase.open();
        TableManager manager = new TableManager(sManager.getTNEManager().getFormat().toLowerCase(), sManager.getTNEManager().getPrefix());
        manager.generateQueriesAndRun(SQLDatabase.getDb().getConnection(), value.getModule().getResource(tablesFile));
        SQLDatabase.close();
      }
    });

    if(saveManager().getTables(configurations().getString("Core.Database.Type").toLowerCase()).size() > 0) {
      try {
        saveManager().getTNEManager().getTNEProvider().createTables(saveManager().getTables(configurations().getString("Core.Database.Type").toLowerCase()));
      } catch (SQLException e) {
        TNE.debug(e);
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
      TNE.debug(e);
    }

    //Bukkit Runnables & Workers
    TNE.debug("Preparing autosavers");
    if(configurations().getBoolean("Core.AutoSaver.Enabled")) {
      autoSaver = new Thread(new SaveWorker(this, configurations().getLong("Core.AutoSaver.Interval")));
      autoSaver.start();
    }

    if(Bukkit.getPluginManager().getPlugin("mcMMO") != null && api().getBoolean("Core.Server.ThirdParty.McMMORewards")) {
      getServer().getPluginManager().registerEvents(new PlayerFishingTreasureListener(this), this);
    }

    TNE.debug("Preparing events");

    //Entity
    getServer().getPluginManager().registerEvents(new EntityDeathListener(this), this);
    getServer().getPluginManager().registerEvents(new EntityPortalListener(this), this);

    //Inventory
    getServer().getPluginManager().registerEvents(new InventoryClickListener(this), this);

    //Item
    getServer().getPluginManager().registerEvents(new CraftItemListener(this), this);

    //Player
    getServer().getPluginManager().registerEvents(new PlayerChangedWorldListener(this), this);
    getServer().getPluginManager().registerEvents(new PlayerChannelListener(this), this);
    getServer().getPluginManager().registerEvents(new PlayerChatListener(this), this);
    getServer().getPluginManager().registerEvents(new PlayerInteractEntityListener(this), this);
    getServer().getPluginManager().registerEvents(new PlayerInteractListener(this), this);
    getServer().getPluginManager().registerEvents(new PlayerJoinListener(this), this);
    getServer().getPluginManager().registerEvents(new PlayerPreLoginListener(this), this);
    getServer().getPluginManager().registerEvents(new PlayerQuitListener(this), this);
    getServer().getPluginManager().registerEvents(new PlayerTeleportListener(this), this);

    //World
    getServer().getPluginManager().registerEvents(new WorldLoadListener(this), this);

    //Test


    loader.getModules().forEach((key, value)->{
      value.getModule().listeners(this).forEach(listener->{
        getServer().getPluginManager().registerEvents(listener, this);
        TNE.debug("Registering Listener");
      });
    });


    TNE.debug("Preparing postLoad");
    loader.getModules().forEach((key, value)->
        value.getModule().postLoad(this)
    );

    moduleCache = new ModuleFileCache();

    //Metrics
    TNE.debug("Preparing metrics");
    new BStats(this, 602);


    TNE.debug("Preparing TNE Forge Mod support");
    useMod = configurations.getBoolean("Core.Server.TNEMod");

    if(useMod) {
      Bukkit.getMessenger().registerOutgoingPluginChannel(this, "tnemod");
      Bukkit.getMessenger().registerIncomingPluginChannel(this, "tnemod", new TNEMessageListener());
    }



    TNE.debug("Preparing placeholders");
    if(Bukkit.getServer().getPluginManager().isPluginEnabled("PlaceholderAPI")) {
      new EconomyPlaceholders(this).register();
    }

    getLogger().info("The New Economy has been enabled!");
  }

  public void onDisable() {

    for(Player player : Bukkit.getServer().getOnlinePlayers()) {
      try {
        TNE.saveManager().getTNEManager().getTNEProvider().saveAccount(TNE.manager().getAccount(player.getUniqueId()));
      } catch (SQLException e) {
        TNE.debug(e);
      }
    }

    loader.getModules().forEach((key, value)->{
      value.getModule().disableSave(saveManager());
    });
    loader.getModules().forEach((key, value)->{
      TNEModuleUnloadEvent event = new TNEModuleUnloadEvent(key, value.getInfo().version(), !Bukkit.getServer().isPrimaryThread());
      Bukkit.getServer().getPluginManager().callEvent(event);
      value.getModule().unload(this);
    });
    SQLDatabase.close();
    SQLDatabase.getDataSource().close();
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

  @Override
  public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] arguments) {
    System.out.println("Tab Complete");
    return handler.tab(new BukkitPlayerProvider(sender), alias, arguments);
  }

  @Override
  public boolean onCommand(CommandSender sender, Command command, String label, String[] arguments) {
    List<String> triggers = new ArrayList<>(Arrays.asList(TNE.configurations().getString( "Core.Commands.Triggers", "main", "", "").split(",")));

    if(sender instanceof Player && !triggers.contains("/")) return false;
    return customCommand(sender, label, arguments);
  }

  public boolean customCommand(CommandSender sender, String label, String[] arguments) {
    return handler.handle(new BukkitPlayerProvider(sender), label, arguments);
  }

  public String sanitizeWorld(String world) {
    if(hasWorldManager(world)) return getWorldManager(world).getBalanceWorld();
    return world;
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

  public void loadConfigurations() {
    initializeConfigurations();

    TNE.debug("Preparing configuration instances");
    main = new MainConfigurations();

    exclusions = main.getConfiguration().getStringList("Core.Commands.Top.Exclusions");
    messages = new MessageConfigurations();
    messages.load(messageConfigurations);
    player = new PlayerConfigurations();
    player.load(playerConfigurations);
  }

  public void initializeConfigurations() {
    initializeConfigurations(true);
  }

  public void initializeConfigurations(boolean item) {
    TNE.logger().info("Loading Configurations.");
    mainConfig = new File(getDataFolder(), "config.yml");
    commands = new File(getDataFolder(), "commands.yml");
    items = new File(getDataFolder(), "items.yml");
    messagesFile = new File(getDataFolder(), "messages.yml");
    players = new File(getDataFolder(), "players.yml");
    worlds = new File(getDataFolder(), "worlds.yml");

    TNE.logger().info("Initializing Configurations.");
    List<String> skip = new ArrayList<>();
    skip.add("Items");
    skip.add("Virtual");
    mainConfigurations = initializeConfiguration(mainConfig, "config.yml");
    TNE.logger().info("Initialized config.yml");
    try {
      commandsConfigurations = initializeConfigurationBukkit(commands, "commands.yml");
    } catch (IOException ignore) {
    }
    TNE.logger().info("Initialized commands.yml");
    messageConfigurations = initializeConfiguration(messagesFile, "messages.yml");
    TNE.logger().info("Initialized messages.yml");
    playerConfigurations = initializeConfiguration(players, "players.yml");
    TNE.logger().info("Initialized players.yml");
    worldConfigurations = initializeConfiguration(worlds, "worlds.yml");
    TNE.logger().info("Initialized worlds.yml");
    if(item) {
      Bukkit.getScheduler().runTaskAsynchronously(this, () -> {
        final String itemsFile = (MISCUtils.isOneThirteen()) ? "items.yml" : "items-1.12.yml";
        itemConfigurations = initializeConfiguration(items, itemsFile);
        MaterialHelper.initialize();

        if (MISCUtils.isOneThirteen()) {
          itemCompatibility = new ItemCompatibility13();
        } else if (MISCUtils.isOneSeven()) {
          itemCompatibility = new ItemCompatibility7();
        } else {
          itemCompatibility = new ItemCompatibility12();
        }

        menuManager = new MenuManager();
        TNE.debug("Preparing menus");
        loader.getModules().forEach((key, value) ->
            value.getModule().menus(this).forEach((name, menu) -> {
              menuManager.menus.put(name, menu);
            })
        );
        TNE.logger().info("Initialized items.yml");

        manager.currencyManager().loadCurrencies();

        Bukkit.getScheduler().runTask(this, ()->{
          manager.currencyManager().loadRecipes();
        });

        TNE.debug("Preparing server account");
        if(api.getBoolean("Core.Server.Account.Enabled")) {
          TNE.debug("Account enabled");
          String world = worldManagers.get(defaultWorld).getBalanceWorld();
          TNE.debug("Got World");
          UUID id = IDFinder.getID(consoleName);
          TNE.debug("Got ID: " + id.toString());

          if(!manager.exists(id)) {
            TNE.debug("doesn't exist");
            special.add(id);
            TNE.debug("added special");
            api.getOrCreate(id);
            TNE.debug("api.getOrCreate");
            TNEAccount account = manager.getAccount(id);
            TNE.debug("Account Null? " + (account == null));
            TNE.debug("Balance Config Null? " + (api.getBigDecimal("Core.Server.Account.Balance") == null));
            account.setHoldings(world, manager.currencyManager().get(world).name(), api.getBigDecimal("Core.Server.Account.Balance"), true);
            getLogger().info("Created server economy account.");
          }
        }

        loader.getModules().forEach((key, value) -> {
          value.getModule().initializeConfigurations();
        });
      });
    }
  }

  public CommentedConfiguration initializeConfiguration(File file, String defaultFile) {
    TNE.debug("Started copying " + file.getName());
    CommentedConfiguration commentedConfiguration = null;

    try {
      commentedConfiguration = new CommentedConfiguration(file, new InputStreamReader(this.getResource(defaultFile), StandardCharsets.UTF_8), false);
      TNE.debug("Initializing commented configuration");
      if (commentedConfiguration != null) {
        TNE.debug("Loading commented configuration");
        commentedConfiguration.load();
      }
      TNE.debug("Finished copying " + file.getName());
    } catch(Exception e) {
      System.out.println("Error while trying to load: " + defaultFile);
      debug(e.getStackTrace());

    }
    return commentedConfiguration;
  }

  public FileConfiguration initializeConfigurationBukkit(File file, String defaultFile) throws IOException {

    FileConfiguration config = YamlConfiguration.loadConfiguration(file);


    Reader commandsStream = new InputStreamReader(this.getResource(defaultFile), StandardCharsets.UTF_8);

    if(!commands.exists() && commandsStream != null) {
      YamlConfiguration defaults = YamlConfiguration.loadConfiguration(commandsStream);

      config.setDefaults(defaults);
    }
    config.options().copyDefaults(true);
    config.save(file);

    return config;
  }

  InputStream getResourceUTF8(String filename) {
    try {
      URL url = getClass().getClassLoader().getResource(filename);
      if (url == null) {
        return null;
      } else {
        URLConnection connection = url.openConnection();
        connection.setUseCaches(false);
        return connection.getInputStream();
      }
    } catch (IOException var4) {
      return null;
    }
  }

  public static void debug(StackTraceElement[] stack) {
    System.out.println("Please let a professional know about the following:");
    System.out.println("------------------- TNE Error Log -------------------");
    for(StackTraceElement element : stack) {
      logger().warning(element.toString());
    }
    System.out.println("----------------- End of Error Log -----------------");
  }

  public static void debug(String message) {
    if(instance().debugMode) {
      System.out.println(message);
    }
  }

  public static void debug(Exception e) {
    debug(e.getStackTrace());
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
    TNE.debug("Returning World Manager for World: " + world);
    return worldManagers.getOrDefault(world, new WorldManager(world, mainConfigurations.getBool("Core.Multiworld")));
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

  public File getPlayers() {
    return players;
  }

  public PlayerConfigurations playerConfigurations() {
    return player;
  }

  public ModuleFileCache moduleCache() {
    return moduleCache;
  }

  public Thread autoSaver() {
    return autoSaver;
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
