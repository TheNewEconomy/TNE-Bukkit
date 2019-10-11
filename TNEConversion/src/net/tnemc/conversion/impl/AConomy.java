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
import java.util.Set;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * Created by creatorfromhell on 10/9/2019.
 * <p>
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */
public class AConomy extends Converter {
  private File configFile = new File(TNE.instance().getDataFolder(), "../AConomy/config.yml");
  private FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);
  private File dataFile = new File(TNE.instance().getDataFolder(), "../AConomy/playerdata.yml");
  private FileConfiguration data = YamlConfiguration.loadConfiguration(dataFile);
  @Override
  public String name() {
    return "AConomy";
  }

  @Override
  public String type() {
    return (config.getBoolean("Mysql.Enabled"))? "mysql" : "yaml";
  }

  @Override
  public void mysql() throws InvalidDatabaseImport {
    initialize(new TNEDataManager(type(), config.getString("Mysql.Host"),
        config.getInt("Mysql.Port"), config.getString("Mysql.Database"),
        config.getString("Mysql.Username"), config.getString("Mysql.Password"),
        "", "accounts.db",
        false, false, 60, false));

    open();
    try(Connection connection = db.getConnection();
        Statement statement = connection.createStatement();
        ResultSet results = statement.executeQuery("SELECT `Name`, `Balance`  FROM `Economy`;")) {

      final Currency currency = TNE.manager().currencyManager().get(TNE.instance().defaultWorld);
      while(results.next()) {
        ConversionModule.convertedAdd(results.getString("Name"),
            TNE.instance().defaultWorld, currency.name(),
            new BigDecimal(results.getDouble("Balance")));
      }
    } catch(SQLException ignore) {}
    close();
  }

  @Override
  public void yaml() throws InvalidDatabaseImport {
    Set<String> keys = data.getKeys(false);

    final Currency currency = TNE.manager().currencyManager().get(TNE.instance().defaultWorld);

    for(String id : keys) {
      if(id.toLowerCase().contains("aconomy")) continue;


      ConversionModule.convertedAdd(id,
          TNE.instance().defaultWorld, currency.name(),
          new BigDecimal(data.getDouble(id)));
    }
  }
}