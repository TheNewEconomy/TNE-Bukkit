package net.tnemc.core.menu.impl.shared;

import net.tnemc.core.menu.Menu;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

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
public class PlayerSelectionMenu extends Menu {

  public PlayerSelectionMenu(String name) {
    super(name, ChatColor.GOLD + "Select a Player", 1);
  }

  @Override
  public Inventory buildInventory(Player player) {
    return super.buildInventory(player);
  }
}