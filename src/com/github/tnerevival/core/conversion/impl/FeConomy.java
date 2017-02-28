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
import com.github.tnerevival.core.conversion.Converter;
import com.github.tnerevival.core.currency.Currency;
import com.github.tnerevival.core.db.MySQL;
import com.github.tnerevival.core.db.SQLite;
import com.github.tnerevival.core.exception.InvalidDatabaseImport;
import com.github.tnerevival.utils.AccountUtils;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.math.BigDecimal;

/**
 * Created by creatorfromhell on 11/13/2016.
 **/
public class FeConomy extends Converter {
  private File configFile = new File(TNE.instance().getDataFolder(), "../Fe/config.yml");
  private FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);

  private String table = config.getString("mysql.tables.accounts");
  private String nameColumn = config.getString("mysql.columns.accounts.username");
  private String balanceColumn = config.getString("mysql.columns.accounts.money");
  private Currency currency = TNE.instance().manager.currencyManager.get(TNE.instance().defaultWorld);
  private double rate = 1.0;

  @Override
  public String name() {
    return "FeConomy";
  }

  @Override
  public void mysql() throws InvalidDatabaseImport {
    db = new MySQL(mysqlHost, mysqlPort, mysqlDatabase, mysqlUser, mysqlPassword);
    try {
      int index = mysqlDB().executeQuery("SELECT * FROM " + table + ";");

      while (mysqlDB().results(index).next()) {
        String username = mysqlDB().results(index).getString(nameColumn);
        Double balance = mysqlDB().results(index).getDouble(balanceColumn);
        AccountUtils.convertedAdd(username, TNE.instance().defaultWorld, currency.getName(), new BigDecimal(balance));
      }
    } catch(Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void sqlite() throws InvalidDatabaseImport {
    db = new SQLite(TNE.instance().getDataFolder() + "../Fe/database.db");
    try {
      int index = sqliteDB().executeQuery("SELECT * FROM " + table + ";");

      while (sqliteDB().results(index).next()) {
        String username = sqliteDB().results(index).getString(nameColumn);
        Double balance = sqliteDB().results(index).getDouble(balanceColumn);
        AccountUtils.convertedAdd(username, TNE.instance().defaultWorld, currency.getName(), new BigDecimal(balance));
      }
    } catch(Exception e) {
      e.printStackTrace();
    }
  }
}