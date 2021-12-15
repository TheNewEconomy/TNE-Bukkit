package net.tnemc.core.common.currency.type;

import net.tnemc.core.TNE;
import net.tnemc.core.common.currency.CurrencyType;
import net.tnemc.core.common.currency.ItemCalculations;
import net.tnemc.core.common.currency.TNECurrency;
import net.tnemc.core.common.utils.MISCUtils;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.UUID;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * Created by creatorfromhell on 12/15/2021.
 * <p>
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */
public class MixedType implements CurrencyType {
  /**
   * @return The name of this currency type. Examples: Virtual, Item
   */
  @Override
  public String name() {
    return "virtual";
  }

  /**
   * @return True if this currency requires setting the player's balance when they log in. This should be true for currency
   * types like item currency.
   */
  @Override
  public boolean loginCalculation() {
    return false;
  }

  /**
   * Used to get the holdings for a specific account from this currency type.
   *
   * @param account  The uuid of the account.
   * @param world    The name of the world.
   * @param currency The instance of the currency to use.
   * @param database True if the holdings should be taken from the database vs the inventory, if applicable.
   * @return The holdings for the specific account.
   */
  @Override
  public BigDecimal getHoldings(UUID account, String world, TNECurrency currency, boolean database) throws SQLException {
    return virtualHoldings(account, world, currency).add(itemHoldings(account, world, currency, database));
  }

  public BigDecimal virtualHoldings(UUID account, String world, TNECurrency currency) {
    return TNE.manager().getBalance(account, world, currency.getIdentifier());
  }

  public BigDecimal itemHoldings(UUID account, String world, TNECurrency currency, boolean database) {
    if(database || !TNE.manager().getAccount(account).playerAccount() || MISCUtils.getPlayer(account) == null) {
      return BigDecimal.ZERO;
    }
    return ItemCalculations.getCurrencyItems(currency, MISCUtils.getPlayer(account).getInventory());
  }

  /**
   * @param account
   * @param world    The world to use for saving the holdings.
   * @param currency The instance of the currency to use.
   * @param amount   The amount to set the player's holdings to.
   * @param skipUpdate   Whether or not to skip updating. For item-based currency this skips updating the inventory.
   */
  @Override
  public void setHoldings(UUID account, String world, TNECurrency currency, BigDecimal amount, boolean skipUpdate) throws SQLException {
    final BigDecimal holdings = getHoldings(account, world, currency, false);

    if(holdings.compareTo(BigDecimal.ZERO) != 0 && holdings.compareTo(amount) > 0) {
      final BigDecimal virtualHoldings = virtualHoldings(account, world, currency);

      BigDecimal toSet = amount;

      if(virtualHoldings.compareTo(amount) < 0) {
        final BigDecimal remaining = amount.subtract(virtualHoldings);

        toSet = BigDecimal.ZERO;

        setItemHoldings(account, world, currency, itemHoldings(account, world, currency, false).subtract(remaining));
      }

      setVirtualHoldings(account, world, currency, toSet, skipUpdate);

    } else if(holdings.compareTo(BigDecimal.ZERO) == 0) {
      setVirtualHoldings(account, world, currency, amount, skipUpdate);
      setItemHoldings(account, world, currency, amount);
    } else {
      setVirtualHoldings(account, world, currency, amount, skipUpdate);
    }
  }

  public void setVirtualHoldings(UUID account, String world, TNECurrency currency, BigDecimal amount, boolean skipUpdate) {
    TNE.manager().setBalance(account, world, currency.getIdentifier(), amount, !skipUpdate);
  }

  public void setItemHoldings(UUID account, String world, TNECurrency currency, BigDecimal amount) {
    if(MISCUtils.getPlayer(account) != null) {
      ItemCalculations.setItems(account, currency, amount, MISCUtils.getPlayer(account).getInventory(), false);
    }
  }
}