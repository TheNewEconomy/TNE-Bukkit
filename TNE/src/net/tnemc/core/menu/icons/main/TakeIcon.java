package net.tnemc.core.menu.icons.main;

import net.tnemc.core.TNE;
import net.tnemc.core.menu.icons.Icon;

/**
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by Daniel on 11/5/2017.
 */
public class TakeIcon extends Icon {

  public TakeIcon() {
    super(4, TNE.item().build("RED_STAINED_GLASS_PANE"), "Take Funds");

    data.put("action_type", "take");
    this.switchMenu = "cur_selection_take";
    this.node = "tne.menu.take";
  }
}