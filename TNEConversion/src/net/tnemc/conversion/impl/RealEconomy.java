package net.tnemc.conversion.impl;

import net.tnemc.conversion.ConversionModule;
import net.tnemc.conversion.Converter;
import net.tnemc.conversion.InvalidDatabaseImport;
import net.tnemc.core.TNE;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.math.BigDecimal;

/**
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */
public class RealEconomy extends Converter {
  private File dataDirectory = new File(TNE.instance().getDataFolder(), "../RealEconomy/userdata");

  @Override
  public String name() {
    return "RealEconomy";
  }

  @Override
  public String type() {
    return "yaml";
  }

  @Override
  public void yaml() throws InvalidDatabaseImport {
    if(!dataDirectory.isDirectory() || dataDirectory.listFiles() == null || dataDirectory.listFiles().length == 0) return;

    for(File accountFile : dataDirectory.listFiles()) {
      FileConfiguration acc = YamlConfiguration.loadConfiguration(accountFile);

      Double money = acc.contains("guthaben")? acc.getDouble("guthaben") : 0.0;
      String currency = TNE.manager().currencyManager().get(TNE.instance().defaultWorld).name();

      ConversionModule.convertedAdd(accountFile.getName().replace(".yml", ""), TNE.instance().defaultWorld, currency, new BigDecimal(money));
    }
  }
}