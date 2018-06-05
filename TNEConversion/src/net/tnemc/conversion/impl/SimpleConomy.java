package net.tnemc.conversion.impl;

import com.github.tnerevival.core.db.sql.MySQL;
import net.tnemc.conversion.ConversionModule;
import net.tnemc.conversion.Converter;
import net.tnemc.conversion.InvalidDatabaseImport;
import net.tnemc.core.TNE;
import net.tnemc.core.common.api.IDFinder;
import net.tnemc.core.economy.currency.Currency;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.math.BigDecimal;

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
public class SimpleConomy extends Converter {
  private File configFile = new File("plugins/SimpleConomy/config.yml");
  private FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);
  @Override
  public String name() {
    return "GemsEconomy";
  }

  @Override
  public void mysql() throws InvalidDatabaseImport {
    db = new MySQL(conversionManager);
    String table = config.getString("mySqlSettings.Connection.Values.table");
    try {
      int index = mysqlDB().executeQuery("SELECT UUID, COINS FROM " + table + ";");

      while (mysqlDB().results(index).next()) {
        String uuid = mysqlDB().results(index).getString("UUID");
        Double balance = mysqlDB().results(index).getDouble("COINS");
        Currency currency = TNE.manager().currencyManager().get(TNE.instance().defaultWorld);
        ConversionModule.convertedAdd(IDFinder.getUsername(uuid), TNE.instance().defaultWorld, currency.name(), new BigDecimal(balance));
      }
    } catch(Exception e) {
      e.printStackTrace();
    }
  }
}
