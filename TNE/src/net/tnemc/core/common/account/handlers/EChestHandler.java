package net.tnemc.core.common.account.handlers;

import net.tnemc.core.common.currency.TNECurrency;
import net.tnemc.core.common.currency.calculations.PlayerCurrencyData;
import org.bukkit.Bukkit;
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
      Player player = Bukkit.getPlayer(account);
      if(player != null) {
        return currency.calculation().calculateHoldings(new PlayerCurrencyData(player.getEnderChest(), currency));
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
      final Player player = Bukkit.getPlayer(account);
      if(player != null) {
        final PlayerCurrencyData data = new PlayerCurrencyData(player.getEnderChest(), currency);
        final BigDecimal holdings = currency.calculation().calculateHoldings(data);

        if(holdings.compareTo(amount) < 0) {
          currency.calculation().clearItems(data);
          return amount.subtract(holdings);
        }
        final BigDecimal newHoldings = holdings.subtract(amount);
        System.out.println("Amount:" + amount.toPlainString());
        System.out.println("Holdings:" + holdings.toPlainString());
        System.out.println("Holdings:" + newHoldings.toPlainString());
        System.out.println("Holdings Sub:" + holdings.subtract(amount).toPlainString());
        currency.calculation().setItems(data, newHoldings);

        return BigDecimal.ZERO;
      }
    }
    return amount;
  }
}