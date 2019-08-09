package net.tnemc.bounty;

import net.tnemc.bounty.command.BountyCommand;
import net.tnemc.bounty.configuration.BountyConfiguration;
import net.tnemc.bounty.configuration.HunterConfiguration;
import net.tnemc.bounty.listeners.InventoryCloseListener;
import net.tnemc.bounty.listeners.PlayerDeathListener;
import net.tnemc.bounty.listeners.PlayerJoinListener;
import net.tnemc.bounty.menu.AmountSelectionMenu;
import net.tnemc.bounty.menu.BountyHunterMenu;
import net.tnemc.bounty.menu.BountyViewMenu;
import net.tnemc.config.CommentedConfiguration;
import net.tnemc.core.TNE;
import net.tnemc.core.commands.TNECommand;
import net.tnemc.core.common.configurations.Configuration;
import net.tnemc.core.common.module.Module;
import net.tnemc.core.common.module.ModuleInfo;
import net.tnemc.core.common.module.ModuleListener;
import net.tnemc.core.menu.Menu;
import net.tnemc.core.menu.impl.CurrencySelectionMenu;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * Created by creatorfromhell on 6/28/2019.
 * <p>
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */
@ModuleInfo(
    name = "Bounty",
    author = "creatorfromhell",
    version = "0.1.1",
    updateURL = "https://tnemc.net/files/module-version.xml"
)
public class BountyModule implements Module {

  private static BountyModule instance;

  private HunterManager hunterManager;

  private File bounty;
  private File hunter;
  private CommentedConfiguration bountyFileConfiguration;
  private CommentedConfiguration hunterFileConfiguration;
  private BountyConfiguration bountyConfiguration;
  private HunterConfiguration hunterConfiguration;

  public BountyModule() {
    instance = this;
  }

  @Override
  public void load(TNE tne) {
    TNE.logger().info("Bounty Module loaded!");
  }

  /**
   * @param plugin The instance of the main TNE plugin class.
   * @return A list of event listeners.
   */
  @Override
  public List<ModuleListener> listeners(TNE plugin) {
    List<ModuleListener> listeners = new ArrayList<>();
    listeners.add(new InventoryCloseListener(plugin));
    listeners.add(new PlayerDeathListener(plugin));
    listeners.add(new PlayerJoinListener(plugin));

    return listeners;
  }

  @Override
  public String tablesFile() {
    /*Map<String, List<String>> tables = new HashMap<>();

    List<String> h2 = new ArrayList<>();
    //H2 Tables
    h2.add("CREATE TABLE IF NOT EXISTS " + BountyData.prefix + "_BOUNTY (" +
        "`bounty_id` VARCHAR(36) NOT NULL UNIQUE," +
        "`bounty_target` VARCHAR(36) NOT NULL," +
        "`bounty_benefactor` VARCHAR(36) NOT NULL," +
        "`bounty_created` BIGINT(60)," +
        "`bounty_head` BOOLEAN NOT NULL DEFAULT 0," +
        "`bounty_finished` BIGINT(60)," +
        "`bounty_claimed` BOOLEAN NOT NULL DEFAULT 0," +
        "`bounty_claimant` VARCHAR(36) NOT NULL," +
        "`bounty_currency` BOOLEAN NOT NULL DEFAULT 0," +
        "`bounty_currency_name` VARCHAR(70) NOT NULL," +
        "`bounty_world` VARCHAR(70) NOT NULL," +
        "`bounty_amount` DECIMAL(49,4) DEFAULT 0," +
        "`bounty_reward` TEXT NOT NULL" +
        ") ENGINE = INNODB;");

    h2.add("CREATE TABLE IF NOT EXISTS " + BountyData.prefix + "_BOUNTY_HUNTER (" +
        "`hunter_id` VARCHAR(36) NOT NULL UNIQUE," +
        "`hunter_last_bounty` BIGINT(60)," +
        "`hunter_experience` BIGINT(60)," +
        "`hunter_bounties` BIGINT(60)," +
        "`hunter_last_track` BIGINT(60)," +
        "`hunter_message` TEXT NOT NULL," +
        "`hunter_level` BIGINT(60)" +
        ") ENGINE = INNODB;");

    h2.add("CREATE TABLE IF NOT EXISTS " + BountyData.prefix + "_BOUNTY_REWARD (" +
        "`hunter_id` VARCHAR(36) NOT NULL UNIQUE," +
        "`hunter_rewards` TEXT NOT NULL" +
        ") ENGINE = INNODB;");

    tables.put("h2", h2);

    List<String> mysql = new ArrayList<>();

    //MySQL Tables
    mysql.add("CREATE TABLE IF NOT EXISTS " + BountyData.prefix + "_BOUNTY (" +
        "`bounty_id` VARCHAR(36) NOT NULL UNIQUE," +
        "`bounty_target` VARCHAR(36) NOT NULL," +
        "`bounty_benefactor` VARCHAR(36) NOT NULL," +
        "`bounty_created` BIGINT(60)," +
        "`bounty_head` BOOLEAN NOT NULL DEFAULT 0," +
        "`bounty_finished` BIGINT(60)," +
        "`bounty_claimed` BOOLEAN NOT NULL DEFAULT 0," +
        "`bounty_claimant` VARCHAR(36) NOT NULL," +
        "`bounty_currency` BOOLEAN NOT NULL DEFAULT 0," +
        "`bounty_currency_name` VARCHAR(70) NOT NULL," +
        "`bounty_world` VARCHAR(70) NOT NULL," +
        "`bounty_amount` DECIMAL(49,4) DEFAULT 0," +
        "`bounty_reward` TEXT NOT NULL" +
        ") ENGINE = INNODB DEFAULT CHARACTER SET utf8 COLLATE utf8_unicode_ci;");

    mysql.add("CREATE TABLE IF NOT EXISTS " + BountyData.prefix + "_BOUNTY_HUNTER (" +
        "`hunter_id` VARCHAR(36) NOT NULL UNIQUE," +
        "`hunter_last_bounty` BIGINT(60)," +
        "`hunter_experience` BIGINT(60)," +
        "`hunter_bounties` BIGINT(60)," +
        "`hunter_last_track` BIGINT(60)," +
        "`hunter_message` TEXT NOT NULL," +
        "`hunter_level` BIGINT(60)" +
        ") ENGINE = INNODB DEFAULT CHARACTER SET utf8 COLLATE utf8_unicode_ci;");

    mysql.add("CREATE TABLE IF NOT EXISTS " + BountyData.prefix + "_BOUNTY_REWARD (" +
        "`hunter_id` VARCHAR(36) NOT NULL UNIQUE," +
        "`hunter_rewards` TEXT NOT NULL" +
        ") ENGINE = INNODB DEFAULT CHARACTER SET utf8 COLLATE utf8_unicode_ci;");

    tables.put("mysql", mysql);*/

    return "bounty_tables.yml";
  }

