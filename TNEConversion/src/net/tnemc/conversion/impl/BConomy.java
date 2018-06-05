package net.tnemc.conversion.impl;

import com.github.tnerevival.core.db.sql.MySQL;
import com.github.tnerevival.core.db.sql.SQLite;
import net.tnemc.conversion.ConversionModule;
import net.tnemc.conversion.Converter;
import net.tnemc.conversion.InvalidDatabaseImport;
import net.tnemc.core.TNE;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.math.BigDecimal;


/**
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */
public class BConomy extends Converter {
  private File configFile = new File("plugins/BConomy/config.yml");
  private FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);

  private String table = config.getString("Database.Mysql.Table");

  @Override
  public String name() {
    return "BConomy";
  }

  @Override
  public void mysql() throws InvalidDatabaseImport {
    db = new MySQL(conversionManager);
    try {
      int index = mysqlDB().executeQuery("SELECT * FROM " + table + ";");

      while (mysqlDB().results(index).next()) {
        String uuid = mysqlDB().results(index).getString("userid");
        Integer balance = mysqlDB().results(index).getInt("balance");
        ConversionModule.convertedAdd(uuid, TNE.instance().defaultWorld, TNE.manager().currencyManager().get(TNE.instance().defaultWorld).name(), new BigDecimal(balance));
      }
    } catch(Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void sqlite() throws InvalidDatabaseImport {
    db = new SQLite(conversionManager);
    try {
      int index = sqliteDB().executeQuery("SELECT * FROM " + table + ";");

      while (sqliteDB().results(index).next()) {
        String uuid = sqliteDB().results(index).getString("userid");
        Integer balance = sqliteDB().results(index).getInt("balance");
        ConversionModule.convertedAdd(uuid, TNE.instance().defaultWorld, TNE.manager().currencyManager().get(TNE.instance().defaultWorld).name(), new BigDecimal(balance));
      }
    } catch(Exception e) {
      e.printStackTrace();
    }
  }
}