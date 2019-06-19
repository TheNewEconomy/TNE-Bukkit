package net.tnemc.core.common.currency.formatter.impl;

import net.tnemc.core.common.currency.TNECurrency;
import net.tnemc.core.common.currency.formatter.FormatRule;
import org.bukkit.Location;

import java.math.BigDecimal;

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
public class MajorAmountRule implements FormatRule {
  @Override
  public String name() {
    return "major_amount";
  }

  @Override
  public String format(TNECurrency currency, BigDecimal amount, Location location, String player, String formatted) {
    String amountFormatted = (currency.canSeparateMajor())? separate(amount.toBigInteger().toString().split("\\."), currency.getMajorSeparator()) : amount.toBigInteger().toString();
    return formatted.replace("<major.amount>", amountFormatted);
  }

  public static String separate(String[] format, String separator) {
    String whole = format[0];

    StringBuilder builder = new StringBuilder();

    final int leftOver = whole.length() % 3;
    boolean hasLeft = leftOver > 0;

    for(int i = 0; i < whole.length(); i++) {
      final int actual = (i + 1) - leftOver;
      builder.append(whole.charAt(i));
      if(i == whole.length() - 1) continue;
      if(hasLeft && i == (leftOver - 1)) {
        builder.append(separator);
        hasLeft = false;
        continue;
      }

      if(!hasLeft && actual > 0 && (actual % 3) == 0) {
        builder.append(separator);
      }
    }
    return builder.toString();
  }
}