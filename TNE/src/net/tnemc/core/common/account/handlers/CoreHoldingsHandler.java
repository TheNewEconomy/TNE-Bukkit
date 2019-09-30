package net.tnemc.core.common.account.handlers;

import net.tnemc.core.TNE;
import net.tnemc.core.common.account.TNEAccount;
import net.tnemc.core.common.currency.TNECurrency;

import java.math.BigDecimal;
import java.sql.SQLException;
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
public class CoreHoldingsHandler implements HoldingsHandler {

  /**
   * Whether or not this is a core handler that adds to the player's inventory vs handles its own data. Don't return true
   * unless you know what you're doing as it could disrupt player balances.
   *
   * @return True if this is a core handler, otherwise false.
   */
  @Override
  public boolean coreHandler() {
    return true;
  }

  /**
   * Used for determining the order for which ExternalHoldings are called in relation to others, and the core holdings.
   * The core holdings priority = 5, making your priority > 5 will have it be called before the core holdings.
   *
   * @return The priority for this holdings handler.
   */
  @Override
  public int priority() {
    return 5;
  }

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
    BigDecimal amount = null;
    try {
      TNE.debug("Currency: " + currency);
      TNE.debug("Currency Type: " + currency.getCurrencyType());
      amount = currency.getCurrencyType().getHoldings(account, world, currency, database);
      if(amount == null) amount = BigDecimal.ZERO;
    } catch (SQLException e) {
      TNE.debug(e);
    }
    return amount;
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
    TNEAccount tneAccount = TNE.manager().getAccount(account);
    BigDecimal holdings = tneAccount.getHoldings(world, currency.name(), true, false);
    TNE.debug("Amount: " + amount.toPlainString());
    TNE.debug("Holdings: " + holdings.toPlainString());
    if(holdings.compareTo(amount) < 0) {
      TNE.debug("holdings - amount compare = < 0");
      tneAccount.setHoldings(world, currency.name(), BigDecimal.ZERO);
      return amount.subtract(holdings);
    }
    TNE.debug("Holdings: " + holdings.toPlainString());
    TNE.debug("Amount: " + amount.toPlainString());
    tneAccount.setHoldings(world, currency.name(), holdings.subtract(amount));
    return BigDecimal.ZERO;
  }
}