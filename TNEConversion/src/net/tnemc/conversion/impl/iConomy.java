package net.tnemc.conversion.impl;

import net.tnemc.conversion.ConversionModule;
import net.tnemc.conversion.Converter;
import net.tnemc.conversion.InvalidDatabaseImport;
import net.tnemc.core.TNE;
import net.tnemc.core.common.data.TNEDataManager;
import net.tnemc.core.economy.currency.Currency;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;

/**
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */
public class iConomy extends Converter {
  private File configFile = new File(TNE.instance().getDataFolder(), "../iConomy/Config.yml");
  private FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);
  private String table = config.getString("System.Database.Settings.Table");

  @Override
  public String name() {
    return "iConomy";
  }

  @Override
  public String type() {
    return config.getString("System.Database.Type");
  }

  @Override
  public File dataFolder() {
    return new File(TNE.instance().getDataFolder(), "../iConomy/Config.yml");
  }

  @Override
  public void mysql() throws InvalidDatabaseImport {
    initialize(new TNEDataManager(type(), config.getString("System.Database.Settings.MySQL.Hostname"),
        config.getInt("System.Database.Settings.MySQL.Port"), config.getString("System.Database.Settings.Name"),
        config.getString("System.Database.Settings.MySQL.Username"), config.getString("System.Database.Settings.MySQL.Password"),
        config.getString("System.Database.Settings.Table"), "accounts.db",
        false, false, 60, false));

    open();
    try(Connection connection = db.getConnection();
        Statement statement = connection.createStatement();
        ResultSet results = statement.executeQuery("SELECT * FROM " + table + ";")) {

      final Currency currency = TNE.manager().currencyManager().get(TNE.instance().defaultWorld);
      while(results.next()) {
        ConversionModule.convertedAdd(results.getString("username"),
            TNE.instance().defaultWorld, currency.name(),
            new BigDecimal(results.getDouble("balance")));
      }
    } catch(SQLException ignore) {}
    close();
  }

  @Override
  public void h2() throws InvalidDatabaseImport {
    super.h2();
  }

  @Override
  public void postgre() throws InvalidDatabaseImport {
    super.postgre();
  }

  @Override
  public void flatfile() throws InvalidDatabaseImport {
    try {
      BufferedReader reader = new BufferedReader(new FileReader(new File("plugins/iConomy/accounts.mini")));

      String line;
      while((line = reader.readLine()) != null) {
        String[] split = line.split(" ");
        Double money = Double.parseDouble(split[1].split(":")[1]);
        ConversionModule.convertedAdd(split[0].trim(), TNE.instance().defaultWorld, TNE.manager().currencyManager().get(TNE.instance().defaultWorld).name(), new BigDecimal(money));
      }
    } catch(Exception e) {
      TNE.instance().getLogger().log(Level.WARNING, "Unable to load iConomy Data.");
    }
  }

  @Override
  public void inventoryDB() throws InvalidDatabaseImport {
    super.inventoryDB();
  }

  @Override
  public void expDB() throws InvalidDatabaseImport {
    super.expDB();
  }
}