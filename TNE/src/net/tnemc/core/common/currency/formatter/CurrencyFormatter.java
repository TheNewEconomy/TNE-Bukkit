package net.tnemc.core.common.currency.formatter;

import net.tnemc.core.TNE;
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
import java.math.BigInteger;
import java.util.Collections;
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
public class CurrencyFormatter {

  static LinkedHashMap<String, FormatRule> rulesMap = new LinkedHashMap<>();

  static {
    addRule(new ShortenRule());

    addRule(new DecimalRule());
    addRule(new MajorAmountRule());
    addRule(new MajorNameRule());
    addRule(new MajorRule());
    addRule(new MinorAmountRule());
    addRule(new MinorNameRule());
    addRule(new MinorRule());
    addRule(new SymbolRule());
    addRule(new ColourRule());
    addRule(new MaterialRule());
  }

  public static void addRule(FormatRule rule) {
    rulesMap.put(rule.name(), rule);
  }

  public static String format(TNECurrency currency, String world, BigDecimal amount, String player) {
    return format(currency, amount, null, player);
  }

  public static String format(TNECurrency currency, BigDecimal amount, Location location, String player) {
    //TNE.debug("Format: " + currency.getFormat());
    amount = amount.setScale(currency.decimalPlaces(), BigDecimal.ROUND_CEILING);

    String format = currency.getFormat();

    for(FormatRule rule : rulesMap.values()) {
      format = rule.format(currency, amount, location, player, format);
    }

    return format;
  }

  public static String format(TNECurrency currency, BigDecimal amount, Location location, String player, String... rules) {
    amount = amount.setScale(currency.decimalPlaces(), BigDecimal.ROUND_CEILING);

    String format = currency.getFormat();

    for(String str : rules) {
      if(rulesMap.containsKey(str)) {
        format = rulesMap.get(str).format(currency, amount, location, player, format);
      }
    }

    return format;
  }

  public static BigDecimal round(TNECurrency currency, BigDecimal amount) {
    return amount.setScale(currency.decimalPlaces(), BigDecimal.ROUND_CEILING);
  }

  public static String parseAmount(TNECurrency currency, String world, String amount) {
    if(amount.length() > 40) return "Messages.Money.ExceedsCurrencyMaximum";
    if(isBigDecimal(amount, currency)) {
      BigDecimal translated = translateBigDecimal(amount, currency);
      if(translated.compareTo(currency.getMaxBalance()) > 0) {
        return "Messages.Money.ExceedsCurrencyMaximum";
      }
      translated = parseWeight(currency, translated);
      return translated.toPlainString();
    }
    String updated = amount.replaceAll(" ", "");
    if(!currency.getPrefixes().contains(updated.charAt(updated.length() - 1) + "")) {
      return "Messages.Money.InvalidFormat";
    }
    return fromShort(currency, updated);
  }

  private static BigDecimal parseWeight(TNECurrency currency, BigDecimal decimal) {
    String[] amountStr = (decimal.toPlainString() + (decimal.toPlainString().contains(".")? "" : ".00")).split("\\.");
    BigInteger major = new BigInteger(amountStr[0]);
    // Get a string that is exactly as long as there are decimal points.
    final String truncatedMinor =
        // make it longer
        (amountStr[1] + String.join("",
            Collections.nCopies(Math.max(0, currency.getDecimalPlaces() - amountStr[1].length()), "0")))
            // make it shorter
            .substring(0, currency.getDecimalPlaces());
    BigInteger minor = new BigInteger(truncatedMinor);
    BigInteger majorConversion = minor;
    majorConversion = majorConversion.divide(new BigInteger(currency.getMinorWeight() + ""));
    major = major.add(majorConversion);

    if(currency.useMinor()) {
      minor = minor.mod(new BigInteger(currency.getMinorWeight() + ""));
    }
    final String minorFinal = (currency.useMinor())? String.format("%0" + currency.getDecimalPlaces() + "d", Integer.valueOf(minor.toString())).replace(' ', '0') : "";

    String toParse = major.toString();
    if(currency.useMinor()) {
      toParse += "." + minorFinal;
    }

    return new BigDecimal(toParse);
  }

  private static String fromShort(TNECurrency currency, String amount) {
    int charIndex = currency.getPrefixes().indexOf(amount.charAt(amount.length() - 1)) + 1;
    String sub = amount.substring(0, amount.length() - 1);
    String form = "%-" + ((charIndex * 3) + sub.length()) + "s";
    return String.format(form, sub).replace(' ', '0');
  }

  private static boolean isBigDecimal(String value, TNECurrency currency) {
    try {
      new BigDecimal(value.replace(currency.getDecimal(), "."));
      return true;
    } catch(Exception e) {
      return false;
    }
  }

  public static BigDecimal translateBigDecimal(String value, String world) {
    return translateBigDecimal(value, TNE.manager().currencyManager().get(world));
  }

  public static BigDecimal translateBigDecimal(String value, TNECurrency currency) {
    return new BigDecimal(value);
  }
}