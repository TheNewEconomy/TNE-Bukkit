package net.tnemc.core.common.currency.formatter;

import net.tnemc.core.common.currency.TNECurrency;
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
public interface FormatRule {

  String name();

  String format(TNECurrency currency, BigDecimal amount, Location location, String player, String formatted);
}