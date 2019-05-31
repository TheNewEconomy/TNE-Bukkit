package net.tnemc.signs.signs.impl.item.menu.amountselection;

import net.tnemc.core.TNE;
import net.tnemc.core.common.WorldVariant;
import net.tnemc.core.common.account.WorldFinder;
import net.tnemc.core.common.api.IDFinder;
import net.tnemc.core.common.currency.CurrencyFormatter;
import net.tnemc.core.menu.icons.Icon;
import net.tnemc.signs.SignsData;
import net.tnemc.signs.signs.impl.ItemSign;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.math.BigDecimal;
import java.sql.SQLException;
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
    super(slot, TNE.item().build("GREEN_STAINED_GLASS_PANE"), "Confirm Transaction");
  }

  @Override
  public void onClick(String menu, Player player) {
    TNE.debug("=====START Confirm.onClick =====");
    final UUID id = IDFinder.getID(player);
    final String world = WorldFinder.getWorld(player, WorldVariant.ACTUAL);
    final String currency = (String)TNE.menuManager().getViewerData(id, "action_currency");
    final BigDecimal amount = (BigDecimal)TNE.menuManager().getViewerData(id, "action_amount");
    final Location location = (Location) TNE.menuManager().getViewerData(id, "action_shop");

    try {
      ItemSign.saveItemOffer(location, null, true, amount);

      if(ItemSign.isAdmin(location)) {
        SignsData.updateStep(location, 4);
        this.message = ChatColor.WHITE + "Set currency offer to " + CurrencyFormatter.format(TNE.manager().currencyManager().get(world, currency), world, amount);
      } else {
        SignsData.updateStep(location, 3);

        this.message = ChatColor.WHITE + "Set currency offer to " + CurrencyFormatter.format(TNE.manager().currencyManager().get(world, currency), world, amount) +
            ". Now right click your shop sign, followed by a chest to mark your shop's storage.";
      }

    } catch (SQLException e) {
      this.message = ChatColor.RED + "Error while changing shop's currency offer.";
    }
    player.sendMessage(message);
    this.message = "";

    super.onClick(menu, player);
  }
}
