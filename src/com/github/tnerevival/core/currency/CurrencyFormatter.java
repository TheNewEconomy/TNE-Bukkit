package com.github.tnerevival.core.currency;

import com.github.tnerevival.TNE;
import com.github.tnerevival.core.Message;
import com.github.tnerevival.utils.AccountUtils;
import com.github.tnerevival.utils.MISCUtils;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

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
 * Created by creatorfromhell on 10/22/2016.
 */
public class CurrencyFormatter {

  public static String format(String world, BigDecimal amount) {
    MISCUtils.debug("CurrencyFormatter.format(" + world + ", " + amount.doubleValue() + ")");
    return format(TNE.instance().manager.currencyManager.get(world), world, amount);
  }

  public static String format(String world, String name, BigDecimal amount) {
    MISCUtils.debug("CurrencyFormatter.format(" + name + ", " + world + ", " + amount.doubleValue() + ")");
    return format(TNE.instance().manager.currencyManager.get(world, name), world, amount);
  }

  public static String format(Currency currency, String world, BigDecimal amount) {
    MISCUtils.debug("CurrencyFormatter.format(" + currency.getName() + ", " + world + ", " + amount.doubleValue() + ")");

    if(currency == null) currency = TNE.instance().manager.currencyManager.get(TNE.instance().defaultWorld);

    amount = AccountUtils.round(currency.getName(), world, amount);
    MISCUtils.debug(currency.getName() + " World: " + currency);

    String shortFormat = "<symbol><short.amount>";
    String format = currency.getFormat();

    String[] amountStr = (String.valueOf(amount) + (String.valueOf(amount).contains(".")? "" : ".00")).split("\\.");
    Integer major = Integer.parseInt(amountStr[0]);
    Integer minor = Integer.parseInt((amountStr[1].length() == 1)? amountStr[1] + "0" : amountStr[1]);
    String majorName = (major == 1)? currency.getTier("Major").getSingle() : currency.getTier("Major").getPlural();
    String minorName = (minor == 1)? currency.getTier("Minor").getSingle() : currency.getTier("Minor").getPlural();

    Map<String, String> replacements = new HashMap<>();
    replacements.put("<symbol>", currency.getTier("Major").getSymbol());
    replacements.put("<decimal>", currency.getDecimal());
    replacements.put("<major>", major + " " + majorName);
    replacements.put("<minor>", minor + " " + minorName);
    replacements.put("<major.name>", majorName);
    replacements.put("<minor.name>", minorName);
    replacements.put("<major.amount>", major + "");
    replacements.put("<minor.amount>", minor + "");
    replacements.put("<short.amount>", shorten(amount, currency.getDecimal()));
    replacements.putAll(Message.colours);

    String formatted = (currency.shorten())? shortFormat : format;

    for(Map.Entry<String, String> entry : replacements.entrySet()) {
      formatted = formatted.replace(entry.getKey(), entry.getValue());
    }
    return formatted;
  }

  private static int getWhole(BigDecimal number) {
    BigDecimal bigDecimal = number.setScale(2, BigDecimal.ROUND_HALF_UP);
    BigDecimal fractionValue =  bigDecimal.remainder(BigDecimal.ONE);
    return bigDecimal.subtract(fractionValue).setScale(0).intValue();
  }

  private static String shorten(BigDecimal balance, String decimal) {
    Integer dollars = getWhole(balance);
    if (dollars < 1000) {
      return "" + dollars;
    }
    int exp = (int) (Math.log(dollars) / Math.log(1000));
    return String.format("%" + decimal + "1f%c", dollars / Math.pow(1000, exp), "kMGTPE".charAt(exp - 1));
  }

  public static Boolean isDouble(String value, String world) {
    String major = TNE.instance().manager.currencyManager.get(world).getMajor();
    return isDouble(value, major, world);
  }

  public static Boolean isDouble(String value, String currency, String world) {
    String decimal = TNE.instance().manager.currencyManager.get(world, currency).getDecimal();
    try {
      Double.valueOf(value.replace(decimal, "."));
      return true;
    } catch(Exception e) {
      return false;
    }
  }

  public static Double translateDouble(String value, String world) {
    String major = TNE.instance().manager.currencyManager.get(world).getMajor();
    return translateDouble(value, major, world);
  }

  public static Double translateDouble(String value, String currency, String world) {
    String decimal = TNE.instance().manager.currencyManager.get(world, currency).getDecimal();
    return Double.valueOf(value.replace(decimal, "."));
  }

  public static BigDecimal translateBigDecimal(String value, String world) {
    String major = TNE.instance().manager.currencyManager.get(world).getMajor();
    return translateBigDecimal(value, major, world);
  }

  public static BigDecimal translateBigDecimal(String value, String currency, String world) {
    return new BigDecimal(translateDouble(value, currency, world));
  }
}