package net.tnemc.conversion.impl;

import net.tnemc.conversion.ConversionModule;
import net.tnemc.conversion.Converter;
import net.tnemc.conversion.InvalidDatabaseImport;
import net.tnemc.core.TNE;
import net.tnemc.core.common.currency.TNECurrency;
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
public class Meller extends Converter {
  private File configFile = new File(TNE.instance().getDataFolder(), "../Meller/economy.yml");
  private FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);
  @Override
  public String name() {
    return "Meller";
  }

  @Override
  public String type() {
    return "yaml";
  }

  @Override
  public File dataFolder() {
    return new File(TNE.instance().getDataFolder(), "../Meller/config.yml");
  }

  @Override
  public void yaml() throws InvalidDatabaseImport {

    final ConfigurationSection accountSection = config.getConfigurationSection("accounts");
    if(accountSection != null) {
      final Set<String> accounts = accountSection.getKeys(false);
      for(String uuid : accounts) {
        TNECurrency cur = TNE.manager().currencyManager().get(TNE.instance().defaultWorld);
        ConversionModule.convertedAdd(uuid, TNE.instance().defaultWorld, cur.name(), new BigDecimal(config.getString("accounts." + uuid + ".balance")));
      }
    }
  }
}
