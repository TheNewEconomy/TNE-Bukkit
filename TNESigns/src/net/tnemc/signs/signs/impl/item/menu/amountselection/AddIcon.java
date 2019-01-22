package net.tnemc.signs.signs.impl.item.menu.amountselection;

import net.tnemc.core.TNE;
import net.tnemc.core.common.api.IDFinder;
import net.tnemc.core.menu.icons.Icon;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by Daniel on 11/7/2017.
 */
public class AddIcon extends Icon {

  private BigDecimal amount;
  private String menu;

  public AddIcon(Integer slot, Material material, BigDecimal amount, String menu) {
    super(slot, material, "Add " + amount.toPlainString());
    this.amount = amount;
    this.menu = menu;

    if(amount.compareTo(BigDecimal.ZERO) < 0) {
      this.display = "Subtract " + amount.toPlainString();
    }

    this.switchMenu = menu;
  }

  @Override
  public void onClick(String menu, Player player) {
    TNE.debug("=====START AddIcon.onClick =====");
    UUID id = IDFinder.getID(player);
    BigDecimal current = (TNE.menuManager().getViewerData(id, "action_amount") != null)?
        (BigDecimal)TNE.menuManager().getViewerData(id, "action_amount") :
        BigDecimal.ZERO;

    if(current.add(amount).compareTo(BigDecimal.ZERO) >= 0) {
      current = current.add(amount);
    }
    TNE.menuManager().setViewerData(id, "action_amount", current);
    TNE.debug("=====END AddIcon.onClick =====");
    super.onClick(menu, player);
  }
}