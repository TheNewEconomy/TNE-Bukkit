package net.tnemc.core.menu.impl;

import com.github.tnerevival.user.IDFinder;
import net.tnemc.core.TNE;
import net.tnemc.core.menu.Menu;
import net.tnemc.core.menu.icons.Icon;
import net.tnemc.core.menu.icons.amountselection.AddIcon;
import net.tnemc.core.menu.icons.amountselection.CancelIcon;
import net.tnemc.core.menu.icons.amountselection.ConfirmIcon;
import net.tnemc.core.menu.icons.amountselection.ResetIcon;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.math.BigDecimal;
import java.util.UUID;

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
 * Created by Daniel on 11/7/2017.
 */
public class AmountSelectionMenu extends Menu {
  public AmountSelectionMenu(String name) {
    super(name, "[TNE]Action", 5);
  }

  @Override
  public Inventory buildInventory(Player player) {
    UUID viewer = IDFinder.getID(player);
    String world = (String) TNE.menuManager().getViewerData(viewer, "action_world");
    UUID playerName = (UUID)TNE.menuManager().getViewerData(viewer, "action_player");
    String currency = (String)TNE.menuManager().getViewerData(viewer, "action_currency");

    if(TNE.menuManager().getViewerData(viewer, "action_amount") == null) {
      TNE.menuManager().setViewerData(viewer, "action_amount", BigDecimal.ZERO);
    }

    BigDecimal amount = (BigDecimal)TNE.menuManager().getViewerData(viewer, "action_amount");

    icons.put(0, new Icon(0, Material.SKULL_ITEM, ChatColor.stripColor(IDFinder.getUsername(playerName.toString())), (short)3));
    icons.put(1, new Icon(1, Material.PAPER, "World: " + world));
    icons.put(4, new Icon(4, Material.PAPER, "Amount: " + amount.toPlainString()));
    icons.put(7, new Icon(7, Material.PAPER, "Currency: " + currency));
    icons.put(8, new Icon(8, Material.SKULL_ITEM, ChatColor.stripColor(IDFinder.getUsername(playerName.toString())), (short)3));

    //Major Icons
    icons.put(18, new AddIcon(18, Material.GOLD_BLOCK, new BigDecimal("100"), getName()));
    icons.put(19, new AddIcon(19, Material.GOLD_PLATE, new BigDecimal("20"), getName()));
    icons.put(20, new AddIcon(20, Material.GOLD_INGOT, new BigDecimal("5"), getName()));
    icons.put(21, new AddIcon(21, Material.GOLD_NUGGET, BigDecimal.ONE, getName()));

    //Minor Icons
    icons.put(23, new AddIcon(23, Material.IRON_NUGGET, new BigDecimal(".01"), getName()));
    icons.put(24, new AddIcon(24, Material.IRON_INGOT, new BigDecimal(".10"), getName()));
    icons.put(25, new AddIcon(25, Material.IRON_PLATE, new BigDecimal(".25"), getName()));
    icons.put(26, new AddIcon(26, Material.IRON_BLOCK, new BigDecimal(".50"), getName()));

    //Control Icons
    icons.put(36, new CancelIcon(36));
    icons.put(40, new ResetIcon(40, getName()));
    icons.put(44, new ConfirmIcon(44));

    return super.buildInventory(player);
  }
}