package net.tnemc.conversion.impl;

import net.tnemc.conversion.ConversionModule;
import net.tnemc.conversion.Converter;
import net.tnemc.conversion.InvalidDatabaseImport;
import net.tnemc.core.TNE;
import net.tnemc.core.common.currency.TNECurrency;
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
public class TownyEco extends Converter {
  private File configFile = new File(TNE.instance().getDataFolder(), "../TownyEco/config.yml");
  private FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);

  private String prefix = config.getString("database.table_prefix");
  @Override
  public String name() {
    return "TownyEco";
  }

  @Override
  public String type() {
    return "mysql";
  }

  @Override
  public File dataFolder() {
    return new File(TNE.instance().getDataFolder(), "../TownyEco/config.yml");
  }

  @Override
  public void mysql() throws InvalidDatabaseImport {
    initialize(null);
    String table = prefix + "balances";

    open();
    try(Connection connection = db.getConnection();
        Statement statement = connection.createStatement();
        ResultSet results = statement.executeQuery("SELECT uuid, world, currency, balance FROM " + table + ";")) {
      while(results.next()) {
        final String world = results.getString("world");
        TNECurrency currency = TNE.manager().currencyManager().get(world, results.getString("currency"));
        if(currency == null) {
          currency = TNE.manager().currencyManager().get(TNE.instance().defaultWorld);
        }
        ConversionModule.convertedAdd(results.getString("username"),
            world, currency.name(),
            new BigDecimal(results.getDouble("balance")));
      }
    } catch(SQLException ignore) {}
    close();
  }

  @Override
  public void sqlite() throws InvalidDatabaseImport {
    initialize(null);
    String table = prefix + "balances";

    open();
    try(Connection connection = db.getConnection();
        Statement statement = connection.createStatement();
        ResultSet results = statement.executeQuery("SELECT uuid, world, currency, balance FROM " + table + ";")) {
      while(results.next()) {
        final String world = results.getString("world");
        TNECurrency currency = TNE.manager().currencyManager().get(world, results.getString("currency"));
        if(currency == null) {
          currency = TNE.manager().currencyManager().get(TNE.instance().defaultWorld);
        }
        ConversionModule.convertedAdd(results.getString("username"),
            world, currency.name(),
            new BigDecimal(results.getDouble("balance")));
      }
    } catch(SQLException ignore) {}
    close();
  }
}
