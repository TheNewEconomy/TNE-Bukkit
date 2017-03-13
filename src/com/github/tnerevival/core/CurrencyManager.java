package com.github.tnerevival.core;

import com.github.tnerevival.TNE;
import com.github.tnerevival.account.IDFinder;
import com.github.tnerevival.core.currency.Currency;
import com.github.tnerevival.core.currency.Tier;
import com.github.tnerevival.utils.MISCUtils;
import org.bukkit.configuration.file.FileConfiguration;

import java.math.BigDecimal;
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
 * Created by creatorfromhell on 10/22/2016.
 */
public class CurrencyManager {
  private Map<String, Currency> currencies = new HashMap<>();
  private Set<String> worlds = TNE.instance().worldConfigurations.getConfigurationSection("Worlds").getKeys(false);

  //Cache-related maps.
  private Map<String, List<Currency>> worldCurrencies = new HashMap<>();
  private Map<String, List<Currency>> trackedCurrencies = new HashMap<>();

  public CurrencyManager() {
    loadCurrencies();
  }

  public void loadCurrencies() {
    worldCurrencies = new HashMap<>();
    trackedCurrencies = new HashMap<>();
    loadCurrency(TNE.instance().getConfig(), false, TNE.instance().defaultWorld);

    for(String s : worlds) {
      loadCurrency(TNE.instance().worldConfigurations, true, s);
    }
  }

  private void loadCurrency(FileConfiguration configuration, boolean world, String name) {
    String curBase = ((world)? "Worlds." + name : "Core") + ".Currency";
    if(configuration.contains(curBase)) {

      Set<String> currencies = configuration.getConfigurationSection(curBase).getKeys(false);
      MISCUtils.debug(currencies.toArray().toString());

      for(String cur : currencies) {
        if (configuration.contains("Core.Currency." + cur + ".Disabled") &&
            configuration.getBoolean("Core.Currency." + cur + ".Disabled")) {
              return;
        }

        MISCUtils.debug("[Loop]Loading Currency: " + cur + " for world: " + name);
        String base = curBase + "." + cur;
        BigDecimal balance = configuration.contains(base + ".Balance")?  BigDecimal.valueOf(configuration.getDouble(base + ".Balance")) : new BigDecimal(200.00);
        String decimal = configuration.contains(base + ".Decimal")? configuration.getString(base + ".Decimal") : ".";
        Integer decimalPlaces = configuration.contains(base + ".DecimalPlace")? configuration.getInt(base + ".DecimalPlace") : 2;
        String format = configuration.contains(base + ".Format")? configuration.getString(base + ".Format") : "<major> and <minor><shorten>";
        Boolean worldDefault = !configuration.contains(base + ".Default") || configuration.getBoolean(base + ".Default");
        Double rate = configuration.contains(base + ".Conversion")? configuration.getDouble(base + ".Conversion") : 1.0;
        Boolean item = configuration.contains(base + ".ItemCurrency") && configuration.getBoolean(base + ".ItemCurrency");
        Boolean track = configuration.contains(base + ".TrackChest") && configuration.getBoolean(base + ".TrackChest");
        String symbol = configuration.contains(base + ".Symbol")? configuration.getString(base + ".Symbol") : "$";
        String major = configuration.contains(base + ".MajorName.Single")? configuration.getString(base + ".MajorName.Single") : "dollar";
        String majorPlural = configuration.contains(base + ".MajorName.Plural")? configuration.getString(base + ".MajorName.Plural") : "dollars";
        String minor = configuration.contains(base + ".MinorName.Single")? configuration.getString(base + ".MinorName.Single") : "cent";
        String minorPlural = configuration.contains(base + ".MinorName.Plural")? configuration.getString(base + ".MinorName.Plural") : "cents";
        String majorItem = configuration.contains(base + ".ItemMajor")? configuration.getString(base + ".ItemMajor") : "GOLD_INGOT";
        String minorItem = configuration.contains(base + ".ItemMinor")? configuration.getString(base + ".ItemMinor") : "IRON_INGOT";

        //Interest-related configurations
        Boolean interestEnabled = !configuration.contains(base + ".Interest.Enabled") || configuration.getBoolean(base + ".Interest.Enabled");
        Double interestRate = configuration.contains(base + ".Interest.Rate")? configuration.getDouble(base + ".Interest.Rate") : 0.2;
        Long interestInterval = configuration.contains(base + ".Interest.Interval")? configuration.getLong(base + ".Interest.Interval") : 1800;


        Tier majorTier = new Tier();
        majorTier.setSymbol(symbol);
        majorTier.setMaterial(majorItem);
        majorTier.setSingle(major);
        majorTier.setPlural(majorPlural);

        Tier minorTier = new Tier();
        minorTier.setSymbol(symbol);
        minorTier.setMaterial(minorItem);
        minorTier.setSingle(minor);
        minorTier.setPlural(minorPlural);

        Currency currency = new Currency();
        currency.setBalance(balance);
        currency.setDecimal(decimal);
        currency.setDecimalPlaces(decimalPlaces);
        currency.setFormat(format);
        currency.setName(cur);
        currency.setWorldDefault(worldDefault);
        currency.setRate(rate);
        currency.setItem(item);
        currency.setTrackChest(track);
        currency.addTier("Major", majorTier);
        currency.addTier("Minor", minorTier);

        //Interest-related configurations
        currency.setInterestEnabled(interestEnabled);
        currency.setInterestRate(interestRate);
        currency.setInterestInterval(interestInterval);

        add(name, currency);
      }
    }
  }

