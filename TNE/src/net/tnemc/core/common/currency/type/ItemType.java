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
 * Created by creatorfromhell on 8/5/2019.
 * <p>
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */
public class ItemType implements CurrencyType {
  /**
   * @return The name of this currency type. Examples: Virtual, Item
   */
  @Override
  public String name() {
    return "item";
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
    TNE.debug("======================== getHoldings ===========================");
    if(database || !TNE.manager().getAccount(account).playerAccount() || MISCUtils.getPlayer(account) == null) {
      TNE.debug("Loading Balance from Database due to player being null.");
      TNE.debug("UUID: " + account.toString());
      return TNE.saveManager().getTNEManager().getTNEProvider().loadBalance(account, world, currency.name());
    }
    TNE.debug("Loading Balance from inventory");
    return ItemCalculations.getCurrencyItems(currency, MISCUtils.getPlayer(account).getInventory());
  }

  /**
   * @param account  The UUID of the account.
   * @param world    The world to use for saving the holdings.
   * @param currency The instance of the currency to use.
   * @param amount   The amount to set the player's holdings to.
   * @param skipUpdate   Whether or not to skip updating. For item-based currency this skips updating the inventory.
   */
  @Override
  public void setHoldings(UUID account, String world, TNECurrency currency, BigDecimal amount, boolean skipUpdate) throws SQLException {
    TNE.saveManager().getTNEManager().getTNEProvider().saveBalance(account, world, currency.getIdentifier(), amount);

    if(!skipUpdate && MISCUtils.getPlayer(account) != null) {
      ItemCalculations.setItems(account, currency, amount, MISCUtils.getPlayer(account).getInventory(), false);
    }
  }
}