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
 * Created by creatorfromhell on 1/25/2020.
 * <p>
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */
public class MySQLBridge extends Converter {

  @Override
  public String name() {
    return "MySQLBridge";
  }

  @Override
  public String type() {
    return "mysql";
  }

  @Override
  public File dataFolder() {
    return new File(TNE.instance().getDataFolder(), "../MysqlPlayerDataBridge/config.yml");
  }


  @Override
  public void mysql() throws InvalidDatabaseImport {
    File configFile = new File(TNE.instance().getDataFolder(), "../MysqlPlayerDataBridge/config.yml");
    FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);

    final String table = config.getString("database.mysql.TablesNames.economyTableName");

    initialize(new TNEDataManager(type(), config.getString("database.mysql.host"),
        config.getInt("database.mysql.port"), config.getString("database.mysql.databaseName"),
        config.getString("database.mysql.user"), config.getString("database.mysql.password"),
        table, "accounts.db",
        false, false, 60, false));
    open();
    try(Connection connection = db.getConnection();
        Statement statement = connection.createStatement();
        ResultSet results = statement.executeQuery("SELECT player_uuid, money, offline_money FROM " + table + ";")) {

      final TNECurrency currency = TNE.manager().currencyManager().get(TNE.instance().defaultWorld);
      while(results.next()) {
        ConversionModule.convertedAdd(results.getString("player_uuid"),
            TNE.instance().defaultWorld, currency.name(),
            new BigDecimal(results.getDouble("money")));
        ConversionModule.convertedAdd(results.getString("player_uuid"),
            TNE.instance().defaultWorld, currency.name(),
            new BigDecimal(results.getDouble("offline_money")));
      }
    } catch(SQLException ignore) {}
    close();

  }
}