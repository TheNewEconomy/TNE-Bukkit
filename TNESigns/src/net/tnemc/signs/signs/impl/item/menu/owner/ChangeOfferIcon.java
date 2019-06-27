package net.tnemc.signs.signs.impl.item.menu.owner;

import net.tnemc.core.TNE;
import net.tnemc.core.menu.icons.Icon;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * Created by creatorfromhell on 6/27/2019.
 * <p>
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */
public class ChangeOfferIcon extends Icon {

  public ChangeOfferIcon(int slot) {
    super(slot, TNE.item().build("GREEN_STAINED_GLASS_PANE"), "Change Offer");

    this.switchMenu = "cur_selection_give";
  }
}