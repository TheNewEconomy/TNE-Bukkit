package net.tnemc.core.menu.impl;

import com.github.tnerevival.user.IDFinder;
import net.tnemc.core.TNE;
import net.tnemc.core.common.account.WorldFinder;
import net.tnemc.core.common.currency.TNECurrency;
import net.tnemc.core.menu.Menu;
import net.tnemc.core.menu.icons.curselection.CurrencyIcon;
import net.tnemc.core.menu.icons.shared.BackIcon;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * Created by Daniel on 11/5/2017.
 */
public class CurrencySelectionMenu extends Menu {

  private String switchMenu;

  public CurrencySelectionMenu(String name, String switchMenu) {
    super(name, "[TNE]Action", 1);
    icons.put(0, new BackIcon("main", 0));

    this.switchMenu = switchMenu;
  }

  @Override
  public Inventory buildInventory(Player player) {
    TNE.debug("=====START DisplayScreen.initializeCurrencies =====");
    String world = (String)TNE.menuManager().getViewerData(IDFinder.getID(player), "action_world");

    TNE.debug("Player: " + player);
    TNE.debug("World: " + world);
    if(world == null) world = WorldFinder.getWorld(player);
    int i = 1;
    for(TNECurrency currency : TNE.instance().api().getCurrencies(world)) {
      icons.put(i, new CurrencyIcon(currency.name(), i, switchMenu));
      i++;
    }

    Integer size = icons.size() / 9;
    if((icons.size() % 9) > 0) size++;
    if(size < 10) rows = 1;
    else rows = (size / 9);

    return super.buildInventory(player);
  }
}