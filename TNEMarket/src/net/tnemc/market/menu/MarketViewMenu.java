package net.tnemc.market.menu;

import net.tnemc.core.TNE;
import net.tnemc.core.menu.Menu;
import net.tnemc.market.MarketData;
import net.tnemc.market.MarketEntry;
import net.tnemc.market.menu.market.PageIcon;
import net.tnemc.market.menu.market.ViewIcon;
import net.tnemc.market.menu.offeramount.CancelIcon;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.List;

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
public class MarketViewMenu extends Menu {
  public MarketViewMenu() {
    super("market_view", "Marketplace", 6);
  }

  @Override
  public Inventory buildInventory(Player player) {

    int page = (Integer) TNE.menuManager().getViewerData(player.getUniqueId(), "market_page");
    final String balWorld = TNE.instance().getWorldManager(player.getWorld().getName()).getBalanceWorld();
    final int pages = maxPages(balWorld);
    if(page > pages) {
      page = 1;
    }

    if(page < 1) page = pages;

    final List<MarketEntry> items = MarketData.getItems(balWorld, page);

    int slot = 0;
    for(MarketEntry entry : items) {
      icons.put(slot, new ViewIcon(slot, entry.getStack(), entry.getId()));
      slot++;
    }
    icons.put(49, new PageIcon(49, Material.RED_STAINED_GLASS_PANE, "Previous Page", -1));
    icons.put(50, new CancelIcon(50));
    icons.put(51, new PageIcon(51, Material.GREEN_STAINED_GLASS_PANE, "Next Page", 1));
    return super.buildInventory(player);
  }

  private int maxPages(String world) {
      return MarketData.offerCount(world);
  }
}