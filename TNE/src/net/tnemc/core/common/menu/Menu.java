package net.tnemc.core.common.menu;

import net.tnemc.core.common.menu.consumable.menu.MenuClose;
import net.tnemc.core.common.menu.consumable.menu.MenuOpen;
import net.tnemc.core.common.menu.consumable.menu.page.PageSwitch;
import net.tnemc.core.common.menu.layout.Layout;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.LinkedList;
import java.util.function.Consumer;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * Created by creatorfromhell on 7/25/2021.
 * <p>
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */
public class Menu {

  //variables
  private LinkedList<Layout> layouts = new LinkedList<>();

  private String identifier;
  private String title;

  //Consumers
  private Consumer<MenuOpen> onOpen;
  private Consumer<MenuClose> onClose;

  //TODO: Should this be in Layout? (maybe)
  private Consumer<PageSwitch> onPageSwitch;

  public Inventory buildInventory(Player player) {
    return Bukkit.createInventory(null, 54);
  }
}