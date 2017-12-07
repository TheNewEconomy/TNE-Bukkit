package net.tnemc.core.menu;

import net.tnemc.core.TNE;
import net.tnemc.core.menu.icons.Icon;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;
import java.util.Map;

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
public class Menu {
  public Map<Integer, Icon> icons = new HashMap<>();

  private String name;
  private String title;
  protected int rows;

  public Menu(String name, String title, Integer rows) {
    this.name = name;
    this.title = title;
    this.rows = rows;
  }

  public Icon getIcon(int slot) {
    return icons.get(slot);
  }

  public Inventory buildInventory(Player player) {
    Inventory inventory = Bukkit.createInventory(new MenuHolder(getName()), rows * 9, title);
    icons.values().forEach(icon->{
      TNE.debug("Icon Permission Node is: " + icon.getNode());
      TNE.debug("Player Has? " + player.hasPermission(icon.getNode()));
      if(!icon.getNode().equalsIgnoreCase("") && player.hasPermission(icon.getNode())
         || icon.getNode().equalsIgnoreCase("")) {
        inventory.setItem(icon.getSlot(), icon.buildStack(player));
      }
    });

    for(int i = 0; i < inventory.getSize(); i++) {
      if(inventory.getItem(i) == null || inventory.getItem(i).getType().equals(Material.AIR)) {
        inventory.setItem(i, MenuManager.getBorder());
      }
    }
    return inventory;
  }

  public void click(Player player, int slot) {
    if(icons.containsKey(slot) && icons.get(slot).canClick(player)) {
      icons.get(slot).onClick(getName(), player);
    }
  }

  public Menu copy() {
    Menu menu = new Menu(name, title, rows);
    menu.setIcons(getIcons());
    return menu;
  }

  public Map<Integer, Icon> getIcons() {
    return icons;
  }

  public void setIcons(Map<Integer, Icon> icons) {
    this.icons = icons;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public int getRows() {
    return rows;
  }

  public void setRows(int rows) {
    this.rows = rows;
  }
}