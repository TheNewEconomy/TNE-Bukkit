package net.tnemc.conversion.impl;

import com.github.tnerevival.core.db.sql.MySQL;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.tnemc.conversion.ConversionModule;
import net.tnemc.conversion.Converter;
import net.tnemc.conversion.InvalidDatabaseImport;
import net.tnemc.core.TNE;
import net.tnemc.core.common.api.IDFinder;
import net.tnemc.core.common.data.TNEDataManager;
import net.tnemc.core.economy.currency.Currency;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

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
public class SaneEconomy extends Converter {
  private File configFile = new File(TNE.instance().getDataFolder(), "../SaneEconomy/config.yml");
  private FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);

  @Override
  public String name() {
    return "SaneEconomy";
  }

  @Override
  public String type() {
    return config.getString("backend.type");
  }

  @Override
  public void mysql() throws InvalidDatabaseImport {
    db = new MySQL(new TNEDataManager(type(), config.getString("backend.host"),
        config.getInt("backend.port"), config.getString("backend.database"),
        config.getString("backend.username"), config.getString("backend.password"),
        config.getString("backend.table_prefix"), "economy.db",
        false, false, 60, false));

    final String prefix = config.getString("backend.table_prefix");

    try(Connection connection = mysqlDB().getDataSource().getConnection();
        Statement statement = connection.createStatement();
        ResultSet results = mysqlDB().executeQuery(statement, "SELECT * FROM " + prefix + "saneeconomy_balances;")) {

      final Currency currency = TNE.manager().currencyManager().get(TNE.instance().defaultWorld);
      while(results.next()) {
        ConversionModule.convertedAdd(results.getString("unique_identifier"),
            TNE.instance().defaultWorld, currency.name(),
            new BigDecimal(results.getDouble("balance")));
      }
    } catch(SQLException ignore) {}
  }

  @Override
  public void json() throws InvalidDatabaseImport {
    File file = new File(TNE.instance().getDataFolder(), "../SaneEconomy" + config.getString("backend.file", "economy.json"));
    final Gson gson = new GsonBuilder().serializeNulls().setPrettyPrinting().create();
    Map<String, Double> balances = new HashMap<>();
    try {
      balances = (HashMap<String, Double>)gson.fromJson(new FileReader(file), new TypeToken<Map<String, Double>>(){}.getType());
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    Currency currency = TNE.manager().currencyManager().get(TNE.instance().defaultWorld);
    balances.forEach((id, balance)->{
      ConversionModule.convertedAdd(IDFinder.getUsername(id), TNE.instance().defaultWorld, currency.name(), new BigDecimal(balance));
    });
  }
}