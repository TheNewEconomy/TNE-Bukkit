package net.tnemc.core.common.account;

import net.tnemc.core.TNE;
import net.tnemc.core.common.module.injectors.InjectMethod;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 */
public class WorldHoldings {

  private Map<String, BigDecimal> holdings = new HashMap<>();
  private String world;

  WorldHoldings(String world) {
    this.world = world;
  }

  public Map<String, BigDecimal> getHoldings() {
    return holdings;
  }

  protected BigDecimal getHoldings(String currency) {
    BigDecimal current = BigDecimal.ZERO;
    if(holdings.containsKey(currency)) {
      current = holdings.get(currency);
    }

    InjectMethod injector = new InjectMethod("WorldHoldings.getHoldings", new HashMap<>());
    injector.setParameter("currency", currency);
    injector.setParameter("holdings", current);
    TNE.loader().call(injector);

    return (BigDecimal)injector.getParameter("holdings");
  }

  protected void setHoldings(String currency, BigDecimal newHoldings) {
    TNE.debug("Currency: " + currency + " Balance: " + newHoldings.toPlainString());
    holdings.put(currency, newHoldings);
  }

  protected boolean hasHoldings(String currency) {
    return holdings.containsKey(currency);
  }
  protected boolean hasHoldings(String currency, BigDecimal amount) {
    if(hasHoldings(currency)) {
      return holdings.get(currency).compareTo(amount) > -1;
    }
    return false;
  }

  public void remove(String currency) {
    holdings.remove(currency);
  }
}