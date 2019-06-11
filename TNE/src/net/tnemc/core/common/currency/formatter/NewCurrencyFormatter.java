package net.tnemc.core.common.currency.formatter;

import net.tnemc.core.common.currency.TNECurrency;
import net.tnemc.core.common.currency.formatter.impl.ColourRule;
import net.tnemc.core.common.currency.formatter.impl.DecimalRule;
import net.tnemc.core.common.currency.formatter.impl.MajorAmountRule;
import net.tnemc.core.common.currency.formatter.impl.MajorNameRule;
import net.tnemc.core.common.currency.formatter.impl.MajorRule;
import net.tnemc.core.common.currency.formatter.impl.MaterialRule;
import net.tnemc.core.common.currency.formatter.impl.MinorAmountRule;
import net.tnemc.core.common.currency.formatter.impl.MinorNameRule;
import net.tnemc.core.common.currency.formatter.impl.MinorRule;
import net.tnemc.core.common.currency.formatter.impl.ShortenRule;
import net.tnemc.core.common.currency.formatter.impl.SymbolRule;
import org.bukkit.Location;

import java.math.BigDecimal;
import java.util.LinkedHashMap;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * Created by creatorfromhell on 6/10/2019.
 * <p>
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */
public class NewCurrencyFormatter {

  static LinkedHashMap<String, FormatRule> rulesMap = new LinkedHashMap<>();

  static {
    addRule(new ColourRule());
    addRule(new DecimalRule());
    addRule(new MajorAmountRule());
    addRule(new MajorNameRule());
    addRule(new MajorRule());
    addRule(new MinorAmountRule());
    addRule(new MinorNameRule());
    addRule(new MinorRule());
    addRule(new ShortenRule());
    addRule(new SymbolRule());
    addRule(new MaterialRule());
  }

  public static void addRule(FormatRule rule) {
    rulesMap.put(rule.name(), rule);
  }

  public static String format(TNECurrency currency, BigDecimal amount, Location location, String player) {
    String format = currency.getFormat();

    for(FormatRule rule : rulesMap.values()) {
      format = rule.format(currency, amount, location, player, format);
    }

    return format;
  }

  public static String format(TNECurrency currency, BigDecimal amount, Location location, String player, String... rules) {

    String format = currency.getFormat();

    for(String str : rules) {
      if(rulesMap.containsKey(str)) {
        format = rulesMap.get(str).format(currency, amount, location, player, format);
      }
    }

    return format;
  }
}