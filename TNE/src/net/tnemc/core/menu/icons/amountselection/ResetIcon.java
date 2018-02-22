package net.tnemc.core.menu.icons.amountselection;

import com.github.tnerevival.user.IDFinder;
import net.tnemc.core.TNE;
import net.tnemc.core.menu.icons.Icon;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.math.BigDecimal;

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
public class ResetIcon extends Icon {
  public ResetIcon(Integer slot, String menu) {
    super(slot, Material.STAINED_GLASS_PANE, "ResetAmount", (byte)0);

    this.switchMenu = menu;
  }

  @Override
  public void onClick(String menu, Player player) {
    TNE.menuManager().setViewerData(IDFinder.getID(player), "action_amount", BigDecimal.ZERO);
    super.onClick(menu, player);
  }
}