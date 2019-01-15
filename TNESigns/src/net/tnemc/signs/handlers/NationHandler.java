package net.tnemc.signs.handlers;

import net.tnemc.core.common.account.handlers.HoldingsHandler;
import net.tnemc.core.common.currency.ItemCalculations;
import net.tnemc.core.common.currency.TNECurrency;
import net.tnemc.signs.SignsData;
import net.tnemc.signs.signs.TNESign;
import org.bukkit.block.Chest;

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
public class NationHandler implements HoldingsHandler {
  /**
   * @return The string the username of the account holder should contain. Empty string "" for no search.
   */
  @Override
  public String userContains() {
    return "nation-";
  }

  /**
   * Used to get the holdings for a specific account from this {@link HoldingsHandler}.
   *
   * @param account  The uuid of the account.
   * @param world    The name of the world.
   * @param currency The instance of the currency to use.
   * @param database True if the holdings should be taken from the database vs the inventory, if applicable.
   * @return The holdings for the specific account in accordance to this {@link HoldingsHandler}.
   */
  @Override
  public BigDecimal getHoldings(UUID account, String world, TNECurrency currency, boolean database) {
    BigDecimal amount = BigDecimal.ZERO;

    try {
      for(TNESign sign : SignsData.loadSigns(account.toString(), "nation")) {

        if(sign.getOwner().toString().equalsIgnoreCase(account.toString())) {
          try {
            if (sign.getAttached().getBlock().getState() instanceof Chest) {
              amount = amount.add(ItemCalculations.getCurrencyItems(currency, ((Chest) sign.getAttached().getBlock().getState()).getBlockInventory()));
            }
          } catch(Exception ignore) {
            //skip this iteration
          }
        }
      }
    } catch (SQLException ignore) {
      //rip balance check
    }

    return amount;
  }

  /**
   * Used to remove holdings from this {@link HoldingsHandler}.
   *
   * @param account  The uuid of the account.
   * @param world    The name of the world.
   * @param currency The instance of the currency to use.
   * @param amount   The amount still left to remove, could be zero.
   * @return The left over holdings that's still needed to be removed after removing the account's holdings for this
   * {@link HoldingsHandler}.
   */
  @Override
  public BigDecimal removeHoldings(UUID account, String world, TNECurrency currency, BigDecimal amount) {
    BigDecimal left = amount;
    try {
      for(TNESign sign : SignsData.loadSigns(account.toString(), "nation")) {
        if(sign.getOwner().toString().equalsIgnoreCase(account.toString())) {
          if(sign.getAttached().getBlock().getState() instanceof Chest) {
            BigDecimal holdings = ItemCalculations.getCurrencyItems(currency, ((Chest)sign.getAttached().getBlock().getState()).getBlockInventory());

            if(holdings.compareTo(left) < 0) {
              ItemCalculations.clearItems(currency, ((Chest)sign.getAttached().getBlock().getState()).getBlockInventory());
              left = left.subtract(holdings);
            } else {
              ItemCalculations.setItems(currency, left, ((Chest)sign.getAttached().getBlock().getState()).getBlockInventory(), true);
              return BigDecimal.ZERO;
            }
          }
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return amount;
  }
}
