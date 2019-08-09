package net.tnemc.signs;

import net.tnemc.config.CommentedConfiguration;
import net.tnemc.core.TNE;
import net.tnemc.core.commands.TNECommand;
import net.tnemc.core.common.configurations.Configuration;
import net.tnemc.core.common.module.Module;
import net.tnemc.core.common.module.ModuleInfo;
import net.tnemc.core.common.module.ModuleListener;
import net.tnemc.core.menu.Menu;
import net.tnemc.core.menu.impl.CurrencySelectionMenu;
import net.tnemc.signs.command.note.NoteCommand;
import net.tnemc.signs.listeners.BlockListener;
import net.tnemc.signs.listeners.ChestSelectionListener;
import net.tnemc.signs.listeners.PlayerListener;
import net.tnemc.signs.signs.impl.item.menu.AmountSelectionMenu;
import net.tnemc.signs.signs.impl.item.menu.ItemAmountSelection;
import net.tnemc.signs.signs.impl.item.menu.OfferMenu;
import net.tnemc.signs.signs.impl.item.menu.ShulkerPreviewMenu;
import net.tnemc.signs.signs.impl.item.menu.itemselection.ConfirmIcon;
import net.tnemc.signs.signs.impl.item.menu.itemselection.ConfirmTradeIcon;

import java.io.File;
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
    version = "0.1.1",
    updateURL = "https://tnemc.net/files/module-version.xml"
)
public class SignsModule implements Module {


  private File signs;
  private CommentedConfiguration fileConfiguration;
  private SignsConfiguration configuration;

  private SignsManager manager;

  private static SignsModule instance;

  public SignsModule() {
    instance = this;
    manager = new SignsManager();
  }

  @Override
  public void load(TNE tne) {
    TNE.logger().info("Signs Module loaded!");
  }

  /**
   * @param plugin The instance of the main TNE plugin class.
   * @return A list of event listeners.
   */
  @Override
  public List<ModuleListener> listeners(TNE plugin) {
    List<ModuleListener> listeners = new ArrayList<>();
    listeners.add(new BlockListener(plugin));
    listeners.add(new PlayerListener(plugin));
    listeners.add(new ChestSelectionListener(plugin));

    return listeners;
  }

  @Override
  public void unload(TNE tne) {
    if(fileConfiguration != null) {
      fileConfiguration.save(signs);
    }
    TNE.logger().info("Signs Module unloaded!");
  }

  @Override
  public void initializeConfigurations() {
    signs = new File(TNE.instance().getDataFolder(), "signs.yml");
    fileConfiguration = TNE.instance().initializeConfiguration(signs, "signs.yml");
  }

  @Override
  public void loadConfigurations() {
    configuration = new SignsConfiguration();
  }

  @Override
  public Map<Configuration, String> configurations() {
    return Collections.singletonMap(configuration, "Signs");
  }

  /**
   * @return A list of commands that should be added from this module.
   */
  @Override
  public List<TNECommand> commands() {
    return Collections.singletonList(new NoteCommand(TNE.instance()));
  }

  @Override
  public Map<String, Menu> menus(TNE pluginInstance) {
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
    fileConfiguration.save(signs);
  }

  /**
   * @return Returns a list of tables that this module requires.
   * Format is <Database Type, List of table creation queries.
   */
  @Override
  public String tablesFile() {
    return "signs_tables.yml";
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

  public CommentedConfiguration getFileConfiguration() {
    return fileConfiguration;
  }

  public SignsConfiguration getConfiguration() {
    return configuration;
  }
}
