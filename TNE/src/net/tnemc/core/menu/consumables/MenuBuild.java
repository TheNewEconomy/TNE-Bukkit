package net.tnemc.core.menu.consumables;

import net.tnemc.core.menu.Menu;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * Created by creatorfromhell on 7/31/2021.
 * <p>
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */
public class MenuBuild {

  private Menu menu;
  private final Player player;
  private Inventory inventory;

  public MenuBuild(Menu menu, Player player, Inventory inventory) {
    this.menu = menu;
    this.player = player;
    this.inventory = inventory;
  }

  public Menu getMenu() {
    return menu;
  }

  public void setMenu(Menu menu) {
    this.menu = menu;
  }

  public Player getPlayer() {
    return player;
  }

  public Inventory getInventory() {
    return inventory;
  }

  public void setInventory(Inventory inventory) {
    this.inventory = inventory;
  }
}