  public void initializeWorld(String world) {
    loadCurrency(TNE.instance().worldConfigurations, true, world);

    Map<String, Currency> toAdd = new HashMap<>();
    Iterator<Map.Entry<String, Currency>> iterator = currencies.entrySet().iterator();
    while(iterator.hasNext()) {
      Map.Entry<String, Currency> entry = iterator.next();
      if(entry.getKey().contains(TNE.instance().defaultWorld + ":") || !IDFinder.getBalanceShareWorld(world).equals(world) && entry.getKey().contains(IDFinder.getBalanceShareWorld(world) + ":")) {
        if (!TNE.instance().worldConfigurations.contains("Worlds." + world + ".Currency." + entry.getValue().getName() + ".Disabled") ||
            !TNE.instance().worldConfigurations.getBoolean("Worlds." + world + ".Currency." + entry.getValue().getName() + ".Disabled")) {
          toAdd.put(world + ":" + entry.getValue().getName(), entry.getValue());
        }
      }
    }
    currencies.putAll(toAdd);
  }

  public void add(String world, Currency currency) {
    MISCUtils.debug("[Add]Loading Currency: " + currency.getName() + " for world: " + world);
    currencies.put(world + ":" + currency.getName(), currency);
    if(world.equals(TNE.instance().defaultWorld)) {
      copyToWorlds(currency);
    } else if(!IDFinder.getBalanceShareWorld(world).equals(world)) {
      copyToWorld(currency, IDFinder.getBalanceShareWorld(world));
    }
  }

  private void copyToWorlds(Currency currency) {
    if(!currencies.containsKey(TNE.instance().defaultWorld + ":" + currency.getName())) {
      if (!TNE.instance().getConfig().contains("Core.Currency." + currency.getName() + ".Disabled") ||
          !TNE.instance().getConfig().getBoolean("Core.Currency." + currency.getName() + ".Disabled")) {
            currencies.put(TNE.instance().defaultWorld + ":" + currency.getName(), currency);
      }
    }

    for(String s : worlds) {
      if(!currencies.containsKey(s + ":" + currency.getName())) {
        if (!TNE.instance().worldConfigurations.contains("Worlds." + s + ".Currency." + currency.getName() + ".Disabled") ||
            !TNE.instance().worldConfigurations.getBoolean("Worlds." + s + ".Currency." + currency.getName() + ".Disabled")) {
            currencies.put(s + ":" + currency.getName(), currency);
        }
      }
    }
  }

  public void copyToWorld(Currency currency, String world) {
    if(!currencies.containsKey(world + ":" + currency.getName())) {
      if (!TNE.instance().worldConfigurations.contains("Worlds." + world + ".Currency." + currency.getName() + ".Disabled") ||
          !TNE.instance().worldConfigurations.getBoolean("Worlds." + world + ".Currency." + currency.getName() + ".Disabled")) {
        currencies.put(world + ":" + currency.getName(), currency);
      }
    }
  }

  public Currency get(String world) {
    for(Map.Entry<String, Currency> entry : currencies.entrySet()) {
      if(entry.getKey().equalsIgnoreCase(world + ":Default") || entry.getKey().contains(world + ":") && entry.getValue().isWorldDefault()) {
        return entry.getValue();
      }
    }
    return get(TNE.instance().defaultWorld, "Default");
  }

  public Currency get(String world, String name) {
    if(contains(world, name)) {
      return currencies.get(world + ":" + name);
    } else if(contains(IDFinder.getBalanceShareWorld(world), name)) {
      return currencies.get(IDFinder.getBalanceShareWorld(world) + ":" + name);
    }
    return get(world);
  }

  public BigDecimal convert(Currency from, Currency to, BigDecimal amount) {
    double fromRate = from.getRate();
    double toRate = to.getRate();

    return convert(fromRate, toRate, amount);
  }

  public BigDecimal convert(Currency from, double toRate, BigDecimal amount) {
    return convert(from.getRate(), toRate, amount);
  }

  public BigDecimal convert(double fromRate, double toRate, BigDecimal amount) {
    double rate = fromRate - toRate;
    BigDecimal difference = amount.multiply(new BigDecimal(rate));

    return amount.add(difference);
  }

  public boolean contains(String world) {
    for(String s : currencies.keySet()) {
      if(s.contains(world + ":")) {
        return true;
      }
    }
    return false;
  }

  public List<Currency> getWorldCurrencies(String world) {
    if(worldCurrencies.containsKey(world)) return worldCurrencies.get(world);

    List<Currency> values = new ArrayList<>();

    for(String s : currencies.keySet()) {
      if(s.contains(world + ":") || s.contains(IDFinder.getBalanceShareWorld(world) + ":")) {
        values.add(currencies.get(s));
      }
    }
    if(values.size() == 0) {
      values.add(get(TNE.instance().defaultWorld));
    }

    worldCurrencies.put(world, values);
    return values;
  }

  public List<Currency> getTrackedCurrencies(String world) {
    if(trackedCurrencies.containsKey(world)) return trackedCurrencies.get(world);

    List<Currency> values = new ArrayList<>();

    for(String s : currencies.keySet()) {
      if(s.contains(world + ":") || s.contains(IDFinder.getBalanceShareWorld(world) + ":")) {

        Currency currency = currencies.get(s);
        if(currency.isItem() && currency.canTrackChest()) {
          values.add(currency);
        }
      }
    }

    if(values.size() == 0) {
      Currency defaultCur = get(TNE.instance().defaultWorld);
      if(defaultCur.isItem() && defaultCur.canTrackChest()) {
        values.add(defaultCur);
      }
    }

    trackedCurrencies.put(world, values);
    return values;
  }

  public Map<String, Currency> getCurrencies() {
    return currencies;
  }

  public boolean contains(String world, String name) {
    return currencies.containsKey(world + ":" + name);
  }
}