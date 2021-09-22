package net.tnemc.conversion;

import net.tnemc.conversion.impl.AConomy;
import net.tnemc.conversion.impl.AdvancedEconomy;
import net.tnemc.conversion.impl.BConomy;
import net.tnemc.conversion.impl.BEconomy;
import net.tnemc.conversion.impl.BOSEconomy;
import net.tnemc.conversion.impl.BasicEconomy;
import net.tnemc.conversion.impl.Blings;
import net.tnemc.conversion.impl.CMI;
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
import net.tnemc.conversion.impl.GMoney;
import net.tnemc.conversion.impl.GemsEconomy;
import net.tnemc.conversion.impl.Meep;
import net.tnemc.conversion.impl.Meller;
import net.tnemc.conversion.impl.MineCoin;
import net.tnemc.conversion.impl.MineCoinsYML;
import net.tnemc.conversion.impl.MineConomy;
import net.tnemc.conversion.impl.MinetopiaEconomy;
import net.tnemc.conversion.impl.MoConomy;
import net.tnemc.conversion.impl.MySQLBridge;
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
import net.tnemc.conversion.impl.iConomy7;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * Created by creatorfromhell on 7/25/2021.
 * <p>
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */
public class ConverterManager {

  private LinkedHashMap<String, Converter> converters = new LinkedHashMap<>();

  public ConverterManager() {
    add(new AConomy());
    add(new AdvancedEconomy());
    add(new BasicEconomy());
    add(new BConomy());
    add(new BEconomy());
    add(new Blings());
    add(new BOSEconomy());
    add(new CMI());
    add(new CraftConomy());
    add(new DevCoinSystem());
    add(new EasyCoins());
    add(new ECEconomy());
    add(new EconomyAPI());
    add(new EcoPlugin());
    add(new EcoSystem());
    add(new Essentials());
    add(new FeatherEconomy());
    add(new FeConomy());
    add(new GemsEconomy());
    add(new GMoney());
    add(new iConomy7());
    add(new iConomy());
    add(new Meep());
    add(new Meller());
    add(new MineCoin());
    add(new MineCoinsYML());
    add(new MineConomy());
    add(new MinetopiaEconomy());
    add(new MoConomy());
    add(new MySQLBridge());
    add(new RealEconomy());
    add(new SaneEconomy());
    add(new SimpleConomy());
    add(new SimplisticEconomy());
    add(new SQLConomy());
    add(new SwiftEconomy());
    add(new TokensEconomy());
    add(new TownyEco());
    add(new XConomy());
  }

  public LinkedHashMap<String, Converter> getConverters() {
    return converters;
  }

  public void add(Converter converter) {
    converters.put(converter.name(), converter);
  }

  public List<String> search() {
    List<String> found = new ArrayList<>();

    Iterator<Map.Entry<String, Converter>> it = converters.entrySet().iterator();

    while(it.hasNext()) {
      Map.Entry<String, Converter> entry = it.next();

      if(entry.getValue().dataFolder() != null) {
        if(entry.getValue().dataFolder().exists()) found.add(entry.getKey());
      }
    }
    return found;
  }

  public Optional<Converter> find(String name) {
    return Optional.of(converters.get(name));
  }
}