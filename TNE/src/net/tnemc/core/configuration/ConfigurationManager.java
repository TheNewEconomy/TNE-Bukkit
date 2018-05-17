package net.tnemc.core.configuration;

import com.github.tnerevival.user.IDFinder;
import net.tnemc.core.TNE;
import net.tnemc.core.configuration.utils.FileMgmt;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static java.util.logging.Level.SEVERE;

public class ConfigurationManager {
  private static Map<String, ConfigurationEntry> configurations = new HashMap<>();

  private static JavaPlugin plugin;

  public static void initialize(JavaPlugin plugin) {
    ConfigurationManager.plugin = plugin;
  }


  public static boolean loadSettings() {
    return loadSettings(false);
  }

  public static boolean loadSettings(boolean modulesOnly) {
    try {
      FileMgmt.checkFolders(new String[]{ getRootFolder() });

      configurations.forEach((file, entry)->{
        if(!modulesOnly || modulesOnly && entry.isModule()) {
          String version = (entry.isModule()) ? TNE.loader().getModule(entry.getOwner()).getInfo().version() :
              plugin.getDescription().getVersion();
          loadConfig(entry, version);
        }
      });

    } catch (Exception e) {
      return false;
    }
    return true;
  }

  public static CommentedConfiguration getConfigurationFile(String file) {
    return configurations.get(file).getNewConfig();
  }

  public static void addConfiguration(ConfigurationEntry entry) {
    configurations.put(entry.getFile(), entry);
  }


  private static void loadConfig(ConfigurationEntry entry, String version) {

    File file = FileMgmt.CheckYMLExists(new File(getRootFolder() + FileMgmt.fileSeparator() + entry.getFile()));
    if (file != null) {

      // read the config.yml into memory
      entry.setOldConfig(new CommentedConfiguration(file));
      if (!entry.getOldConfig().load()) {
        plugin.getLogger().log(SEVERE, "Failed to load configuration");
      }

      setDefaults(version, entry);

      entry.getOldConfig().save();
    }
  }

  /**
   * Builds a new config reading old config data.
   */
  private static void setDefaults(String version, ConfigurationEntry entry) {

    entry.setNewConfig(new CommentedConfiguration(new File(getRootFolder() + FileMgmt.fileSeparator() + entry.getFile())));
    entry.getNewConfig().load();

    for(IConfigNode node : entry.getNodes()) {
      if (node.getComments().length > 0) {
        addComment(entry, node.getNode(), node.getComments());
      }
      if (node.getNode().equals(CommonNodes.VERSION.getNode())) {
        setNewProperty(entry, node.getNode(), version);
      } else if (node.getNode().equals(CommonNodes.LAST_RUN_VERSION.getNode())) {
        setNewProperty(entry, node.getNode(), getLastRunVersion(entry.getFile(), version));
      } else {
        setNewProperty(entry, node.getNode(), (entry.getOldConfig().get(node.getNode().toLowerCase()) != null) ? entry.getOldConfig().get(node.getNode().toLowerCase()) : node.getDefaultValue());
      }
    }
    entry.setOldConfig(entry.getNewConfig());
    entry.setNewConfig(null);
  }

  private static void addComment(ConfigurationEntry entry, String root, String... comments) {
    entry.getNewConfig().addComment(root.toLowerCase(), comments);
  }

  private static void setNewProperty(ConfigurationEntry entry, String root, Object value) {

    if (value == null) {
      // System.out.print("value is null for " + root.toLowerCase());
      value = "";
    }
    entry.getNewConfig().set(root.toLowerCase(), value.toString());
  }

  private static String getLastRunVersion(String file, String currentVersion) {

    return getString(file, CommonNodes.LAST_RUN_VERSION.getNode(), currentVersion);
  }

  private static String getString(String file, String root, String def) {

    String data = configurations.get(file).getOldConfig().getString(root.toLowerCase(), def);
    if (data == null) {
      sendError(root.toLowerCase() + " from " + file);
      return "";
    }
    return data;
  }

  private static void sendError(String msg) {

    plugin.getLogger().log(SEVERE, "Error could not read " + msg);
  }

  public static String getRootFolder() {
    return plugin.getDataFolder().getPath();
  }

  public static String getDataFolder() {
    return getRootFolder() + FileMgmt.fileSeparator() + "data";
  }

  public static IConfigNode stringToNode(String file, String node) {
    if(configurations.containsKey(file)) {
      for (IConfigNode iNode : configurations.get(file).getNodes()) {
        if(iNode.getNode().equalsIgnoreCase(node)) return iNode;
      }
    }
    return null;
  }

  public static String translate(String node, String world, String player) {
    if(configurations.get("players.yml").getOldConfig().contains(player + "." + node)) {
      return configurations.get("players.yml").getOldConfig().getString(player + "." + node);
    }

    if(configurations.get("worlds.yml").getOldConfig().contains(world + "." + node)) {
      return configurations.get("worlds.yml").getOldConfig().getString(world + "." + node);
    }

    String language = (player.trim().equalsIgnoreCase(""))? "Default" : TNE.manager().getAccount(IDFinder.getID(player)).getLanguage();

    if(TNE.instance().getLanguages().containsKey(language)) {
      if(TNE.instance().getLanguage(language).hasTranslation(node)) {
        return TNE.instance().getLanguage(language).getTranslation(node);
      }
    }
    return null;
  }

