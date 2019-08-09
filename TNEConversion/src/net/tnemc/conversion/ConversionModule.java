package net.tnemc.conversion;

import net.tnemc.conversion.command.ConvertCommand;
import net.tnemc.conversion.impl.AdvancedEconomy;
import net.tnemc.conversion.impl.BConomy;
import net.tnemc.conversion.impl.BEconomy;
import net.tnemc.conversion.impl.BOSEconomy;
import net.tnemc.conversion.impl.BasicEconomy;
import net.tnemc.conversion.impl.Blings;
import net.tnemc.conversion.impl.CMI;
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
import net.tnemc.conversion.impl.TownyEco;
import net.tnemc.conversion.impl.XConomy;
import net.tnemc.conversion.impl.iConomy;
import net.tnemc.core.TNE;
import net.tnemc.core.commands.TNECommand;
import net.tnemc.core.common.module.Module;
import net.tnemc.core.common.module.ModuleInfo;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

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
    version = "0.1.0",
    updateURL = "https://tnemc.net/files/module-version.xml"
)
public class ConversionModule implements Module {

  private static ConversionModule instance;
  
  private Converter converter = null;


  @Override
  public void load(TNE tne) {
    tne.getLogger().info("Conversion Module loaded!");
    instance = this;
  }

  @Override
  public void unload(TNE tne) {

    converter = null;
    tne.logger().info("Conversion Module unloaded!");
  }

  @Override
  public List<TNECommand> commands() {
    return Collections.singletonList(new ConvertCommand(TNE.instance()));
  }

  public Converter getConverter(String name) {

    switch(name.toLowerCase()) {
      case "advancedeconomy":
        converter = new AdvancedEconomy();
      case "basiceconomy":
        converter = new BasicEconomy();
      case "bconomy":
        converter = new BConomy();
      case "beconomy":
        converter = new BEconomy();
      case "blings":
        converter = new Blings();
      case "boseconomy":
        converter = new BOSEconomy();
      case "cmi":
        converter = new CMI();
      case "devcoinsystem":
        converter = new DevCoinSystem();
      case "easycoins":
        converter = new EasyCoins();
      case "ececonomy":
        converter = new ECEconomy();
      case "economyapi":
        converter = new EconomyAPI();
      case "ecoplugin":
        converter = new EcoPlugin();
      case "ecosystem":
        converter = new EcoSystem();
      case "essentials":
        converter = new Essentials();
      case "feathereconomy":
        converter = new FeatherEconomy();
      case "feconomy":
        converter = new FeConomy();
      case "gemseconomy":
        converter = new GemsEconomy();
      case "iconomy":
        converter = new iConomy();
      case "meep":
        converter = new Meep();
      case "meller":
        converter = new Meller();
      case "minecoin":
        converter = new MineCoin();
      case "minecoinsyml":
        converter = new MineCoinsYML();
      case "mineconomy":
        converter = new MineConomy();
      case "minetopia":
        converter = new MinetopiaEconomy();
      case "moconomy":
        converter = new MoConomy();
      case "realeconomy":
        converter = new RealEconomy();
      case "saneeconomy":
        converter = new SaneEconomy();
      case "simpleconomy":
        converter = new SimpleConomy();
      case "simplisticeconomy":
        converter = new SimplisticEconomy();
      case "sqlconomy":
        converter = new SQLConomy();
      case "swifteconomy":
        converter = new SwiftEconomy();
      case "tokenseconomy":
        converter = new TokensEconomy();
      case "townyeco":
        converter = new TownyEco();
      case "xconomy":
        converter = new XConomy();
    }
    return converter;
  }

  public static void convertedAdd(String identifier, String world, String currency, BigDecimal amount) {
    File conversionFile = new File(TNE.instance().getDataFolder(), "extracted.yml");
    FileConfiguration conversion = YamlConfiguration.loadConfiguration(conversionFile);

    if(!conversion.contains("Accounts")) {
      conversion.createSection("Accounts");
      try {
        conversion.save(conversionFile);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }


    BigDecimal starting = BigDecimal.ZERO;

    String newID = identifier.replaceAll("\\.", "!").replaceAll("\\-", "@").replaceAll("\\_", "%");

    if(conversion.contains("Accounts." + newID + ".Balances." + world + "." + currency)) {
      starting = new BigDecimal(conversion.getString("Accounts." + newID + ".Balances." + world + "." + currency));
    }

    conversion.set("Accounts." + newID + ".Balances." + world + "." + currency, starting.add(amount).toPlainString());
    try {
      conversion.save(conversionFile);
    } catch (IOException e) {
      TNE.debug(e);
    }
  }

  public static ConversionModule instance() {
    return instance;
  }
}