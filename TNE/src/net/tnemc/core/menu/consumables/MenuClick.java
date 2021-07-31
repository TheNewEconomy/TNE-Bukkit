package net.tnemc.core.menu.consumables;

import net.tnemc.core.menu.Menu;
import net.tnemc.core.menu.icons.Icon;
import org.bukkit.entity.Player;

import java.util.Optional;

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
public class MenuClick {

  private Menu menu;
  private final Player player;
  private final int slot;
  private final Optional<Icon> icon;

  public MenuClick(Menu menu, Player player, int slot, Optional<Icon> icon) {
    this.menu = menu;
    this.player = player;
    this.slot = slot;
    this.icon = icon;
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

  public int getSlot() {
    return slot;
  }

  public Optional<Icon> getIcon() {
    return icon;
  }
}