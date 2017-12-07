package net.tnemc.core.common;


import net.tnemc.core.common.currency.TNECurrency;

import java.util.*;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * Created by creatorfromhell on 06/30/2017.
 */
public class WorldManager {
  private Map<String, TNECurrency> currencies = new HashMap<>();
  private Map<String, Object> configurations = new HashMap<>();
  private List<String> itemCurrencies = new ArrayList<>();
  private List<String> disabled = new ArrayList<>();

  private String world;
  private String balanceWorld = null;
  private String configurationWorld = null;

  public WorldManager(String world) {
    this.world = world;
    this.balanceWorld = world;
    this.configurationWorld = world;
  }

  public void addCurrency(TNECurrency currency) {
    if(currency.isItem()) {
      itemCurrencies.add(currency.name());
    }
    currencies.put(currency.name(), currency);
  }

  public TNECurrency getCurrency(String currency) {
    return currencies.get(currency);
  }

  public Collection<TNECurrency> getCurrencies() {
    return currencies.values();
  }

  public List<String> getItemCurrencies() {
    return itemCurrencies;
  }

  public void disable(String currency, boolean disable) {
    if(disable) disabled.add(currency);
    else disabled.remove(currency);
  }

  public boolean isEconomyDisabled() {
    return configExists("Worlds." + world + ".DisableEconomy")
           && (boolean) configurations.get("Worlds." + world + ".DisableEconomy");
  }
  public boolean isDisabled(String currency) {
    return disabled.contains(currency);
  }

  public boolean containsCurrency(String currency) {
    for(String s : currencies.keySet()) {
      if(s.equalsIgnoreCase(currency)) return true;
    }
    return false;
  }

  public void setConfiguration(String node, Object value) {
    configurations.put(node, value);
  }

  public boolean configExists(String node) {
    return configurations.containsKey(node);
  }

  public Object getConfiguration(String node) {
    return configurations.get(node);
  }

  public String getWorld() {
    return world;
  }

  public void setWorld(String world) {
    this.world = world;
  }

  public String getBalanceWorld() {
    return balanceWorld;
  }

  public void setBalanceWorld(String balanceWorld) {
    this.balanceWorld = balanceWorld;
  }

  public String getConfigurationWorld() {
    return configurationWorld;
  }

  public void setConfigurationWorld(String configurationWorld) {
    this.configurationWorld = configurationWorld;
  }
}