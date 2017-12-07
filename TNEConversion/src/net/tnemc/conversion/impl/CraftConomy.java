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
package net.tnemc.conversion.impl;

import com.github.tnerevival.core.db.sql.H2;
import com.github.tnerevival.core.db.sql.MySQL;
import com.github.tnerevival.user.IDFinder;
import net.tnemc.conversion.ConversionModule;
import net.tnemc.conversion.Converter;
import net.tnemc.conversion.InvalidDatabaseImport;
import net.tnemc.core.TNE;
import net.tnemc.core.economy.currency.Currency;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

/**
 * Created by creatorfromhell on 11/13/2016.
 **/
public class CraftConomy extends Converter {
  private File tneConfigFile = new File(TNE.instance().getDataFolder(), "config.yml");
  private FileConfiguration tneConfig = YamlConfiguration.loadConfiguration(tneConfigFile);

  private File configFile = new File("plugins/Craftconomy3/config.yml");
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
    db = new MySQL(TNE.saveManager().getTNEManager());

    try {
      int currencyIndex = mysqlDB().executeQuery("SELECT * FROM " + currencyTable + ";");
      while (mysqlDB().results(currencyIndex).next()) {
        String major = mysqlDB().results(currencyIndex).getString("name");

        TNE.instance().getConfig().set("Core.Currency." + major + ".Format", "<symbol><major><decimal><minor><shorten>");
        TNE.instance().getConfig().set("Core.Currency." + major + ".Balance", 100.00);
        TNE.instance().getConfig().set("Core.Currency." + major + ".Default", false);
        TNE.instance().getConfig().set("Core.Currency." + major + ".Conversion", 1.0);
        TNE.instance().getConfig().set("Core.Currency." + major + ".Symbol", mysqlDB().results(currencyIndex).getString("sign"));
        TNE.instance().getConfig().set("Core.Currency." + major + ".Decimal", ".");
        TNE.instance().getConfig().set("Core.Currency." + major + ".ItemCurrency", false);
        TNE.instance().getConfig().set("Core.Currency." + major + ".ItemMajor", "GOLD_INGOT");
        TNE.instance().getConfig().set("Core.Currency." + major + ".ItemMinor", "IRON_INGOT");
        TNE.instance().getConfig().set("Core.Currency." + major + ".MajorName.Single", major);
        TNE.instance().getConfig().set("Core.Currency." + major + ".MajorName.Plural", mysqlDB().results(currencyIndex).getString("plural"));
        TNE.instance().getConfig().set("Core.Currency." + major + ".MinorName.Single", mysqlDB().results(currencyIndex).getString("minor"));
        TNE.instance().getConfig().set("Core.Currency." + major + ".MinorName.Plural", mysqlDB().results(currencyIndex).getString("minorplural"));
        TNE.instance().getConfig().save(tneConfigFile);
      }

      TNE.manager().currencyManager().loadCurrencies();

      Map<Integer, String> ids = new HashMap<>();
      int accountIndex = mysqlDB().executeQuery("SELECT * FROM " + accountTable + ";");
      while(mysqlDB().results(accountIndex).next()) {
        String id = (mysqlDB().results(accountIndex).getString("uuid") == null || mysqlDB().results(accountIndex).getString("uuid").isEmpty())? IDFinder.ecoID(mysqlDB().results(accountIndex).getString("name")).toString() : mysqlDB().results(accountIndex).getString("uuid");
        ids.put(mysqlDB().results(accountIndex).getInt("id"), id);
      }

      int balanceIndex = mysqlDB().executeQuery("SELECT * FROM " + balanceTable + ";");
      while (mysqlDB().results(balanceIndex).next()) {
        if(ids.containsKey(mysqlDB().results(balanceIndex).getInt("username_id"))) {
          String currencyName = mysqlDB().results(balanceIndex).getString("currency_id");
          Double amount = mysqlDB().results(balanceIndex).getDouble("balance");
          String world = (mysqlDB().results(balanceIndex).getString("worldName").equalsIgnoreCase("default")) ? TNE.instance().defaultWorld : mysqlDB().results(balanceIndex).getString("worldName");
          Currency currency = TNE.manager().currencyManager().get(world);
          if (TNE.manager().currencyManager().contains(world, currencyName)) {
            currency = TNE.manager().currencyManager().get(world, currencyName);
          }
          ConversionModule.convertedAdd(ids.get(mysqlDB().results(balanceIndex).getInt("username_id")), TNE.instance().defaultWorld, currency.name(), new BigDecimal(amount));
        }
      }
    } catch(Exception e) {
      TNE.debug(e);
      TNE.instance().getLogger().log(Level.WARNING, "Unable to load CraftConomy Data.");
    }
  }

  @Override
  public void h2() throws InvalidDatabaseImport {
    db = new H2(TNE.saveManager().getTNEManager());

    try {
      int currencyIndex = h2DB().executeQuery("SELECT * FROM " + currencyTable + ";");
      while (h2DB().results(currencyIndex).next()) {
        String major = h2DB().results(currencyIndex).getString("name");

        TNE.instance().getConfig().set("Core.Currency." + major + ".Format", "<symbol><major><decimal><minor><shorten>");
        TNE.instance().getConfig().set("Core.Currency." + major + ".Balance", 100.00);
        TNE.instance().getConfig().set("Core.Currency." + major + ".Default", false);
        TNE.instance().getConfig().set("Core.Currency." + major + ".Conversion", 1.0);
        TNE.instance().getConfig().set("Core.Currency." + major + ".Symbol", h2DB().results(currencyIndex).getString("sign"));
        TNE.instance().getConfig().set("Core.Currency." + major + ".Decimal", ".");
        TNE.instance().getConfig().set("Core.Currency." + major + ".ItemCurrency", false);
        TNE.instance().getConfig().set("Core.Currency." + major + ".ItemMajor", "GOLD_INGOT");
        TNE.instance().getConfig().set("Core.Currency." + major + ".ItemMinor", "IRON_INGOT");
        TNE.instance().getConfig().set("Core.Currency." + major + ".MajorName.Single", major);
        TNE.instance().getConfig().set("Core.Currency." + major + ".MajorName.Plural", h2DB().results(currencyIndex).getString("plural"));
        TNE.instance().getConfig().set("Core.Currency." + major + ".MinorName.Single", h2DB().results(currencyIndex).getString("minor"));
        TNE.instance().getConfig().set("Core.Currency." + major + ".MinorName.Plural", h2DB().results(currencyIndex).getString("minorplural"));
        TNE.instance().getConfig().save(tneConfigFile);
      }

      TNE.manager().currencyManager().loadCurrencies();

      Map<Integer, String> ids = new HashMap<>();
      int accountIndex = h2DB().executeQuery("SELECT * FROM " + accountTable + ";");
      while(h2DB().results(accountIndex).next()) {
        String id = (h2DB().results(accountIndex).getString("uuid") == null)? h2DB().results(accountIndex).getString("name") : h2DB().results(accountIndex).getString("uuid");
        ids.put(h2DB().results(accountIndex).getInt("id"), IDFinder.getID(id).toString());
      }

      int balanceIndex = h2DB().executeQuery("SELECT * FROM " + balanceTable + ";");
      while (h2DB().results(balanceIndex).next()) {
        if(ids.containsKey(h2DB().results(balanceIndex).getInt("username_id"))) {
          String currencyName = h2DB().results(balanceIndex).getString("currency_id");
          Double amount = h2DB().results(balanceIndex).getDouble("balance");
          String world = (h2DB().results(balanceIndex).getString("worldName").equalsIgnoreCase("default")) ? TNE.instance().defaultWorld : h2DB().results(balanceIndex).getString("worldName");
          Currency currency = TNE.manager().currencyManager().get(world);
          if (TNE.manager().currencyManager().contains(world, currencyName)) {
            currency = TNE.manager().currencyManager().get(world, currencyName);
          }
          ConversionModule.convertedAdd(ids.get(h2DB().results(balanceIndex).getInt("username_id")), TNE.instance().defaultWorld, currency.name(), new BigDecimal(amount));
        }
      }
    } catch(Exception e) {
      TNE.instance().getLogger().log(Level.WARNING, "Unable to load CraftConomy Data.");
    }
  }
}