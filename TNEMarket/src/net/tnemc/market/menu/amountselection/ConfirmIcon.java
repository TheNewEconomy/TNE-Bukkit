package net.tnemc.market.menu.amountselection;

import net.tnemc.core.TNE;
import net.tnemc.core.common.WorldVariant;
import net.tnemc.core.common.account.WorldFinder;
import net.tnemc.core.common.api.IDFinder;
import net.tnemc.core.common.currency.ItemCalculations;
import net.tnemc.core.menu.icons.Icon;
import net.tnemc.market.MarketData;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

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
    super(slot, Material.GREEN_STAINED_GLASS_PANE, "Confirm");
    this.close = true;
  }

  @Override
  public void onClick(String menu, Player player) {
    TNE.debug("=====START Confirm.onClick =====");
    final UUID id = IDFinder.getID(player);
    final ItemStack item = (ItemStack)TNE.menuManager().getViewerData(id, "offer_item");
    final String world = WorldFinder.getWorld(player, WorldVariant.ACTUAL);
    final String currency = (String)TNE.menuManager().getViewerData(id, "action_currency");
    final BigDecimal amount = (BigDecimal)TNE.menuManager().getViewerData(id, "action_amount");

    try {

      if(ItemCalculations.getCount(item, player.getInventory()) >= item.getAmount()) {
        MarketData.addOffer(MarketData.freeID(), id, item, currency, amount, world);
        ItemCalculations.removeItem(item, player.getInventory());
        player.sendMessage(ChatColor.WHITE + "Your item has been added to the marketplace.");
      } else {
        player.sendMessage(ChatColor.RED + "You do not have enough of that item.");
      }

    } catch (SQLException e) {
      this.message = ChatColor.RED + "Error while adding your offer to the marketplace.";
    }
    super.onClick(menu, player);
  }
}
