package net.tnemc.core.menu.impl.balance;

import net.tnemc.core.TNE;
import net.tnemc.core.common.WorldVariant;
import net.tnemc.core.common.account.TNEAccount;
import net.tnemc.core.common.account.WorldFinder;
import net.tnemc.core.common.api.IDFinder;
import net.tnemc.core.common.currency.TNECurrency;
import net.tnemc.core.common.currency.formatter.CurrencyFormatter;
import net.tnemc.core.menu.Menu;
import net.tnemc.core.menu.icons.balance.BalCurrencyIcon;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * Created by creatorfromhell on 8/11/2021.
 * <p>
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */
public class BalanceMenu extends Menu {

  private String switchMenu;

  public BalanceMenu() {
    super("balance_menu", ChatColor.GOLD + "Your Money", 1);
  }

  @Override
  public Inventory buildInventory(Player player) {
    TNE.debug("=====START DisplayScreen.initializeCurrencies =====");
    String world = (String)TNE.menuManager().getViewerData(IDFinder.getID(player), "action_world");

    final TNEAccount account = TNE.manager().getAccount(player.getUniqueId());

    TNE.debug("Player: " + player);
    TNE.debug("World: " + world);
    if(world == null) world = WorldFinder.getWorld(player, WorldVariant.ACTUAL);
    int i = 1;
    for(TNECurrency currency : TNE.instance().api().getCurrencies(world)) {
      icons.put(i, new BalCurrencyIcon(currency, CurrencyFormatter.format(currency, world, account.getHoldings(world, currency), ""), world, i));
      i++;
    }

    Integer size = icons.size() / 9;
    if((icons.size() % 9) > 0) size++;
    if(size < 10) rows = 1;
    else rows = (size / 9);

    return super.buildInventory(player);
  }
}