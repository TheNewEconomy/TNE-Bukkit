package net.tnemc.core.common;


import net.tnemc.core.TNE;
import net.tnemc.core.common.currency.TNECurrency;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
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
  private String changeFeeCurrency = "Default";
  private BigDecimal changeFee = BigDecimal.ZERO;

  public WorldManager(String world) {
    this.world = world;
    this.balanceWorld = world;
    if(!TNE.instance().mainConfigurations().getBool("Core.Multiworld")) {
      this.balanceWorld = TNE.instance().defaultWorld;
    }
    this.configurationWorld = world;
  }

  public WorldManager(String world, boolean multi) {
    this.world = world;
    this.balanceWorld = world;
    if(!multi) {
      this.balanceWorld = TNE.instance().defaultWorld;
    }
    this.configurationWorld = world;
  }

  public void addCurrency(TNECurrency currency) {
    TNE.debug("WorldManager.addCurrency: " + currency.name() + " Size: " + currencies.size());
    if(currency.isItem()) {
      itemCurrencies.add(currency.name());
    }
    currencies.put(currency.name(), currency);
    TNE.debug("WorldManager.addCurrency: " + currency.name() + " Size: " + currencies.size());
  }

  public TNECurrency getCurrency(String currency) {
    return currencies.get(currency);
  }

  public boolean hasCurrency(String currency) {
    return currencies.containsKey(currency);
  }

  public void removeCurrency(String currency) {
    currencies.remove(currency);
  }

  public Collection<TNECurrency> getCurrencies() {
    TNE.debug("WorldManager.getCurrencies: " + currencies.values().toString());
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

  public void setConfiguration(String node, String value) {
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
    if(!TNE.instance().mainConfigurations().getBool("Core.Multiworld")) {
      TNE.debug("Default World returned.");
      return TNE.instance().defaultWorld;
    }
    TNE.debug("World: " + world + " Balance World: " + balanceWorld);
    return balanceWorld;
  }

  public void setBalanceWorld(String balanceWorld) {

    if(!TNE.instance().mainConfigurations().getBool("Core.Multiworld")) {
      this.balanceWorld = TNE.instance().defaultWorld;
    }
    this.balanceWorld = balanceWorld;
  }

  public String getConfigurationWorld() {
    return configurationWorld;
  }

  public void setConfigurationWorld(String configurationWorld) {
    this.configurationWorld = configurationWorld;
  }

  public String getChangeFeeCurrency() {
    return changeFeeCurrency;
  }

  public void setChangeFeeCurrency(String changeFeeCurrency) {
    this.changeFeeCurrency = changeFeeCurrency;
  }

  public BigDecimal getChangeFee() {
    return changeFee;
  }

  public void setChangeFee(BigDecimal changeFee) {
    this.changeFee = changeFee;
  }
}