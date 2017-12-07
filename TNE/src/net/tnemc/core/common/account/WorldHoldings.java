package net.tnemc.core.common.account;

import net.tnemc.core.TNE;
import net.tnemc.core.common.module.injectors.InjectMethod;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by creatorfromhell on 7/2/2017.
 * All rights reserved.
 **/
public class WorldHoldings {

  private Map<String, BigDecimal> holdings = new HashMap<>();
  private String world;

  public WorldHoldings(String world) {
    this.world = world;
  }

  public Map<String, BigDecimal> getHoldings() {
    return holdings;
  }

  public BigDecimal getHoldings(String currency) {
    BigDecimal current = new BigDecimal(0.0);
    if(holdings.containsKey(currency)) {
      return holdings.get(currency);
    }

    InjectMethod injector = new InjectMethod("WorldHoldings.getHoldings", new HashMap<>());
    injector.setParameter("currency", currency);
    injector.setParameter("holdings", current);
    TNE.instance().loader().call(injector);

    return (BigDecimal)injector.getParameter("holdings");
  }

  public void setHoldings(String currency, BigDecimal newHoldings) {
    holdings.put(currency, newHoldings);
  }

  public boolean hasHoldings(String currency) {
    return holdings.containsKey(currency);
  }
  public boolean hasHoldings(String currency, BigDecimal amount) {
    if(hasHoldings(currency)) {
      return holdings.get(currency).compareTo(amount) > -1;
    }
    return false;
  }
}