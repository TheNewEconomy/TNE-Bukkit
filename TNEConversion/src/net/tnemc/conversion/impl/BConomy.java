package net.tnemc.conversion.impl;

import com.github.tnerevival.core.db.sql.MySQL;
import com.github.tnerevival.core.db.sql.SQLite;
import net.tnemc.conversion.ConversionModule;
import net.tnemc.conversion.Converter;
import net.tnemc.conversion.InvalidDatabaseImport;
import net.tnemc.core.TNE;
import net.tnemc.core.common.data.TNEDataManager;
import net.tnemc.core.economy.currency.Currency;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


/**
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */
public class BConomy extends Converter {
  private File configFile = new File(TNE.instance().getDataFolder(), "../BConomy/config.yml");
  private FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);

  private String table = "accounts";

  @Override
  public String name() {
    return "BConomy";
  }

  @Override
  public String type() {
    return (config.getBoolean("Database.Mysql.Enabled"))? "MySQL" : "sqlite";
  }

  @Override
  public void mysql() throws InvalidDatabaseImport {
    db = new MySQL(new TNEDataManager(type(), config.getString("Database.Mysql.Host"),
        config.getInt("Database.Mysql.Port"), config.getString("Database.Mysql.Database"),
        config.getString("Database.Mysql.User"), config.getString("Database.Mysql.Pass"),
        config.getString("Database.Mysql.Table"), "accounts.db",
        false, false, 60, false));

    try(Connection connection = mysqlDB().getDataSource().getConnection();
        Statement statement = connection.createStatement();
        ResultSet results = mysqlDB().executeQuery(statement, "SELECT * FROM " + table + ";")) {

      final Currency currency = TNE.manager().currencyManager().get(TNE.instance().defaultWorld);
      while(results.next()) {
        ConversionModule.convertedAdd(results.getString("userid"),
            TNE.instance().defaultWorld, currency.name(),
            new BigDecimal(results.getDouble("balance")));
      }
    } catch(SQLException ignore) {}
  }

  @Override
  public void sqlite() throws InvalidDatabaseImport {
    db = new SQLite(new TNEDataManager(type(), config.getString("Database.Mysql.Host"),
        config.getInt("Database.Mysql.Port"), config.getString("Database.Mysql.Database"),
        config.getString("Database.Mysql.User"), config.getString("Database.Mysql.Pass"),
        config.getString("Database.Mysql.Table"), "accounts.db",
        false, false, 60, false));

    try(Connection connection = sqliteDB().getDataSource().getConnection();
        Statement statement = connection.createStatement();
        ResultSet results = sqliteDB().executeQuery(statement, "SELECT * FROM " + table + ";")) {

      final Currency currency = TNE.manager().currencyManager().get(TNE.instance().defaultWorld);
      while(results.next()) {
        ConversionModule.convertedAdd(results.getString("userid"),
            TNE.instance().defaultWorld, currency.name(),
            new BigDecimal(results.getDouble("balance")));
      }
    } catch(SQLException ignore) {}
  }
}