package net.tnemc.core.menu.impl.currency;

import net.tnemc.core.TNE;
import net.tnemc.core.common.currency.TNECurrency;
import net.tnemc.core.menu.Menu;
import net.tnemc.core.menu.icons.curselection.CurrencyIcon;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * Created by creatorfromhell on 7/8/2021.
 * <p>
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */
public class ListMenu extends Menu {

  public ListMenu() {
    super("currency_list", "Currency List", 6);
  }

  @Override
  public Inventory buildInventory(Player player) {

    TNE.debug("==== START ListMenu.buildInventory ====");

    int i = 0;
    for(TNECurrency currency : TNE.instance().api().getCurrencies()) {
      icons.put(i, new CurrencyIcon(currency.name(), currency.getWorlds().get(0), i, "currency_info"));
      i++;
    }

    int size = icons.size() / 9;
    if((icons.size() % 9) > 0) size++;

    if(size < 10) rows = 1;
    else rows = (size / 9);

    //TODO: Add new currency icon.

    return super.buildInventory(player);
  }
}