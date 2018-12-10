package net.tnemc.signs;

import com.github.tnerevival.core.SaveManager;
import com.github.tnerevival.core.db.SQLDatabase;
import net.tnemc.core.TNE;
import net.tnemc.core.common.module.Module;
import net.tnemc.core.common.module.ModuleInfo;
import net.tnemc.core.menu.Menu;
import net.tnemc.core.menu.impl.CurrencySelectionMenu;
import net.tnemc.signs.handlers.NationHandler;
import net.tnemc.signs.handlers.PlayerHandler;
import net.tnemc.signs.handlers.TownHandler;
import net.tnemc.signs.listeners.BlockListener;
import net.tnemc.signs.listeners.PlayerListener;
import net.tnemc.signs.listeners.TownyListener;
import net.tnemc.signs.signs.SignType;
import net.tnemc.signs.signs.impl.item.menu.AmountSelectionMenu;
import net.tnemc.signs.signs.impl.item.menu.ItemAmountSelection;
import net.tnemc.signs.signs.impl.item.menu.OfferMenu;
import net.tnemc.signs.signs.impl.item.menu.ShulkerPreviewMenu;
import net.tnemc.signs.signs.impl.item.menu.itemselection.ConfirmIcon;
import net.tnemc.signs.signs.impl.item.menu.itemselection.ConfirmTradeIcon;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * All rights reserved.
 **/
@ModuleInfo(
    name = "Signs",
    author = "creatorfromhell",
    version = "0.1.0"
)
public class SignsModule extends Module {


  private File signs;
  private FileConfiguration fileConfiguration;
  private SignsConfiguration configuration;

  private SignsManager manager;

  private static SignsModule instance;

  public SignsModule() {
    instance = this;
    manager = new SignsManager();
  }

  @Override
  public void load(TNE tne, String version) {
    Bukkit.getServer().getPluginManager().registerEvents(new BlockListener(tne), tne);
    Bukkit.getServer().getPluginManager().registerEvents(new PlayerListener(tne), tne);

    if(Bukkit.getPluginManager().getPlugin("Towny") != null) {
      Bukkit.getServer().getPluginManager().registerEvents(new TownyListener(tne), tne);
    }

    tne.logger().info("Signs Module loaded!");
  }

  @Override
  public void unload(TNE tne) {
    if(!signs.exists()) {
      configuration.save(fileConfiguration);
    }
    tne.logger().info("Signs Module unloaded!");
  }

  /**
   * Called at the last portion of TNE's onEnable, post initialization.
   *
   * @param tne An instance of the main TNE class.
   */
  @Override
  public void postLoad(TNE tne) {
    TNE.manager().registerHandler(new NationHandler());
    TNE.manager().registerHandler(new PlayerHandler());
    TNE.manager().registerHandler(new TownHandler());
  }

  @Override
  public void initializeConfigurations() {
    super.initializeConfigurations();
    signs = new File(TNE.instance().getDataFolder(), "signs.yml");
    fileConfiguration = YamlConfiguration.loadConfiguration(signs);
  }

  @Override
  public void loadConfigurations() {
    super.loadConfigurations();
    fileConfiguration.options().copyDefaults(true);
    configuration = new SignsConfiguration();
    configurations.put(configuration, "Signs");
  }

  @Override
  public Map<String, Menu> registerMenus(TNE pluginInstance) {
    Map<String, Menu> menus = new HashMap<>();
    menus.put("shop_currency_selection", new CurrencySelectionMenu("shop_currency_selection", "shop_amount_selection"));
    menus.put("shop_amount_selection", new AmountSelectionMenu("shop_amount_selection"));
    menus.put("shop_offer_menu", new OfferMenu());
    menus.put("shop_shulker_preview", new ShulkerPreviewMenu());
    menus.put("shop_offer_amount_selection", new ItemAmountSelection("shop_offer_amount_selection", "shop_offer_amount", new ConfirmIcon(42, "shop_offer_amount")));
    menus.put("shop_trade_amount_selection", new ItemAmountSelection("shop_trade_amount_selection", "shop_offer_amount", new ConfirmTradeIcon(42, "shop_offer_amount")));

    return menus;
  }

  @Override
  public void saveConfigurations() {
    super.saveConfigurations();
    if(!signs.exists()) {
      Reader stream = null;
      try {
        stream = new InputStreamReader(TNE.instance().getResource("signs.yml"), "UTF8");
      } catch (UnsupportedEncodingException e) {
        e.printStackTrace();
      }
      if (stream != null) {
        YamlConfiguration config = YamlConfiguration.loadConfiguration(stream);
        fileConfiguration.setDefaults(config);
      }
    }
    try {
      fileConfiguration.save(signs);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * @return Returns a list of tables that this module requires.
   * Format is <Database Type, List of table creation queries.
   */
  @Override
  public Map<String, List<String>> getTables() {
    Map<String, List<String>> tables = new HashMap<>();
    tables.put("h2", Collections.singletonList(SignsData.SIGNS_TABLE_H2));
    tables.put("mysql", Collections.singletonList(SignsData.SIGNS_TABLE));

    for(SignType type : manager.getSignTypes().values()) {
      if(type.tables().size() > 0) {
        for(Map.Entry<String, List<String>> entry : type.tables().entrySet()) {
          List<String> query = new ArrayList<>();
          query.addAll(entry.getValue());
          if(tables.containsKey(entry.getKey())) query.addAll(tables.get(entry.getKey()));
          tables.put(entry.getKey(), query);
        }
      }
    }

    return tables;
  }

  /**
   * Used to perform any data loading, manipulation, layout updating, etc.
   *
   * @param saveManager An instance of TNE's Save Manager
   */
  @Override
  public void enableSave(SaveManager saveManager) {
    for(String table : getTables().get(saveManager.getDataManager().getFormat().toLowerCase())) {
      try {
        ((SQLDatabase) TNE.saveManager().getTNEManager().getTNEProvider().connector()).executeUpdate(table);
        TNE.debug("Creating table: " + table);
      } catch (SQLException e) {
        TNE.debug("Failed to create tables on module load.");
      }
    }
  }

  public static SignsModule instance() {
    return instance;
  }

  public static SignsManager manager() {
    return instance.manager;
  }

  public File getSigns() {
    return signs;
  }

  public FileConfiguration getFileConfiguration() {
    return fileConfiguration;
  }

  public SignsConfiguration getConfiguration() {
    return configuration;
  }
}
