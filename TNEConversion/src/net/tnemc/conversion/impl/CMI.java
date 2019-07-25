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
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * Created by creatorfromhell on 7/24/2019.
 * <p>
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */
public class CMI extends Converter {

  private File configFile = new File(TNE.instance().getDataFolder(), "../CMI/config.yml");
  private FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);

  @Override
  public String name() {
    return "CMI";
  }

  @Override
  public String type() {
    return config.getString("storage.method").toLowerCase();
  }

  @Override
  public void mysql() throws InvalidDatabaseImport {

    final String prefix = config.getString("mysql.tablePrefix");
    final String table = prefix + "users";
    final String[] workHost = config.getString("mysql.hostname").split(":");

    initialize(new TNEDataManager(type(), workHost[0],
        Integer.valueOf(workHost[1]), config.getString("mysql.database"),
        config.getString("mysql.username"), config.getString("mysql.password"),
        prefix, "cmi.sqlite",
        false, false, 60, false));
    open();
    try(Connection connection = db.getConnection();
        Statement statement = connection.createStatement();
        ResultSet results = statement.executeQuery("SELECT username, Balance FROM " + table + ";")) {

      final Currency currency = TNE.manager().currencyManager().get(TNE.instance().defaultWorld);
      while(results.next()) {
        ConversionModule.convertedAdd(results.getString("username"),
            TNE.instance().defaultWorld, currency.name(),
            new BigDecimal(results.getDouble("Balance")));
      }
    } catch(SQLException ignore) {}
    close();

  }

  @Override
  public void sqlite() throws InvalidDatabaseImport {
    try {

      Class.forName("org.sqlite.JDBC");

      try(Connection connection = DriverManager.getConnection("jdbc:sqlite:" + new File(TNE.instance().getDataFolder(), "../CMI/cmi.sqlite.db").getAbsolutePath());
          Statement statement = connection.createStatement();
          ResultSet results = statement.executeQuery("SELECT username, Balance FROM users;")) {

        final Currency currency = TNE.manager().currencyManager().get(TNE.instance().defaultWorld);
        while(results.next()) {
          ConversionModule.convertedAdd(results.getString("username"),
              TNE.instance().defaultWorld, currency.name(),
              new BigDecimal(results.getDouble("Balance")));
        }
      } catch(SQLException ignore) {}

    } catch(Exception ignore) {}

  }
}