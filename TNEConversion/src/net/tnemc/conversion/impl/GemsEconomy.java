package net.tnemc.conversion.impl;

import com.github.tnerevival.core.db.sql.MySQL;
import net.tnemc.conversion.ConversionModule;
import net.tnemc.conversion.Converter;
import net.tnemc.conversion.InvalidDatabaseImport;
import net.tnemc.core.TNE;
import net.tnemc.core.common.api.IDFinder;
import net.tnemc.core.economy.currency.Currency;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.math.BigDecimal;
import java.util.Set;

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
public class GemsEconomy extends Converter {
  private File configFile = new File("plugins/GemsEconomy/config.yml");
  private FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);
  @Override
  public String name() {
    return "GemsEconomy";
  }

  @Override
  public void mysql() throws InvalidDatabaseImport {
    db = new MySQL(TNE.saveManager().getTNEManager());
    String table = config.getString("mysql.tableprefix") + "_balances";
    try {
      int index = mysqlDB().executeQuery("SELECT * FROM " + table + ";");

      while (mysqlDB().results(index).next()) {
        String uuid = mysqlDB().results(index).getString("account_id");
        Double balance = mysqlDB().results(index).getDouble("balance");
        Currency currency = TNE.manager().currencyManager().get(TNE.instance().defaultWorld);
        if(TNE.manager().currencyManager().contains(TNE.instance().defaultWorld, mysqlDB().results(index).getString("currency_id"))) {
          currency = TNE.manager().currencyManager().get(TNE.instance().defaultWorld, mysqlDB().results(index).getString("currency_id"));
        }
        ConversionModule.convertedAdd(IDFinder.getUsername(uuid), TNE.instance().defaultWorld, currency.name(), new BigDecimal(balance));
      }
    } catch(Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void yaml() throws InvalidDatabaseImport {
    File dataFile = new File("plugins/GemsEconomy/data.yml");
    FileConfiguration dataConfiguration = YamlConfiguration.loadConfiguration(dataFile);

    final ConfigurationSection accountSection = dataConfiguration.getConfigurationSection("accounts");
    if(accountSection != null) {
      final Set<String> accounts = accountSection.getKeys(false);
      for(String uuid : accounts) {
        final ConfigurationSection balanceSection = accountSection.getConfigurationSection(uuid + ".balances");
        if(balanceSection != null) {
          final Set<String> currencies = balanceSection.getKeys(false);
          for(String currency : currencies) {
            Currency cur = TNE.manager().currencyManager().get(TNE.instance().defaultWorld);
            if(TNE.manager().currencyManager().contains(TNE.instance().defaultWorld, currency)) {
              cur = TNE.manager().currencyManager().get(TNE.instance().defaultWorld, currency);
            }
            ConversionModule.convertedAdd(IDFinder.getUsername(uuid), TNE.instance().defaultWorld, cur.name(), new BigDecimal(config.getString("accounts." + uuid + ".balances." + currency)));
          }
        }
      }
    }
  }
}
