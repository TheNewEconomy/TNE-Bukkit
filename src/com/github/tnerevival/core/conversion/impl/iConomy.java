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
import com.github.tnerevival.account.Account;
import com.github.tnerevival.account.IDFinder;
import com.github.tnerevival.core.conversion.Converter;
import com.github.tnerevival.core.currency.Currency;
import com.github.tnerevival.core.db.MySQL;
import com.github.tnerevival.core.exception.InvalidDatabaseImport;
import com.github.tnerevival.core.transaction.TransactionType;
import com.github.tnerevival.utils.AccountUtils;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.UUID;
import java.util.logging.Level;

/**
 * Created by creatorfromhell on 11/13/2016.
 **/
public class iConomy extends Converter {
  private File configFile = new File(TNE.instance.getDataFolder(), "../iConomy/Config.yml");
  private FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);
  private String table = config.getString("System.Database.Table");

  @Override
  public String name() {
    return "iConomy";
  }

  @Override
  public void mysql() throws InvalidDatabaseImport {
    db = new MySQL(mysqlHost, mysqlPort, mysqlDatabase, mysqlUser, mysqlPassword);
    try {
      mysqlDB().executeQuery("SELECT * FROM " + table + ";");

      Currency currency = TNE.instance.manager.currencyManager.get(TNE.instance.defaultWorld);
      while (mysqlDB().results().next()) {
        String username = mysqlDB().results().getString("username");
        Double balance = mysqlDB().results().getDouble("balance");

        UUID id = IDFinder.getID(username);
        Account account = new Account(IDFinder.getID(username));
        account.setBalance(TNE.instance.defaultWorld, balance, currency.getName());
        TNE.instance.manager.accounts.put(id, account);
      }
    } catch(Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void h2() throws InvalidDatabaseImport {
    super.h2();
  }

  @Override
  public void postgre() throws InvalidDatabaseImport {
    super.postgre();
  }

  @Override
  public void flatfile() throws InvalidDatabaseImport {
    try {
      BufferedReader reader = new BufferedReader(new FileReader(new File(TNE.instance.getDataFolder(), "../iConomy/accounts.mini")));

      String line;
      while((line = reader.readLine()) != null) {
        String[] split = line.split(" ");
        UUID id = IDFinder.getID(split[0].trim());
        Double money = Double.parseDouble(split[1].split(":")[1]);


        AccountUtils.transaction(null,
            id.toString(),
            money,
            TNE.instance.manager.currencyManager.get(
                TNE.instance.defaultWorld
            ),
            TransactionType.MONEY_SET,
            TNE.instance.defaultWorld
        );
      }
    } catch(Exception e) {
      TNE.instance.getLogger().log(Level.WARNING, "Unable to load iConomy Data.");
    }
  }

  @Override
  public void inventoryDB() throws InvalidDatabaseImport {
    super.inventoryDB();
  }

  @Override
  public void expDB() throws InvalidDatabaseImport {
    super.expDB();
  }
}