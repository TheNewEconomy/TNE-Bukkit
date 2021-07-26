package net.tnemc.conversion.impl;

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
public class FeConomy extends Converter {
  private File configFile = new File(TNE.instance().getDataFolder(), "../Fe/config.yml");
  private FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);

  private String table = config.getString("mysql.tables.accounts");
  private String nameColumn = config.getString("mysql.columns.accounts.username");
  private String balanceColumn = config.getString("mysql.columns.accounts.money");

  @Override
  public String name() {
    return "FeConomy";
  }

  @Override
  public String type() {
    return config.getString("type");
  }

  @Override
  public File dataFolder() {
    return new File(TNE.instance().getDataFolder(), "../Fe/config.yml");
  }

  @Override
  public void mysql() throws InvalidDatabaseImport {
    initialize(new TNEDataManager(type(), config.getString("mysql.host"),
        config.getInt("mysql.port"), config.getString("mysql.database"),
        config.getString("mysql.user"), config.getString("mysql.password"),
        table, "database.db",
        false, false, 60, false));

    open();
    try(Connection connection = db.getConnection();
        Statement statement = connection.createStatement();
        ResultSet results = statement.executeQuery("SELECT * FROM " + table + ";")) {

      final Currency currency = TNE.manager().currencyManager().get(TNE.instance().defaultWorld);
      while(results.next()) {
        ConversionModule.convertedAdd(results.getString(nameColumn),
            TNE.instance().defaultWorld, currency.name(),
            new BigDecimal(results.getDouble(balanceColumn)));
      }
    } catch(SQLException ignore) {}
    close();
  }

  @Override
  public void sqlite() throws InvalidDatabaseImport {
    initialize(new TNEDataManager(type(), config.getString("mysql.host"),
        config.getInt("mysql.port"), config.getString("mysql.database"),
        config.getString("mysql.user"), config.getString("mysql.password"),
        table, "database.db",
        false, false, 60, false));

    open();
    try(Connection connection = db.getConnection();
        Statement statement = connection.createStatement();
        ResultSet results = statement.executeQuery("SELECT * FROM " + table + ";")) {

      final Currency currency = TNE.manager().currencyManager().get(TNE.instance().defaultWorld);
      while(results.next()) {
        ConversionModule.convertedAdd(results.getString(nameColumn),
            TNE.instance().defaultWorld, currency.name(),
            new BigDecimal(results.getDouble(balanceColumn)));
      }
    } catch(SQLException ignore) {}
    close();
  }
}