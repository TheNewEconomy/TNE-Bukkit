package net.tnemc.web.rest.service;

import net.tnemc.core.TNE;
import net.tnemc.core.common.WorldManager;
import net.tnemc.core.common.account.TNEAccount;
import net.tnemc.core.common.api.IDFinder;
import net.tnemc.core.economy.currency.Currency;
import net.tnemc.web.rest.object.HoldingsObject;
import net.tnemc.web.rest.object.RestAccount;
import net.tnemc.web.rest.object.RestWorldHoldings;

import java.math.BigDecimal;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * Created by creatorfromhell on 6/14/2019.
 * <p>
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */
public class AccountService {

  //Account-related Methods
  public static boolean addAccount(String identifier, BigDecimal defaultBalance) {
    return true;
  }

  public static boolean hasAccount(String identifier) {
    return TNE.instance().api().hasAccount(identifier);
  }

  public static RestAccount getAccount(String identifier) {
    TNEAccount account = TNE.manager().getAccount(IDFinder.getID(identifier));

    RestAccount acc = new RestAccount(identifier);
    acc.setDisplay(account.displayName());
    acc.setJoined(account.getJoined());
    acc.setLanguage(account.getLanguage());
    acc.setLastOnline(account.getLastOnline());
    acc.setPlayer(account.playerAccount());

    for(WorldManager manager : TNE.instance().getWorldManagers()) {
      RestWorldHoldings worldHoldings = new RestWorldHoldings(manager.getBalanceWorld());
      for(Currency cur : manager.getCurrencies()) {
        worldHoldings.getHoldings().put(cur.name(), account.getHoldings(manager.getBalanceWorld(), cur));
      }
      acc.getHoldingsMap().put(manager.getBalanceWorld(), worldHoldings);
    }
    return acc;
  }

  public static boolean deleteAccount(String identifier) {
    return TNE.manager().deleteAccount(IDFinder.getID(identifier));
  }

  //Holdings-related Methods
  public static boolean addHoldings(String identifier, String world, String currency, BigDecimal amount) {
    if(!hasAccount(identifier)) return false;
    return TNE.instance().api().addHoldings(identifier, amount, TNE.manager().currencyManager().get(world, currency), world);
  }

  public static boolean transferHoldings(String identifier, String receiver, String world, String currency, BigDecimal amount) {
    return true;
  }

  public static HoldingsObject getHoldings(String identifier, String world, String currency) {
    return new HoldingsObject(world, currency, BigDecimal.ZERO);
  }

  public static boolean hasHoldings(String identifier, String world, String currency, BigDecimal amount) {
    return TNE.instance().api().hasHoldings(identifier, amount, TNE.manager().currencyManager().get(world, currency), world);
  }

  public static boolean removeHoldings(String identifier, String world, String currency, BigDecimal amount) {
    return TNE.instance().api().removeHoldings(identifier, amount, TNE.manager().currencyManager().get(world, currency), world);
  }
}