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
public class DisplayIcon extends Icon {

  public DisplayIcon() {
    super(6, TNE.item().build("WHITE_STAINED_GLASS_PANE"), "Display Funds");

    data.put("action_type", "display");
    this.switchMenu = "display";
    this.node = "tne.menu.display";
  }
}