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
import com.github.tnerevival.core.db.sql.SQLite;
import net.tnemc.conversion.ConversionModule;
import net.tnemc.conversion.Converter;
import net.tnemc.conversion.InvalidDatabaseImport;
import net.tnemc.core.TNE;
import net.tnemc.core.economy.currency.Currency;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.math.BigDecimal;

/**
 * Created by creatorfromhell on 11/13/2016.
 **/
public class FeConomy extends Converter {
  private File configFile = new File("plugins/Fe/config.yml");
  private FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);

  private String table = config.getString("mysql.tables.accounts");
  private String nameColumn = config.getString("mysql.columns.accounts.username");
  private String balanceColumn = config.getString("mysql.columns.accounts.money");
  private Currency currency = TNE.manager().currencyManager().get(TNE.instance().defaultWorld);
  private double rate = 1.0;

  @Override
  public String name() {
    return "FeConomy";
  }

  @Override
  public void mysql() throws InvalidDatabaseImport {
    db = new MySQL(TNE.saveManager().getTNEManager());
    try {
      int index = mysqlDB().executeQuery("SELECT * FROM " + table + ";");

      while (mysqlDB().results(index).next()) {
        String username = mysqlDB().results(index).getString(nameColumn);
        Double balance = mysqlDB().results(index).getDouble(balanceColumn);
        ConversionModule.convertedAdd(username, TNE.instance().defaultWorld, currency.name(), new BigDecimal(balance));
      }
    } catch(Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void sqlite() throws InvalidDatabaseImport {
    db = new SQLite(TNE.saveManager().getTNEManager());
    try {
      int index = sqliteDB().executeQuery("SELECT * FROM " + table + ";");

      while (sqliteDB().results(index).next()) {
        String username = sqliteDB().results(index).getString(nameColumn);
        Double balance = sqliteDB().results(index).getDouble(balanceColumn);
        ConversionModule.convertedAdd(username, TNE.instance().defaultWorld, currency.name(), new BigDecimal(balance));
      }
    } catch(Exception e) {
      e.printStackTrace();
    }
  }
}