  public static void setConfiguration(String file, String node, Object value) {
    if(configurations.containsKey(file)) {
      configurations.get(file).getOldConfig().set(node, value);
      configurations.get(file).getOldConfig().save();
    }
  }

  public static Object getConfiguration(String file, IConfigNode node, String world, String player) {
    Object value = getConfiguration(file, node.getNode(), world, player);
    return (value != null)? value : node.getDefaultValue();
  }

  public static Object getConfiguration(String file, String node, String world, String player) {
    if(configurations.get("players.yml").getOldConfig().contains(player + "." + node)) {
      return configurations.get("players.yml").getOldConfig().get(player + "." + node);
    }

    if(configurations.get("worlds.yml").getOldConfig().contains(world + "." + node)) {
      return configurations.get("worlds.yml").getOldConfig().get(world + "." + node);
    }

    if(configurations.containsKey(file)) {
      return configurations.get(file).getOldConfig().get(node, null);
    }
    return null;
  }

  public static String getString(String file, String node) {
    IConfigNode configNode = stringToNode(file, node);
    if(configNode != null) {
      return (String)getConfiguration(file, configNode, "", "");
    }
    return null;
  }

  public static String getString(String file, String node, boolean translatable, String world, String player) {
    IConfigNode configNode = stringToNode(file, node);
    if(configNode != null) {
      return (translatable)? translate(node, world, player) : (String)getConfiguration(file, configNode, world, player);
    }
    return null;
  }

  public static String getString(String file, String node, boolean translatable, String world, UUID player) {
    IConfigNode configNode = stringToNode(file, node);
    if(configNode != null) {
      return (translatable)? translate(node, world, player.toString()) : (String)getConfiguration(file, configNode, world, player.toString());
    }
    return null;
  }

  public static Boolean getBoolean(String file, String node) {
    IConfigNode configNode = stringToNode(file, node);
    if(configNode != null) {
      return (boolean)getConfiguration(file, configNode, "", "");
    }
    return null;
  }

  public static Boolean getBoolean(String file, String node, String world, String player) {
    IConfigNode configNode = stringToNode(file, node);
    if(configNode != null) {
      return (boolean)getConfiguration(file, configNode, world, player);
    }
    return null;
  }

  public static Boolean getBoolean(String file, String node, String world, UUID player) {
    return getBoolean(file, node, world, player.toString());
  }

  public static Double getDouble(String file, String node) {
    IConfigNode configNode = stringToNode(file, node);
    if(configNode != null) {
      return (double)getConfiguration(file, configNode, "", "");
    }
    return null;
  }

  public static Double getDouble(String file, String node, String world, String player) {
    IConfigNode configNode = stringToNode(file, node);
    if(configNode != null) {
      return (double)getConfiguration(file, configNode, world, player);
    }
    return null;
  }

  public static Double getDouble(String file, String node, String world, UUID player) {
    return getDouble(file, node, world, player.toString());
  }

  public static Integer getInteger(String file, String node) {
    IConfigNode configNode = stringToNode(file, node);
    if(configNode != null) {
      return (int)getConfiguration(file, configNode, "", "");
    }
    return null;
  }

  public static Integer getInteger(String file, String node, String world, String player) {
    IConfigNode configNode = stringToNode(file, node);
    if(configNode != null) {
      return (int)getConfiguration(file, configNode, world, player);
    }
    return null;
  }

  public static Integer getInteger(String file, String node, String world, UUID player) {
    return getInteger(file, node, world, player.toString());
  }

  public static Long getLong(String file, String node) {
    IConfigNode configNode = stringToNode(file, node);
    if(configNode != null) {
      return (long)getConfiguration(file, configNode, "", "");
    }
    return null;
  }

  public static Long getLong(String file, String node, String world, String player) {
    IConfigNode configNode = stringToNode(file, node);
    if(configNode != null) {
      return (long)getConfiguration(file, configNode, world, player);
    }
    return null;
  }

  public static Long getLong(String file, String node, String world, UUID player) {
    return getLong(file, node, world, player.toString());
  }


  public static String getString(String file, IConfigNode node) {
    return configurations.get(file).getOldConfig().getString(node.getNode().toLowerCase(), node.getDefaultValue());

  }

  public static boolean getBoolean(String file, IConfigNode node) {

    return Boolean.parseBoolean(configurations.get(file).getOldConfig().getString(node.getNode().toLowerCase(), node.getDefaultValue()));
  }

  public static double getDouble(String file, IConfigNode node) {

    try {
      return Double.parseDouble(configurations.get(file).getOldConfig().getString(node.getNode().toLowerCase(), node.getDefaultValue()).trim());
    } catch (NumberFormatException e) {
      sendError(node.getNode().toLowerCase() + " from " + file);
      return 0.0;
    }
  }

  public static int getInt(String file, IConfigNode node) {

    try {
      return Integer.parseInt(configurations.get(file).getOldConfig().getString(node.getNode().toLowerCase(), node.getDefaultValue()).trim());
    } catch (NumberFormatException e) {
      sendError(node.getNode().toLowerCase() + " from " + file);
      return 0;
    }
  }
}
