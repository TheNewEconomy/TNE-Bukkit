package com.github.tnerevival.core.conversion.impl;

import com.github.tnerevival.TNE;
import com.github.tnerevival.core.conversion.Converter;
import com.github.tnerevival.core.db.MySQL;
import com.github.tnerevival.core.db.SQLite;
import com.github.tnerevival.core.exception.InvalidDatabaseImport;
import com.github.tnerevival.utils.AccountUtils;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.math.BigDecimal;

/**
 * Created by creatorfromhell on 6/10/2017.
 * All rights reserved.
 **/
public class BConomy extends Converter {
  private File configFile = new File(TNE.instance().getDataFolder(), "../BConomy/config.yml");
  private FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);

  private String table = config.getString("Database.Mysql.Table");

  @Override
  public String name() {
    return "BConomy";
  }

  @Override
  public void mysql() throws InvalidDatabaseImport {
    db = new MySQL(mysqlHost, mysqlPort, mysqlDatabase, mysqlUser, mysqlPassword);
    try {
      int index = mysqlDB().executeQuery("SELECT * FROM " + table + ";");

      while (mysqlDB().results(index).next()) {
        String uuid = mysqlDB().results(index).getString("userid");
        Integer balance = mysqlDB().results(index).getInt("balance");
        AccountUtils.convertedAdd(uuid, TNE.instance().defaultWorld, TNE.instance().manager.currencyManager.get(TNE.instance().defaultWorld).getName(), new BigDecimal(balance));
      }
    } catch(Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void sqlite() throws InvalidDatabaseImport {
    db = new SQLite(TNE.instance().getDataFolder() + "../BConomy/accounts.db");
    try {
      int index = sqliteDB().executeQuery("SELECT * FROM " + table + ";");

      while (sqliteDB().results(index).next()) {
        String uuid = mysqlDB().results(index).getString("userid");
        Integer balance = mysqlDB().results(index).getInt("balance");
        AccountUtils.convertedAdd(uuid, TNE.instance().defaultWorld, TNE.instance().manager.currencyManager.get(TNE.instance().defaultWorld).getName(), new BigDecimal(balance));
      }
    } catch(Exception e) {
      e.printStackTrace();
    }
  }
}