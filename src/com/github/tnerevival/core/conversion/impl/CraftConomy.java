/*
 * The New Economy Minecraft Server Plugin
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.

 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.github.tnerevival.core.conversion.impl;

import com.github.tnerevival.TNE;
import com.github.tnerevival.account.IDFinder;
import com.github.tnerevival.core.conversion.Converter;
import com.github.tnerevival.core.currency.Currency;
import com.github.tnerevival.core.db.H2;
import com.github.tnerevival.core.db.MySQL;
import com.github.tnerevival.core.exception.InvalidDatabaseImport;
import com.github.tnerevival.utils.AccountUtils;
import com.github.tnerevival.utils.MISCUtils;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

/**
 * Created by creatorfromhell on 11/13/2016.
 **/
public class CraftConomy extends Converter {
  private File tneConfigFile = new File(TNE.instance.getDataFolder(), "config.yml");
  private FileConfiguration tneConfig = YamlConfiguration.loadConfiguration(tneConfigFile);

  private File configFile = new File(TNE.instance.getDataFolder(), "../Craftconomy3/config.yml");
  private FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);

  private String prefix = config.getString("System.Database.Prefix");
  private String accountTable = prefix + "account";
  private String balanceTable = prefix + "balance";
  private String currencyTable = prefix + "currency";

  @Override
  public String name() {
    return "CraftConomy";
  }

  @Override
  public void mysql() throws InvalidDatabaseImport {
    db = new MySQL(mysqlHost, mysqlPort, mysqlDatabase, mysqlUser, mysqlPassword);

    try {
      mysqlDB().executeQuery("SELECT * FROM " + currencyTable + ";");
      while (mysqlDB().results().next()) {
        String major = mysqlDB().results().getString("name");

        TNE.instance.getConfig().set("Core.Currency." + major + ".Format", "<symbol><major><decimal><minor><shorten>");
        TNE.instance.getConfig().set("Core.Currency." + major + ".Balance", 100.00);
        TNE.instance.getConfig().set("Core.Currency." + major + ".Default", false);
        TNE.instance.getConfig().set("Core.Currency." + major + ".Conversion", 1.0);
        TNE.instance.getConfig().set("Core.Currency." + major + ".Symbol", mysqlDB().results().getString("sign"));
        TNE.instance.getConfig().set("Core.Currency." + major + ".Decimal", ".");
        TNE.instance.getConfig().set("Core.Currency." + major + ".ItemCurrency", false);
        TNE.instance.getConfig().set("Core.Currency." + major + ".ItemMajor", "GOLD_INGOT");
        TNE.instance.getConfig().set("Core.Currency." + major + ".ItemMinor", "IRON_INGOT");
        TNE.instance.getConfig().set("Core.Currency." + major + ".MajorName.Single", major);
        TNE.instance.getConfig().set("Core.Currency." + major + ".MajorName.Plural", mysqlDB().results().getString("plural"));
        TNE.instance.getConfig().set("Core.Currency." + major + ".MinorName.Single", mysqlDB().results().getString("minor"));
        TNE.instance.getConfig().set("Core.Currency." + major + ".MinorName.Plural", mysqlDB().results().getString("minorplural"));
        TNE.instance.getConfig().save(tneConfigFile);
      }

      TNE.instance.manager.currencyManager.loadCurrencies();

      Map<Integer, String> ids = new HashMap<>();
      mysqlDB().executeQuery("SELECT * FROM " + accountTable + ";");
      while(mysqlDB().results().next()) {
        String id = (mysqlDB().results().getString("uuid") == null || mysqlDB().results().getString("uuid").isEmpty())? IDFinder.ecoID(mysqlDB().results().getString("name")).toString() : mysqlDB().results().getString("uuid");
        ids.put(mysqlDB().results().getInt("id"), id);
      }

      mysqlDB().executeQuery("SELECT * FROM " + balanceTable + ";");
      while (mysqlDB().results().next()) {
        if(ids.containsKey(mysqlDB().results().getInt("username_id"))) {
          String currencyName = mysqlDB().results().getString("currency_id");
          Double amount = mysqlDB().results().getDouble("balance");
          String world = (mysqlDB().results().getString("worldName").equalsIgnoreCase("default")) ? TNE.instance.defaultWorld : mysqlDB().results().getString("worldName");
          Currency currency = TNE.instance.manager.currencyManager.get(world);
          if (TNE.instance.manager.currencyManager.contains(world, currencyName)) {
            currency = TNE.instance.manager.currencyManager.get(world, currencyName);
          }
          AccountUtils.convertedAdd(ids.get(mysqlDB().results().getInt("username_id")), TNE.instance.defaultWorld, currency.getName(), amount);
        }
      }
    } catch(Exception e) {
      MISCUtils.debug(e);
      TNE.instance.getLogger().log(Level.WARNING, "Unable to load CraftConomy Data.");
    }
  }

  @Override
  public void h2() throws InvalidDatabaseImport {
    db = new H2(TNE.instance.getDataFolder() + "../CraftConomy3/database.h2.db", mysqlUser, mysqlPassword);

    try {
      h2DB().executeQuery("SELECT * FROM " + currencyTable + ";");
      while (h2DB().results().next()) {
        String major = h2DB().results().getString("name");

        TNE.instance.getConfig().set("Core.Currency." + major + ".Format", "<symbol><major><decimal><minor><shorten>");
        TNE.instance.getConfig().set("Core.Currency." + major + ".Balance", 100.00);
        TNE.instance.getConfig().set("Core.Currency." + major + ".Default", false);
        TNE.instance.getConfig().set("Core.Currency." + major + ".Conversion", 1.0);
        TNE.instance.getConfig().set("Core.Currency." + major + ".Symbol", h2DB().results().getString("sign"));
        TNE.instance.getConfig().set("Core.Currency." + major + ".Decimal", ".");
        TNE.instance.getConfig().set("Core.Currency." + major + ".ItemCurrency", false);
        TNE.instance.getConfig().set("Core.Currency." + major + ".ItemMajor", "GOLD_INGOT");
        TNE.instance.getConfig().set("Core.Currency." + major + ".ItemMinor", "IRON_INGOT");
        TNE.instance.getConfig().set("Core.Currency." + major + ".MajorName.Single", major);
        TNE.instance.getConfig().set("Core.Currency." + major + ".MajorName.Plural", h2DB().results().getString("plural"));
        TNE.instance.getConfig().set("Core.Currency." + major + ".MinorName.Single", h2DB().results().getString("minor"));
        TNE.instance.getConfig().set("Core.Currency." + major + ".MinorName.Plural", h2DB().results().getString("minorplural"));
        TNE.instance.getConfig().save(tneConfigFile);
      }

      TNE.instance.manager.currencyManager.loadCurrencies();

      Map<Integer, String> ids = new HashMap<>();
      h2DB().executeQuery("SELECT * FROM " + accountTable + ";");
      while(h2DB().results().next()) {
        String id = (h2DB().results().getString("uuid") == null)? h2DB().results().getString("name") : h2DB().results().getString("uuid");
        ids.put(h2DB().results().getInt("id"), IDFinder.getID(id).toString());
      }

      h2DB().executeQuery("SELECT * FROM " + balanceTable + ";");
      while (h2DB().results().next()) {
        if(ids.containsKey(h2DB().results().getInt("username_id"))) {
          String currencyName = h2DB().results().getString("currency_id");
          Double amount = h2DB().results().getDouble("balance");
          String world = (h2DB().results().getString("worldName").equalsIgnoreCase("default")) ? TNE.instance.defaultWorld : h2DB().results().getString("worldName");
          Currency currency = TNE.instance.manager.currencyManager.get(world);
          if (TNE.instance.manager.currencyManager.contains(world, currencyName)) {
            currency = TNE.instance.manager.currencyManager.get(world, currencyName);
          }
          AccountUtils.convertedAdd(ids.get(h2DB().results().getInt("username_id")), TNE.instance.defaultWorld, currency.getName(), amount);
        }
      }
    } catch(Exception e) {
      TNE.instance.getLogger().log(Level.WARNING, "Unable to load CraftConomy Data.");
    }
  }
}