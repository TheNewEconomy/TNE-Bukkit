package net.tnemc.signs.signs.impl.item.menu;

import net.tnemc.core.menu.Menu;
import net.tnemc.signs.signs.impl.item.menu.offer.ConfirmIcon;
import net.tnemc.signs.signs.impl.item.menu.offer.CostIcon;
import net.tnemc.signs.signs.impl.item.menu.offer.ItemIcon;
import org.bukkit.ChatColor;

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

    icons.put(4, new ItemIcon(4));
    icons.put(6, new CostIcon(6));
    icons.put(13, new ConfirmIcon(13));
  }
}
