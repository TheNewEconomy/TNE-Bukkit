package net.tnemc.core.menu;

import net.tnemc.core.menu.impl.AmountSelectionMenu;
import net.tnemc.core.menu.impl.CurrencySelectionMenu;
import net.tnemc.core.menu.impl.DisplayMenu;
import net.tnemc.core.menu.impl.MainMenu;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.Map;
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
 * Created by Daniel on 11/5/2017.
 */
public class MenuManager {
  public Map<String, Menu> menus = new HashMap<>();
  public Map<UUID, ViewerData> data = new HashMap<>();

  private static ItemStack border;

  public MenuManager() {
    border = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte)15);
    ItemMeta setMeta = border.getItemMeta();
    setMeta.setDisplayName(ChatColor.WHITE + "'member borders?");
    border.setItemMeta(setMeta);

    menus.put("main", new MainMenu());
    menus.put("display", new DisplayMenu());
    menus.put("cur_selection_give", new CurrencySelectionMenu("cur_selection_give", "give"));
    menus.put("cur_selection_pay", new CurrencySelectionMenu("cur_selection_pay", "pay"));
    menus.put("cur_selection_set", new CurrencySelectionMenu("cur_selection_set", "set"));
    menus.put("cur_selection_take", new CurrencySelectionMenu("cur_selection_take", "take"));
    menus.put("give", new AmountSelectionMenu("give"));
    menus.put("pay", new AmountSelectionMenu("pay"));
    menus.put("set", new AmountSelectionMenu("set"));
    menus.put("take", new AmountSelectionMenu("take"));
  }

  public void open(String menu, Player player) {
    if(menus.containsKey(menu)) {
      Inventory inventory = menus.get(menu).buildInventory(player);
      InventoryHolder holder = player.getOpenInventory().getTopInventory().getHolder();
      if(player.getOpenInventory().getTopInventory().getSize() == inventory.getSize() && holder instanceof MenuHolder) {
        player.getOpenInventory().getTopInventory().setContents(inventory.getContents());
        ((MenuHolder)holder).setMenu(menu);
      } else {
        player.openInventory(menus.get(menu).buildInventory(player));
      }
    }
  }

  public void removeData(UUID id) {
    data.remove(id);
  }

  public Object getViewerData(UUID viewer, String identifier) {
    if(data.containsKey(viewer)) {
      return data.get(viewer).getValue(identifier);
    }
    return null;
  }

  public void setViewerData(UUID viewer, String identifier, Object value) {
    if(!data.containsKey(viewer)) {
      data.put(viewer, new ViewerData(viewer));
    }
    data.get(viewer).setValue(identifier, value);
  }

  public static ItemStack getBorder() {
    return border;
  }
}