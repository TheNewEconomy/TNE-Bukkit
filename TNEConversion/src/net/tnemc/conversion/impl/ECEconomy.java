package net.tnemc.conversion.impl;

import net.tnemc.conversion.ConversionModule;
import net.tnemc.conversion.Converter;
import net.tnemc.conversion.InvalidDatabaseImport;
import net.tnemc.core.TNE;
import net.tnemc.core.common.currency.TNECurrency;
import net.tnemc.core.common.data.TNEDataManager;
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
 * <p>
 * Created by Daniel on 5/28/2018.
 * <p>
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */
public class ECEconomy extends Converter {
  private File configFile = new File(TNE.instance().getDataFolder(), "../EC_Economy/config.yml");
  private FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);
  @Override
  public String name() {
    return "ECEconomy";
  }

  @Override
  public String type() {
    return (config.getBoolean("Mysql.Enabled"))? "MySQL" : "sqlite";
  }

  @Override
  public File dataFolder() {
    return new File(TNE.instance().getDataFolder(), "../EC_Economy/config.yml");
  }

  @Override
  public void mysql() throws InvalidDatabaseImport {
    initialize(new TNEDataManager(type(), config.getString("MySQL.hostname"),
        config.getInt("MySQL.port"), config.getString("MySQL.database"),
        config.getString("MySQL.username"), config.getString("MySQL.password"),
        "Money", "accounts.db",
        false, false, 60, false));

    open();
    try(Connection connection = db.getConnection();
        Statement statement = connection.createStatement();
        ResultSet results = statement.executeQuery("SELECT Player, Money FROM Money;")) {

      final TNECurrency currency = TNE.manager().currencyManager().get(TNE.instance().defaultWorld);
      while(results.next()) {
        ConversionModule.convertedAdd(results.getString("Player"),
            TNE.instance().defaultWorld, currency.name(),
            new BigDecimal(results.getDouble("Money")));
      }
    } catch(SQLException ignore) {}
    close();
  }

  @Override
  public void sqlite() throws InvalidDatabaseImport {
    initialize(new TNEDataManager(type(), config.getString("MySQL.hostname"),
        config.getInt("MySQL.port"), config.getString("MySQL.database"),
        config.getString("MySQL.username"), config.getString("MySQL.password"),
        "Money", "accounts.db",
        false, false, 60, false));

    open();
    try(Connection connection = db.getConnection();
        Statement statement = connection.createStatement();
        ResultSet results = statement.executeQuery("SELECT Player, Money FROM Money;")) {

      final TNECurrency currency = TNE.manager().currencyManager().get(TNE.instance().defaultWorld);
      while(results.next()) {
        ConversionModule.convertedAdd(results.getString("Player"),
            TNE.instance().defaultWorld, currency.name(),
            new BigDecimal(results.getDouble("Money")));
      }
    } catch(SQLException ignore) {}
    close();
  }
}