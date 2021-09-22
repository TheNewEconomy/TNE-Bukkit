package net.tnemc.core.menu;

import net.tnemc.core.TNE;
import net.tnemc.core.menu.consumables.MenuBuild;
import net.tnemc.core.menu.consumables.MenuClick;
import net.tnemc.core.menu.icons.Icon;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by Daniel on 11/5/2017.
 */
public class Menu {
  public Map<Integer, Icon> icons = new HashMap<>();

  private String name;
  private String title;
  protected int rows;

  private Consumer<MenuBuild> onBuild;
  private Consumer<MenuClick> onClick;

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

    if(onBuild != null) {
      onBuild.accept(new MenuBuild(this, player, inventory));
    }

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

  public void click(Player player, int slot, ClickType type) {
    if(onClick != null) {
      onClick.accept(new MenuClick(this, player, slot, Optional.of(icons.get(slot))));
    }

    if(icons.containsKey(slot) && icons.get(slot).canClick(player)) {
      icons.get(slot).onClick(getName(), player, type);
    }
  }

  public Menu copy() {
    Menu menu = new Menu(name, title, rows);
    menu.setIcons(getIcons());
    return menu;
  }

  public void setOnBuild(Consumer<MenuBuild> onBuild) {
    this.onBuild = onBuild;
  }

  public void setOnClick(Consumer<MenuClick> onClick) {
    this.onClick = onClick;
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