package net.tnemc.core.menu;

import net.tnemc.core.TNE;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

/**
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
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