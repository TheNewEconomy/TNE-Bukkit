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
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */
public class MineConomy extends Converter {
  private File configFile = new File(TNE.instance().getDataFolder(), "../Mineconomy/config.yml");
  private FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);
  private File accountsFile = new File(TNE.instance().getDataFolder(), "../MineConomy/accounts.yml");
  private File banksFile = new File(TNE.instance().getDataFolder(), "../MineConomy/banks.yml");
  private File currencyFile = new File(TNE.instance().getDataFolder(), "../MineConomy/currencies.yml");
  private FileConfiguration accounts = YamlConfiguration.loadConfiguration(accountsFile);
  private FileConfiguration banks = YamlConfiguration.loadConfiguration(banksFile);
  private FileConfiguration currencies = YamlConfiguration.loadConfiguration(currencyFile);

  @Override
  public String name() {
    return "MineConomy";
  }

  @Override
  public String type() {
    return config.getString("Database.Type");
  }

  @Override
  public File dataFolder() {
    return new File(TNE.instance().getDataFolder(), "../Mineconomy/config.yml");
  }

  @Override
  public void mysql() throws InvalidDatabaseImport {
    initialize(new TNEDataManager(type(), config.getString("Database.URL"),
        3306, config.getString("Database.Name"),
        config.getString("Database.Username"), config.getString("Database.Password"),
        "mineconomy_accounts", "accounts.db",
        false, false, 60, false));

    String table = "mineconomy_accounts";

    open();
    try(Connection connection = db.getConnection();
        Statement statement = connection.createStatement();
        ResultSet results = statement.executeQuery("SELECT * FROM " + table + ";")) {

      TNECurrency currency = TNE.manager().currencyManager().get(TNE.instance().defaultWorld);
      while(results.next()) {
        String username = results.getString("account");
        Double balance = results.getDouble("balance");
        String currencyName = results.getString("currency");

        String currencyPath = "Currencies." + currencyName + ".Value";
        double rate = (currencies.contains(currencyPath))? currencies.getDouble(currencyPath) : 1.0;
        if(rate > 1.0) rate = 1.0;
        else if(rate < 0.1) rate = 0.1;

        if(TNE.manager().currencyManager().contains(TNE.instance().defaultWorld, currencyName)) {
          currency = TNE.manager().currencyManager().get(TNE.instance().defaultWorld, currencyName);
        }
        ConversionModule.convertedAdd(username, TNE.instance().defaultWorld, currency.name(),
            TNE.manager().currencyManager().convert(rate, currency.getRate(), new BigDecimal(balance)));
      }
    } catch(SQLException ignore) {}
    close();
  }

  @Override
  public void yaml() throws InvalidDatabaseImport {

    String base = "Accounts";
    TNECurrency currency = TNE.manager().currencyManager().get(TNE.instance().defaultWorld);
    for(String username : accounts.getConfigurationSection(base).getKeys(false)) {

      double amount = accounts.getDouble(base + "." + username + ".Balance");
      String currencyPath = (currencies != null)? "Currencies." + accounts.getString(base + "." + username + ".Currency") + ".Value" : null;
      double rate = (currencyPath != null && currencies != null && currencies.contains(currencyPath))? currencies.getDouble(currencyPath) : 1.0;
      if(rate > 1.0) rate = 1.0;
      else if(rate < 0.1) rate = 0.1;

      if(TNE.manager().currencyManager().contains(TNE.instance().defaultWorld, accounts.getString(base + "." + username + ".Currency"))) {
        currency = TNE.manager().currencyManager().get(TNE.instance().defaultWorld, accounts.getString(base + "." + username + ".Currency"));
      }
      ConversionModule.convertedAdd(username, TNE.instance().defaultWorld, currency.name(), TNE.manager().currencyManager().convert(rate, currency.getRate(), new BigDecimal(amount)));
    }

    base = "Banks";

    if(banks != null && banks.contains(base) && !banks.isString(base)) {
      for (String bank : banks.getConfigurationSection(base).getKeys(false)) {
        base = "Banks." + bank + ".Accounts";
        if(banks.contains(base)) {
          for (String username : banks.getConfigurationSection(base).getKeys(false)) {
            ConversionModule.convertedAdd(username, TNE.instance().defaultWorld, currency.name(), new BigDecimal(banks.getDouble(base + "." + username + ".Balance")));
          }
        }
      }
    }
  }
}
