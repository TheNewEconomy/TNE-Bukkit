package net.tnemc.conversion;

import net.tnemc.conversion.impl.AdvancedEconomy;
import net.tnemc.conversion.impl.BConomy;
import net.tnemc.conversion.impl.BEconomy;
import net.tnemc.conversion.impl.BOSEconomy;
import net.tnemc.conversion.impl.BasicEconomy;
import net.tnemc.conversion.impl.Blings;
import net.tnemc.conversion.impl.CraftConomy;
import net.tnemc.conversion.impl.DevCoinSystem;
import net.tnemc.conversion.impl.ECEconomy;
import net.tnemc.conversion.impl.EasyCoins;
import net.tnemc.conversion.impl.EcoPlugin;
import net.tnemc.conversion.impl.EcoSystem;
import net.tnemc.conversion.impl.EconomyAPI;
import net.tnemc.conversion.impl.Essentials;
import net.tnemc.conversion.impl.FeConomy;
import net.tnemc.conversion.impl.FeatherEconomy;
import net.tnemc.conversion.impl.GemsEconomy;
import net.tnemc.conversion.impl.Meep;
import net.tnemc.conversion.impl.Meller;
import net.tnemc.conversion.impl.MineCoin;
import net.tnemc.conversion.impl.MineCoinsYML;
import net.tnemc.conversion.impl.MineConomy;
import net.tnemc.conversion.impl.MinetopiaEconomy;
import net.tnemc.conversion.impl.MoConomy;
import net.tnemc.conversion.impl.RealEconomy;
import net.tnemc.conversion.impl.SQLConomy;
import net.tnemc.conversion.impl.SaneEconomy;
import net.tnemc.conversion.impl.SimpleConomy;
import net.tnemc.conversion.impl.SimplisticEconomy;
import net.tnemc.conversion.impl.SwiftEconomy;
import net.tnemc.conversion.impl.TokensEconomy;
import net.tnemc.conversion.impl.XConomy;
import net.tnemc.conversion.impl.iConomy;
import net.tnemc.core.TNE;
import net.tnemc.core.common.account.TNEAccount;
import net.tnemc.core.common.api.IDFinder;
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
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
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
    tne.getLogger().info("Conversion Module loaded!");
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
      case "advancedeconomy":
        return new AdvancedEconomy();
      case "basiceconomy":
        return new BasicEconomy();
      case "bconomy":
        return new BConomy();
      case "beconomy":
        return new BEconomy();
      case "blings":
        return new Blings();
      case "boseconomy":
        return new BOSEconomy();
      case "craftconomy":
        return new CraftConomy();
      case "devcoinsystem":
        return new DevCoinSystem();
      case "easycoins":
        return new EasyCoins();
      case "ececonomy":
        return new ECEconomy();
      case "economyapi":
        return new EconomyAPI();
      case "ecoplugin":
        return new EcoPlugin();
      case "ecosystem":
        return new EcoSystem();
      case "essentials":
        return new Essentials();
      case "feathereconomy":
        return new FeatherEconomy();
      case "feconomy":
        return new FeConomy();
      case "gemseconomy":
        return new GemsEconomy();
      case "iconomy":
        return new iConomy();
      case "meep":
        return new Meep();
      case "meller":
        return new Meller();
      case "minecoin":
        return new MineCoin();
      case "minecoinsyml":
        return new MineCoinsYML();
      case "mineconomy":
        return new MineConomy();
      case "minetopia":
        return new MinetopiaEconomy();
      case "moconomy":
        return new MoConomy();
      case "realeconomy":
        return new RealEconomy();
      case "saneeconomy":
        return new SaneEconomy();
      case "simpleconomy":
        return new SimpleConomy();
      case "simplisticeconomy":
        return new SimplisticEconomy();
      case "sqlconomy":
        return new SQLConomy();
      case "swifteconomy":
        return new SwiftEconomy();
      case "tokenseconomy":
        return new TokensEconomy();
      case "xconomy":
        return new XConomy();
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