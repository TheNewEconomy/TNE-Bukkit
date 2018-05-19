package net.tnemc.core.common.currency;

import net.tnemc.core.TNE;
import net.tnemc.core.common.Message;
import net.tnemc.core.economy.currency.Currency;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

/**
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by Daniel on 7/16/2017.
 */
public class CurrencyFormatter {

  public static String format(String world, BigDecimal amount) {
    TNE.debug("CurrencyFormatter.format(" + world + ", " + amount.doubleValue() + ")");
    return format(TNE.manager().currencyManager().get(world), world, amount);
  }

  public static String format(String world, String name, BigDecimal amount) {
    TNE.debug("CurrencyFormatter.format(" + name + ", " + world + ", " + amount.doubleValue() + ")");
    return format(TNE.manager().currencyManager().get(world, name), world, amount);
  }

  public static String format(Currency currency, String world, BigDecimal amount) {
    return format(TNE.manager().currencyManager().get(world, currency.name()), world, amount);
  }

  public static String format(TNECurrency currency, String world, BigDecimal amount) {
    TNE.debug("CurrencyFormatter.java(41): TNECurrency != null - " + (currency != null));
    TNE.debug("CurrencyFormatter.java(41): world != null - " + (world != null));
    TNE.debug("CurrencyFormatter.java(41): amount != null - " + (amount != null));
    TNE.debug("CurrencyFormatter.java(41): currency.name() != null - " + (currency.name() != null));
    TNE.debug("CurrencyFormatter.java(41): amount.toPlainString() != null - " + (amount.toPlainString() != null));
    TNE.debug("CurrencyFormatter.format(" + currency.name() + ", " + world + ", " + amount.toPlainString() + ")");

    if(currency == null) currency = TNE.manager().currencyManager().get(TNE.instance().defaultWorld);

    amount = round(world, currency.name(), amount);
    TNE.debug(currency.name() + " World: " + world);

    String shortFormat = "<symbol><short.amount>";
    String format = currency.getFormat();

    String[] amountStr = (String.valueOf(amount) + (String.valueOf(amount).contains(".")? "" : ".00")).split("\\.");
    BigInteger major = new BigInteger(amountStr[0]);
    BigInteger minor = new BigInteger(String.format("%1$-2s", Integer.valueOf(amountStr[1])).replace(' ', '0'));
    String majorName = (major.compareTo(BigInteger.ONE) == 0)? currency.name() : currency.plural();
    String minorName = (minor.compareTo(BigInteger.ONE) == 0)? currency.getSingleMinor() : currency.getPluralMinor();

    Map<String, String> replacements = new HashMap<>();
    replacements.put("<symbol>", currency.symbol());
    replacements.put("<decimal>", currency.getDecimal());
    replacements.put("<major>", major + " " + majorName);
    replacements.put("<minor>", minor + " " + minorName);
    replacements.put("<major.name>", majorName);
    replacements.put("<minor.name>", minorName);
    replacements.put("<major.amount>", major + "");
    replacements.put("<minor.amount>", minor + "");
    replacements.put("<short.amount>", shorten(currency, amount));
    replacements.putAll(Message.colours);

    String formatted = (currency.shorten())? shortFormat : format;

    for(Map.Entry<String, String> entry : replacements.entrySet()) {
      formatted = formatted.replace(entry.getKey(), entry.getValue());
    }
    TNE.debug("Formatted: " + formatted);
    return formatted;
  }

  public static BigDecimal round(String world, String currency, BigDecimal amount) {
    if(TNE.manager().currencyManager().contains(world, currency)) {
      return amount.setScale(TNE.manager().currencyManager().get(world, currency).decimalPlaces(), BigDecimal.ROUND_CEILING);
    }

    if(TNE.manager().currencyManager().contains(world)) {
      return amount.setScale(TNE.manager().currencyManager().get(world).decimalPlaces(), BigDecimal.ROUND_CEILING);
    }
    return amount.setScale(TNE.manager().currencyManager().get(TNE.instance().defaultWorld).decimalPlaces(), BigDecimal.ROUND_CEILING);
  }

  public static String parseAmount(TNECurrency currency, String world, String amount) {
    if(amount.length() > 40) return "Messages.Money.ExceedsCurrencyMaximum";
    if(isBigDecimal(amount, currency.name(), world)) {
      BigDecimal translated = translateBigDecimal(amount, currency.name(), world);
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
    String[] amountStr = (String.valueOf(decimal) + (String.valueOf(decimal).contains(".")? "" : ".00")).split("\\.");
    BigInteger major = new BigInteger(amountStr[0]);
    BigInteger minor = new BigInteger(String.format("%1$-2s", Integer.valueOf(amountStr[1])).replace(' ', '0'));
    BigInteger majorConversion = minor;
    majorConversion = majorConversion.divide(new BigInteger(currency.getMinorWeight() + ""));
    major = major.add(majorConversion);
    minor = minor.mod(new BigInteger(currency.getMinorWeight() + ""));

    return new BigDecimal(major.toString() + currency.getDecimal() + minor.toString());
  }

  private static String shorten(TNECurrency currency, BigDecimal balance) {
    String prefixes = currency.getPrefixes();
    BigInteger wholeNum = balance.toBigInteger();
    if (wholeNum.compareTo(new BigInteger("1000")) < 0) {
      return "" + wholeNum.toString();
    }
    String whole = wholeNum.toString();
    int pos = ((whole.length() - 1) / 3) - 1;
    int posInclude = ((whole.length() % 3) == 0)? 3 : whole.length() % 3;
    return whole.substring(0, posInclude) + prefixes.charAt(pos);
  }
  private static String fromShort(TNECurrency currency, String amount) {
    int charIndex = currency.getPrefixes().indexOf(amount.charAt(amount.length() - 1)) + 1;
    String sub = amount.substring(0, amount.length() - 1);
    String form = "%1$-" + ((charIndex * 3) + sub.length()) + "s";
    return String.format(form, Integer.valueOf(sub)).replace(' ', '0');
  }

  public static boolean isBigDecimal(String value, String world) {
    String major = TNE.manager().currencyManager().get(world).name();
    return isBigDecimal(value, major, world);
  }

  private static boolean isBigDecimal(String value, String currency, String world) {
    String decimal = (TNE.manager().currencyManager().get(world, currency)).getDecimal();
    try {
      new BigDecimal(value.replace(decimal, "."));
      return true;
    } catch(Exception e) {
      return false;
    }
  }

  public static BigDecimal translateBigDecimal(String value, String world) {
    String major = TNE.manager().currencyManager().get(world).name();
    return translateBigDecimal(value, major, world);
  }

  public static BigDecimal translateBigDecimal(String value, String currency, String world) {
    String decimal = (TNE.manager().currencyManager().get(world, currency)).getDecimal();
    return new BigDecimal(value.replace(decimal, "."));
  }
}