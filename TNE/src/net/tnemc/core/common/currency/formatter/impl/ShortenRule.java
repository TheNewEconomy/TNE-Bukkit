package net.tnemc.core.common.currency.formatter.impl;

import net.tnemc.core.common.currency.TNECurrency;
import net.tnemc.core.common.currency.formatter.FormatRule;
import org.bukkit.Location;

import java.math.BigDecimal;
import java.math.BigInteger;

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
public class ShortenRule implements FormatRule {
  @Override
  public String name() {
    return "shorten";
  }

  @Override
  public String format(TNECurrency currency, BigDecimal amount, Location location, String player, String formatted) {
    final BigInteger wholeNum = amount.toBigInteger();

    if(formatted.contains("<shorten>")) {
      formatted = "<symbol><short.amount>";
    }
    if (wholeNum.compareTo(new BigInteger("1009")) <= 0) {
      return formatted.replace("<short.amount>", wholeNum.toString());
    }
    final String whole = wholeNum.toString();
    final int pos = ((whole.length() - 1) / 3) - 1;
    final int posInclude = ((whole.length() % 3) == 0) ? 3 : whole.length() % 3;
    String wholeSub = whole.substring(0, posInclude);

    if (whole.length() > 3) {
      String extra = whole.substring(posInclude, posInclude + 2);
      if (Integer.valueOf(extra) > 0) {
        if (extra.endsWith("0")) {
          extra = extra.substring(0, extra.length() - 1);
        }
        wholeSub = wholeSub + "." + extra;
      }
    }
    char pre;
    if(currency.getPrefixes().length() < (pos + 1)) {
      pre = '^';
    } else {
      pre = currency.getPrefixes().charAt(pos);
    }

    return formatted.replace("<short.amount>", wholeSub + pre);
  }
}