package net.tnemc.market.menu;

import net.tnemc.core.TNE;
import net.tnemc.core.common.api.IDFinder;
import net.tnemc.core.menu.Menu;
import net.tnemc.core.menu.icons.Icon;
import net.tnemc.market.menu.offeramount.AddIcon;
import net.tnemc.market.menu.offeramount.CancelIcon;
import net.tnemc.market.menu.offeramount.ConfirmIcon;
import net.tnemc.market.menu.offeramount.ResetIcon;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.UUID;

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
public class MarketOfferAmountMenu extends Menu {
  public MarketOfferAmountMenu(String name) {
    super(name, "Market Offer Amount", 5);
  }

  @Override
  public Inventory buildInventory(Player player) {
    final UUID viewer = IDFinder.getID(player);

    if (TNE.menuManager().getViewerData(viewer, "offer_amount") == null) {
      TNE.menuManager().setViewerData(viewer, "offer_amount", 0);
    }

    Integer amount = (Integer) TNE.menuManager().getViewerData(viewer, "offer_amount");

    icons.put(0, new Icon(0, Material.PLAYER_HEAD, ChatColor.stripColor(player.getDisplayName())));
    icons.put(4, new Icon(4, Material.PAPER, "Amount: " + amount));
    icons.put(8, new Icon(8, Material.PLAYER_HEAD, ChatColor.stripColor(player.getDisplayName())));

    //Major Icons
    icons.put(21, new AddIcon(21, Material.GOLD_BLOCK, 64, getName()));
    icons.put(22, new AddIcon(22, Material.HEAVY_WEIGHTED_PRESSURE_PLATE, 32, getName()));
    icons.put(23, new AddIcon(23, Material.LIGHT_WEIGHTED_PRESSURE_PLATE, 10, getName()));
    icons.put(24, new AddIcon(24, Material.GOLD_INGOT, 5, getName()));
    icons.put(25, new AddIcon(25, Material.IRON_INGOT, 1, getName()));

    //Control Icons
    icons.put(36, new CancelIcon(36));
    icons.put(40, new ResetIcon(40, getName()));
    icons.put(44, new ConfirmIcon(44));

    return super.buildInventory(player);
  }
}
