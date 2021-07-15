package net.tnemc.core.common.account.handlers;

import net.tnemc.core.common.currency.ItemCalculations;
import net.tnemc.core.common.currency.TNECurrency;
import net.tnemc.core.common.utils.MISCUtils;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * Created by Daniel on 6/3/2018.
 * <p>
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */
public class EChestHandler implements HoldingsHandler {
  /**
   * Used to get the holdings for a specific account from this {@link HoldingsHandler}.
   *
   * @param account  The uuid of the account.
   * @param world    The name of the world.
   * @param currency The name of the currency to use.
   * @return The holdings for the specific account in accordance to this {@link HoldingsHandler}.
   */
  @Override
  public BigDecimal getHoldings(UUID account, String world, TNECurrency currency, boolean database) {
    if(currency.canEnderChest()) {
      Player player = MISCUtils.getPlayer(account);
      if(player != null) {
        return ItemCalculations.getCurrencyItems(currency, player.getEnderChest());
      } else {
        /*final OfflinePlayer offlinePlayer = MISCUtils.getOfflinePlayer(account);
        if(offlinePlayer != null) {

        }*/
      }
    }
    return BigDecimal.ZERO;
  }

  /**
   * Used to remove holdings from this {@link HoldingsHandler}.
   *
   * @param account  The uuid of the account.
   * @param world    The name of the world.
   * @param currency The name of the currency to use.
   * @param amount The amount still left to remove, could be zero.
   * @return The left over holdings that's still needed to be removed after removing the account's holdings for this
   * {@link HoldingsHandler}.
   */
  @Override
  public BigDecimal removeHoldings(UUID account, String world, TNECurrency currency, BigDecimal amount) {
    if(currency.canEnderChest()) {
      final Player player = MISCUtils.getPlayer(account);
      if(player != null) {
        BigDecimal holdings = ItemCalculations.getCurrencyItems(currency, player.getEnderChest());
        System.out.println("Echest to: " + holdings.toPlainString());
        System.out.println("To Remove: " + amount.toPlainString());

        if(holdings.compareTo(amount) < 0) {
          ItemCalculations.clearItems(currency, player.getEnderChest());
          return amount.subtract(holdings);
        }

        System.out.println("Setting echest to: " + holdings.subtract(amount).toPlainString());
        //ItemCalculations.setItems(account, currency, holdings.subtract(amount), player.getEnderChest(), true);
        ItemCalculations.setItems(account, currency, holdings.subtract(amount), player.getEnderChest(), false);
        return BigDecimal.ZERO;
      } else {
        /*final OfflinePlayer offlinePlayer = MISCUtils.getOfflinePlayer(account);
        if(offlinePlayer != null) {

        }*/
      }
    }
    return amount;
  }
}
