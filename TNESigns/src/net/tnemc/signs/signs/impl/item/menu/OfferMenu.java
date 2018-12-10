package net.tnemc.signs.signs.impl.item.menu;

import net.tnemc.core.TNE;
import net.tnemc.core.menu.Menu;
import net.tnemc.signs.signs.impl.item.menu.offer.ConfirmBuyIcon;
import net.tnemc.signs.signs.impl.item.menu.offer.ConfirmIcon;
import net.tnemc.signs.signs.impl.item.menu.offer.CostIcon;
import net.tnemc.signs.signs.impl.item.menu.offer.ItemIcon;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * Created by Daniel on 11/30/2018.
 * <p>
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */
public class OfferMenu extends Menu {
  public OfferMenu() {
    super("shop_offer_menu", ChatColor.GOLD + "[TNE]Shop Menu", 2);
  }

  @Override
  public Inventory buildInventory(Player player) {
    final Boolean selling = (Boolean) TNE.menuManager().getViewerData(player.getUniqueId(), "shop_selling");

    icons.put(4, new ItemIcon(4));
    icons.put(6, new CostIcon(6));
    if(selling) {
      icons.put(13, new ConfirmIcon(13));
    } else {
      icons.put(13, new ConfirmBuyIcon(13));
    }

    return super.buildInventory(player);
  }
}