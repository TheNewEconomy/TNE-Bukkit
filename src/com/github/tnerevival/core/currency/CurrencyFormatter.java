package com.github.tnerevival.core.currency;

import com.github.tnerevival.TNE;

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

  public static String format(String world, double amount) {
    return format(TNE.instance.manager.currencyManager.get(world), amount);
  }

  public static String format(String world, String name, double amount) {
    return format(TNE.instance.manager.currencyManager.get(world, name), amount);
  }

  public static String format(Currency currency, double amount) {
    return "";
  }

  public static Double translateDouble(String value, String world) {
    String major = TNE.instance.manager.currencyManager.get(world).getMajor();
    return translateDouble(value, major, world);
  }

  public static Double translateDouble(String value, String currency, String world) {
    String decimal = TNE.instance.manager.currencyManager.get(world, currency).getDecimal();
    return Double.valueOf(value.replace(decimal, "."));
  }
}