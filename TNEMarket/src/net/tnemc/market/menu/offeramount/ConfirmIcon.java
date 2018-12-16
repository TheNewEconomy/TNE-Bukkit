package net.tnemc.market.menu.offeramount;

import net.tnemc.core.TNE;
import net.tnemc.core.common.api.IDFinder;
import net.tnemc.core.menu.icons.Icon;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

/**
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by Daniel on 11/7/2017.
 */
public class ConfirmIcon extends Icon {
  public ConfirmIcon(Integer slot) {
    super(slot, Material.GREEN_STAINED_GLASS_PANE, "Confirm Transaction");
  }

  @Override
  public void onClick(String menu, Player player) {
    TNE.debug("=====START Confirm.onClick =====");
    final UUID id = IDFinder.getID(player);
    ItemStack stack = (ItemStack) TNE.menuManager().getViewerData(id, "offer_item");
    stack.setAmount((Integer) TNE.menuManager().getViewerData(id, "offer_amount"));

    this.switchMenu = "offer_currency_selection";
    super.onClick(menu, player);
  }
}
