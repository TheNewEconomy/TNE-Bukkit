package net.tnemc.signs.signs.impl.item.menu.itemselection;

import net.tnemc.core.TNE;
import net.tnemc.core.common.api.IDFinder;
import net.tnemc.core.menu.icons.Icon;
import org.bukkit.entity.Player;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * Created by Daniel on 12/7/2018.
 * <p>
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */
public class ResetIcon extends Icon {

  private final String data;

  public ResetIcon(Integer slot, final String menu, final String data) {
    super(slot, TNE.item().build("WHITE_STAINED_GLASS_PANE"), "Reset Amount");

    this.data = data;
    this.switchMenu = menu;
  }

  @Override
  public void onClick(String menu, Player player) {
    TNE.menuManager().setViewerData(IDFinder.getID(player), data, 0);
    super.onClick(menu, player);
  }
}