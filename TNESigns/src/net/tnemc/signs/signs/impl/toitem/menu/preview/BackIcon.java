package net.tnemc.signs.signs.impl.toitem.menu.preview;

import net.tnemc.core.TNE;
import net.tnemc.core.menu.icons.Icon;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * Created by Daniel on 12/2/2018.
 * <p>
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */
public class BackIcon extends Icon {

  public BackIcon(Integer slot, String menu) {
    super(slot, TNE.item().build("GREEN_STAINED_GLASS_PANE"), "Go Back");
    this.switchMenu = menu;
  }
}