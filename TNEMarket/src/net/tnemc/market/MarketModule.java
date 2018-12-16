package net.tnemc.market;

import net.tnemc.core.TNE;
import net.tnemc.core.commands.TNECommand;
import net.tnemc.core.common.module.Module;
import net.tnemc.core.common.module.ModuleInfo;
import net.tnemc.core.menu.Menu;
import net.tnemc.core.menu.impl.CurrencySelectionMenu;
import net.tnemc.market.command.MarketCommand;
import net.tnemc.market.menu.AmountSelectionMenu;
import net.tnemc.market.menu.MarketOfferAmountMenu;
import net.tnemc.market.menu.MarketViewMenu;
import net.tnemc.market.menu.OfferMenu;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * Created by Daniel on 12/3/2018.
 * <p>
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */
@ModuleInfo(
    name = "Market",
    author = "creatorfromhell",
    version = "0.1.0"
)
public class MarketModule extends Module {

  /**
   * Called when this @Module is loaded.
   *
   * @param tne     An instance of the main TNE class.
   * @param version The last version of this module used on this server.
   */
  @Override
  public void load(TNE tne, String version) {
    TNE.logger().info("Loaded Market Module.");
  }

  /**
   * Called when this @Module is unloaded.
   *
   * @param tne An instance of the main TNE class.
   */
  @Override
  public void unload(TNE tne) {
    TNE.logger().info("Unloaded Market Module");
  }

  /**
   * Returns a list of command instances this module uses.
   */
  @Override
  public List<TNECommand> getCommands() {
    return Collections.singletonList(new MarketCommand(TNE.instance()));
  }

  /**
   * @return Returns a list of tables that this module requires.
   * Format is <Database Type, List of table creation queries.
   */
  @Override
  public Map<String, List<String>> getTables() {
    Map<String, List<String>> tables = new HashMap<>();
    tables.put("mysql", Collections.singletonList(MarketData.OFFERS_TABLE));
    tables.put("h2", Collections.singletonList(MarketData.OFFERS_TABLE_H2));
    return tables;
  }

  @Override
  public Map<String, Menu> registerMenus(TNE pluginInstance) {
    Map<String, Menu> menus = new HashMap<>();
    menus.put("offer_currency_selection", new CurrencySelectionMenu("offer_currency_selection", "offer_cost_selection"));
    menus.put("offer_cost_selection", new AmountSelectionMenu("offer_cost_selection"));
    menus.put("market_offer_amount", new MarketOfferAmountMenu("market_offer_amount"));
    menus.put("market_view", new MarketViewMenu());
    menus.put("market_offer_menu", new OfferMenu());

    return super.registerMenus(pluginInstance);
  }
}