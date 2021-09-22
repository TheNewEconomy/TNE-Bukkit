package net.tnemc.core.common.menu.layout;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * Created by creatorfromhell on 8/12/2021.
 * <p>
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */
public class Container {


  private TreeMap<Integer, Layout> layouts = new TreeMap<>();

  public void addLayout(Layout layout) {
    int order = layout.getOrder();
    if(layouts.containsKey(order)) {
      order = getNextKey(order);
    }
    layouts.put(order, layout);
  }

  public TreeMap<Integer, Layout> getLayouts() {
    return layouts;
  }

  public int getNextKey(int key) {
    for(int i = key; i < layouts.size(); i++) {
      if(!layouts.containsKey(i)) return i;
    }
    return layouts.size() + 1;
  }

  public Inventory buildInventory(String menu, Player player, String title) {

    int size = 64;

    Map<Integer, ItemStack> items = new HashMap<>();

    for(Layout layout : layouts.values()) {
      items.putAll(layout.buildIcons(menu, player));
      if(layout.getMaxSlot() > size) size = layout.getMaxSlot();
    }

    Inventory inventory = Bukkit.createInventory(null, size, title);
    for(Map.Entry<Integer, ItemStack> entry : items.entrySet()) {
      inventory.setItem(entry.getKey(), entry.getValue());
    }

    return inventory;
  }
}