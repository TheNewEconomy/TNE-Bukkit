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

import com.github.tnerevival.core.db.sql.MySQL;
import net.tnemc.conversion.ConversionModule;
import net.tnemc.conversion.Converter;
import net.tnemc.conversion.InvalidDatabaseImport;
import net.tnemc.core.TNE;
import net.tnemc.core.common.currency.TNECurrency;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.math.BigDecimal;

/**
 * Created by creatorfromhell on 11/13/2016.
 **/
public class MineConomy extends Converter {
  private File accountsFile = new File("plugins/MineConomy/accounts.yml");
  private File banksFile = new File("plugins/MineConomy/banks.yml");
  private File currencyFile = new File("plugins/MineConomy/currencies.yml");
  private FileConfiguration accounts = YamlConfiguration.loadConfiguration(accountsFile);
  private FileConfiguration banks = YamlConfiguration.loadConfiguration(banksFile);
  private FileConfiguration currencies = YamlConfiguration.loadConfiguration(currencyFile);

  @Override
  public String name() {
    return "MineConomy";
  }

  @Override
  public void mysql() throws InvalidDatabaseImport {
    db = new MySQL(TNE.saveManager().getTNEManager());

    String table = "mineconomy_accounts";
    try {
      int index = mysqlDB().executeQuery("SELECT * FROM " + table + ";");

      TNECurrency currency = TNE.manager().currencyManager().get(TNE.instance().defaultWorld);
      while (mysqlDB().results(index).next()) {
        String username = mysqlDB().results(index).getString("account");
        Double balance = mysqlDB().results(index).getDouble("balance");
        String currencyName = mysqlDB().results(index).getString("currency");

        String currencyPath = "Currencies." + currencyName + ".Value";
        double rate = (currencies.contains(currencyPath))? currencies.getDouble(currencyPath) : 1.0;
        if(rate > 1.0) rate = 1.0;
        else if(rate < 0.1) rate = 0.1;

        if(TNE.manager().currencyManager().contains(TNE.instance().defaultWorld, currencyName)) {
          currency = TNE.manager().currencyManager().get(TNE.instance().defaultWorld, currencyName);
        }
        ConversionModule.convertedAdd(username, TNE.instance().defaultWorld, currency.name(), TNE.manager().currencyManager().convert(rate, currency.getRate(), new BigDecimal(balance)));
      }
    } catch(Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void yaml() throws InvalidDatabaseImport {

    String base = "Accounts";
    TNECurrency currency = TNE.manager().currencyManager().get(TNE.instance().defaultWorld);
    for(String username : accounts.getConfigurationSection(base).getKeys(false)) {

      double amount = accounts.getDouble(base + "." + username + ".Balance");
      String currencyPath = "Currencies." + accounts.getString(base + "." + username + ".Currency") + ".Value";
      double rate = (currencies.contains(currencyPath))? currencies.getDouble(currencyPath) : 1.0;
      if(rate > 1.0) rate = 1.0;
      else if(rate < 0.1) rate = 0.1;

      if(TNE.manager().currencyManager().contains(TNE.instance().defaultWorld, accounts.getString(base + "." + username + ".Currency"))) {
        currency = TNE.manager().currencyManager().get(TNE.instance().defaultWorld, accounts.getString(base + "." + username + ".Currency"));
      }
      ConversionModule.convertedAdd(username, TNE.instance().defaultWorld, currency.name(), TNE.manager().currencyManager().convert(rate, currency.getRate(), new BigDecimal(amount)));
    }

    base = "Banks";
    for(String bank : banks.getConfigurationSection(base).getKeys(false)) {
      base = "Banks." + bank + ".Accounts";
      for(String username : banks.getConfigurationSection(base).getKeys(false)) {
        ConversionModule.convertedAdd(username, TNE.instance().defaultWorld, currency.name(), new BigDecimal(banks.getDouble(base + "." + username + ".Balance")));
      }
    }
  }
}
