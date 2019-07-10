package net.tnemc.conversion.impl;

import net.tnemc.conversion.ConversionModule;
import net.tnemc.conversion.Converter;
import net.tnemc.conversion.InvalidDatabaseImport;
import net.tnemc.core.TNE;
import net.tnemc.core.common.data.TNEDataManager;
import net.tnemc.core.economy.currency.Currency;
import org.bukkit.Bukkit;
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
public class Essentials extends Converter {
  private File dataDirectory = new File(TNE.instance().getDataFolder(), "../Essentials/userdata");

  @Override
  public String name() {
    return "Essentials";
  }

  @Override
  public String type() {
    return (Bukkit.getServer().getPluginManager().isPluginEnabled("EssentialsMysqlStorage"))? "mysql" : "yaml";
  }

  @Override
  public void mysql() throws InvalidDatabaseImport {
    File configFile = new File(TNE.instance().getDataFolder(), "../EssentialsMysqlStorage/config.yml");
    FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);

    final String table = config.getString("Database.Mysql.TableName");

    initialize(new TNEDataManager(type(), config.getString("Database.Mysql.Host"),
        config.getInt("Database.Mysql.Port"), config.getString("Database.Mysql.DatabaseName"),
        config.getString("Database.Mysql.User"), config.getString("Database.Mysql.Password"),
        table, "accounts.db",
        false, false, 60, false));
    open();
    try(Connection connection = db.getConnection();
        Statement statement = connection.createStatement();
        ResultSet results = statement.executeQuery("SELECT player_uuid, money, offline_money FROM " + table + ";")) {

      final Currency currency = TNE.manager().currencyManager().get(TNE.instance().defaultWorld);
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

  @Override
  public void yaml() throws InvalidDatabaseImport {
    if(!dataDirectory.isDirectory() || dataDirectory.listFiles() == null || dataDirectory.listFiles().length == 0) return;

    for(File accountFile : dataDirectory.listFiles()) {
      FileConfiguration acc = YamlConfiguration.loadConfiguration(accountFile);

      final BigDecimal money = acc.contains("money")? new BigDecimal(acc.getString("money")) : BigDecimal.ZERO;
      String currency = TNE.manager().currencyManager().get(TNE.instance().defaultWorld).name();

      ConversionModule.convertedAdd(acc.getString("lastAccountName"), TNE.instance().defaultWorld, currency, money);
    }
  }
}