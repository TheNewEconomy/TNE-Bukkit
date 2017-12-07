package net.tnemc.conversion;

import com.github.tnerevival.user.IDFinder;
import net.tnemc.conversion.impl.*;
import net.tnemc.core.TNE;
import net.tnemc.core.common.account.TNEAccount;
import net.tnemc.core.common.module.Module;
import net.tnemc.core.common.module.ModuleInfo;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * All rights reserved.
 **/
@ModuleInfo(
    name = "Conversion",
    author = "creatorfromhell",
    version = "0.1.0"
)
public class ConversionModule extends Module {

  @Override
  public void load(TNE tne, String version) {
    tne.logger().info("Conversion Module loaded!");
    mainConfigurations.put("Core.Conversion.Convert", false);
    mainConfigurations.put("Core.Conversion.Name", "iConomy");
    mainConfigurations.put("Core.Conversion.Format", "MySQL");
    mainConfigurations.put("Core.Conversion.Options.Host", "localhost");
    mainConfigurations.put("Core.Conversion.Options.Port", 3306);
    mainConfigurations.put("Core.Conversion.Options.Database", "sql_eco");
    mainConfigurations.put("Core.Conversion.Options.User", "root");
    mainConfigurations.put("Core.Conversion.Options.Password", "Password");
    listeners.add(new ConversionListener(tne));
  }

  @Override
  public void postLoad(TNE tne) {
    if(TNE.configurations().getBoolean("Core.Conversion.Convert")) {
      if(!new File(TNE.instance().getDataFolder(), "conversion.yml").exists()) {
        Converter converter = getConverter();
        if(converter != null) {
          converter.convert();
        }
      }
    }
  }

  @Override
  public void unload(TNE tne) {
    tne.logger().info("Conversion Module unloaded!");
  }

  public Converter getConverter() {
    String name = TNE.configurations().getString("Core.Conversion.Name").toLowerCase();

    switch(name) {
      case "iconomy":
        return new iConomy();
      case "bconomy":
        return new BConomy();
      case "boseconomy":
        return new BOSEconomy();
      case "essentials":
        return new Essentials();
      case "craftconomy":
        return new CraftConomy();
      case "mineconomy":
        return new MineConomy();
      case "feconomy":
        return new FeConomy();
    }
    return null;
  }

  public static TNEAccount convertAccount(String username) {
    UUID id = IDFinder.getID(username);
    TNEAccount account = TNE.manager().getAccount(id);
    if(new File(TNE.instance().getDataFolder(), "conversion.yml").exists()) {
      File conversionFile = new File(TNE.instance().getDataFolder(), "conversion.yml");
      FileConfiguration conversion = YamlConfiguration.loadConfiguration(conversionFile);

      if (conversion.contains("Converted." + username) || conversion.contains("Converted." + id.toString())) {
        String base = "Converted." + ((conversion.contains("Converted." + username)) ? username : id.toString());

        Set<String> worlds = conversion.getConfigurationSection(base).getKeys(false);

        for (String world : worlds) {
          String section = base + "." + world;

          Set<String> currencies = conversion.getConfigurationSection(section).getKeys(false);

          for(String currency : currencies) {
            if(conversion.contains(section + "." + currency + ".Amount")) {
              account.setHoldings(world, currency, new BigDecimal(conversion.getString(section + "." + currency + ".Amount")));
            }
          }
        }
        conversion.set(base, null);
        try {
          conversion.save(conversionFile);
        } catch(Exception e) {
          TNE.debug(e);
        }
      }
      return account;
    }
    return null;
  }

  public static void convertedAdd(String identifier, String world, String currency, BigDecimal amount) {
    File conversionFile = new File(TNE.instance().getDataFolder(), "conversion.yml");
    FileConfiguration conversion = YamlConfiguration.loadConfiguration(conversionFile);

    BigDecimal starting = BigDecimal.ZERO;

    if(conversion.contains("Converted." + identifier + "." + world + "." + currency + ".Amount")) {
      starting = new BigDecimal(conversion.getString("Converted." + identifier + "." + world + "." + currency + ".Amount"));
    }

    conversion.set("Converted." + identifier + "." + world + "." + currency + ".Amount", (starting.add(amount).toPlainString()));
    try {
      conversion.save(conversionFile);
    } catch (IOException e) {
      TNE.debug(e);
    }
  }
}