  /**
   * @return A list of commands that should be added from this module.
   */
  @Override
  public List<TNECommand> commands() {
    return Collections.singletonList(new BountyCommand(TNE.instance()));
  }

  @Override
  public Map<String, Menu> menus(TNE pluginInstance) {
    Map<String, Menu> menus = new HashMap<>();
    menus.put("bounty_currency_selection", new CurrencySelectionMenu("bounty_currency_selection", "bounty_amount_selection"));
    menus.put("bounty_amount_selection", new AmountSelectionMenu("bounty_amount_selection"));
    menus.put("bounty_view_player", new BountyViewMenu());
    menus.put("bounty_hunter_menu", new BountyHunterMenu());

    return menus;
  }

  @Override
  public void initializeConfigurations() {
    bounty = new File(TNE.instance().getDataFolder(), "bounty.yml");
    hunter = new File(TNE.instance().getDataFolder(), "hunter.yml");
    bountyFileConfiguration = initializeConfiguration(bounty, "bounty.yml");
    hunterFileConfiguration = initializeConfiguration(hunter, "hunter.yml");

    hunterManager = new HunterManager();
  }

  @Override
  public void loadConfigurations() {
    bountyConfiguration = new BountyConfiguration();
    hunterConfiguration = new HunterConfiguration();
  }

  @Override
  public Map<Configuration, String> configurations() {
    Map<Configuration, String> configurations = new HashMap<>();
    configurations.put(bountyConfiguration, "Bounty");
    configurations.put(hunterConfiguration, "Hunter");

    return configurations;
  }

  @Override
  public void saveConfigurations() {
    bountyFileConfiguration.save(bounty);
    hunterFileConfiguration.save(hunter);
  }

  public CommentedConfiguration initializeConfiguration(File file, String defaultFile) {
    TNE.debug("Started copying " + file.getName());
    CommentedConfiguration commentedConfiguration = new CommentedConfiguration(file, new InputStreamReader(getResource(defaultFile), StandardCharsets.UTF_8), false);
    TNE.debug("Initializing commented configuration");
    if(commentedConfiguration != null) {
      TNE.debug("Loading commented configuration");
      commentedConfiguration.load();
    }
    TNE.debug("Finished copying " + file.getName());
    return commentedConfiguration;
  }


  public InputStream getResource(@NotNull String filename) {
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

  public static BountyModule instance() {
    return instance;
  }

  public File getBounty() {
    return bounty;
  }

  public File getHunter() {
    return hunter;
  }

  public CommentedConfiguration getBountyFileConfiguration() {
    return bountyFileConfiguration;
  }

  public CommentedConfiguration getHunterFileConfiguration() {
    return hunterFileConfiguration;
  }

  public BountyConfiguration getBountyConfiguration() {
    return bountyConfiguration;
  }

  public HunterConfiguration getHunterConfiguration() {
    return hunterConfiguration;
  }

  public HunterManager getHunterManager() {
    return hunterManager;
  }
}