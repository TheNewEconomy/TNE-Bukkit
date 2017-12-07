package net.tnemc.core.menu;

import net.tnemc.core.TNE;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

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
public class MenuHolder implements InventoryHolder {

  private String menu;

  public MenuHolder(String menu) {
    this.menu = menu;
  }

  @Override
  public Inventory getInventory() {
    return null;
  }

  public String getMenu() {
    return menu;
  }

  public Menu getMenuInstance() {
    return TNE.menuManager().menus.get(menu);
  }

  public void setMenu(String menu) {
    this.menu = menu;
  }
}