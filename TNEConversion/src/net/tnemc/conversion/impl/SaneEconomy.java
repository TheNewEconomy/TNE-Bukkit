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
import net.tnemc.core.economy.currency.Currency;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.math.BigDecimal;
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
  private File configFile = new File("plugins/SaneEconomy/config.yml");
  private FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);

  @Override
  public String name() {
    return "SaneEconomy";
  }

  @Override
  public void mysql() throws InvalidDatabaseImport {
    db = new MySQL(conversionManager);
    try {
      int index = mysqlDB().executeQuery("SELECT * FROM saneeconomy_balances;");

      Currency currency = TNE.manager().currencyManager().get(TNE.instance().defaultWorld);
      while (mysqlDB().results(index).next()) {
        String uuid = mysqlDB().results(index).getString("unique_identifier");
        Double balance = mysqlDB().results(index).getDouble("balance");
        ConversionModule.convertedAdd(IDFinder.getUsername(uuid), TNE.instance().defaultWorld, currency.name(), new BigDecimal(balance));
      }
    } catch(Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void json() throws InvalidDatabaseImport {
    File file = new File("plugins/SaneEconomy" + config.getString("backend.file", "economy.json"));
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