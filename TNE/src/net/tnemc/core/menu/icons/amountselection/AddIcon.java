package net.tnemc.core.menu.icons.amountselection;

import com.github.tnerevival.user.IDFinder;
import net.tnemc.core.TNE;
import net.tnemc.core.menu.icons.Icon;
import org.bukkit.Material;
import org.bukkit.entity.Player;

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
public class AddIcon extends Icon {

  private BigDecimal amount;
  private String menu;

  public AddIcon(Integer slot, Material material, BigDecimal amount, String menu) {
    super(slot, material, "Add " + amount.toPlainString());
    this.amount = amount;
    this.menu = menu;

    this.switchMenu = menu;
  }

  @Override
  public void onClick(String menu, Player player) {
    TNE.debug("=====START AddIcon.onClick =====");
    UUID id = IDFinder.getID(player);
    BigDecimal current = (TNE.menuManager().getViewerData(id, "action_amount") != null)?
        (BigDecimal)TNE.menuManager().getViewerData(id, "action_amount") :
        new BigDecimal(0.0);

    current = current.add(amount);
    TNE.menuManager().setViewerData(id, "action_amount", current);
    TNE.debug("=====END AddIcon.onClick =====");
    super.onClick(menu, player);
  }
}