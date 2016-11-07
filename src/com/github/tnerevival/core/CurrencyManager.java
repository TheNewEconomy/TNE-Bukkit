package com.github.tnerevival.core;

import com.github.tnerevival.TNE;
import com.github.tnerevival.core.currency.Currency;
import com.github.tnerevival.core.currency.Tier;
import com.github.tnerevival.utils.MISCUtils;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

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

  public CurrencyManager() {
    loadCurrencies();
  }

  public void loadCurrencies() {
    loadCurrency(TNE.instance.getConfig(), false, MISCUtils.getWorld(TNE.instance.defaultWorld));

    for(World w : Bukkit.getWorlds()) {
      loadCurrency(TNE.instance.worldConfigurations, true, MISCUtils.getWorld(w.getName()));
    }
  }

  private void loadCurrency(FileConfiguration configuration, boolean world, String name) {
    String curBase = ((world)? "Worlds." + name : "Core") + ".Currency";
    if(configuration.contains(curBase)) {

      Set<String> currencies = configuration.getConfigurationSection(curBase).getKeys(false);

      for(String cur : currencies) {
        String base = curBase + "." + cur;
        String decimal = configuration.contains(base + ".Decimal")? configuration.getString(base + ".Decimal") : ".";
        String format = configuration.contains(base + ".Format")? configuration.getString(base + ".Format") : "<major> and <minor><shorten=false>";
        Boolean worldDefault = !configuration.contains(base + ".Default") || configuration.getBoolean(base + ".Default");
        Double rate = configuration.contains(base + ".Conversion")? configuration.getDouble(base + ".Conversion") : 1.0;
        Boolean item = configuration.contains(base + ".ItemCurrency") && configuration.getBoolean(base + ".ItemCurrency");
        String symbol = configuration.contains(base + ".Symbol")? configuration.getString(base + ".Symbol") : "$";
        String major = configuration.contains(base + ".MajorName.Single")? configuration.getString(base + ".MajorName.Single") : "dollar";
        String majorPlural = configuration.contains(base + ".MajorName.Plural")? configuration.getString(base + ".MajorName.Plural") : "dollars";
        String minor = configuration.contains(base + ".MinorName.Single")? configuration.getString(base + ".MinorName.Single") : "cent";
        String minorPlural = configuration.contains(base + ".MinorName.Plural")? configuration.getString(base + ".MinorName.Plural") : "cents";
        String majorItem = configuration.contains(base + ".ItemMajor")? configuration.getString(base + ".ItemMajor") : "GOLD_INGOT";
        String minorItem = configuration.contains(base + ".ItemMinor")? configuration.getString(base + ".ItemMinor") : "IRON_INGOT";


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
        currency.setDecimal(decimal);
        currency.setFormat(format);
        currency.setName(cur);
        currency.setWorldDefault(worldDefault);
        currency.setRate(rate);
        currency.setItem(item);
        currency.addTier("Major", majorTier);
        currency.addTier("Minor", minorTier);

        add(name, currency);
      }
    }
  }

  public void add(String world, Currency currency) {
    this.currencies.put(world + ":" + currency.getName(), currency);
  }

  public Currency get(String world) {
    for(Map.Entry<String, Currency> entry : currencies.entrySet()) {
      if(entry.getKey().equalsIgnoreCase(world + ":Default") || entry.getKey().contains(world + ":") && entry.getValue().isWorldDefault()) {
        return entry.getValue();
      }
    }
    return null;
  }

  public Currency get(String world, String name) {
    if(contains(world, name)) {
      return currencies.get(world + ":" + name);
    }
    return get(world);
  }

  public boolean contains(String world) {
    for(String s : currencies.keySet()) {
      if(s.contains(world + ":")) {
        return true;
      }
    }
    return false;
  }

  public boolean contains(String world, String major) {
    return currencies.containsKey(world + ":" + major);
  }
}