package net.tnemc.core.common.api;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import net.tnemc.core.TNE;
import net.tnemc.core.common.currency.CurrencyFormatter;
import org.bukkit.entity.Player;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * Created by Daniel on 9/13/2018.
 * <p>
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */
public class EconomyPlaceholders extends PlaceholderExpansion {
  @Override
  public String getIdentifier() {
    return "tne";
  }

  @Override
  public String getPlugin() {
    return null;
  }

  @Override
  public String getAuthor() {
    return "creatorfromhell";
  }

  @Override
  public String getVersion() {
    return "0.1.1.3";
  }

  @Override
  public String onPlaceholderRequest(Player player, String identifier) {

    if(player == null) {
      return null;
    }

    final String id = IDFinder.getID(player).toString();

    //%tne_balance%
    if(identifier.equalsIgnoreCase("balance")) {
      return CurrencyFormatter.format(
          TNE.instance().defaultWorld,
          TNE.instance().api().getHoldings(id)
      );
    }

    final String[] args = identifier.split("_");

    //%tne_wcur_<world name>_<currency name>
    if(identifier.toLowerCase().contains("wcur_")) {
      if(args.length >= 3) {
        return CurrencyFormatter.format(
            args[1],
            args[2],
            TNE.instance().api().getHoldings(id, args[1], TNE.manager().currencyManager().get(args[1], args[2]))
        );
      }
    }

    //%tne_world_<world name>%
    if(identifier.toLowerCase().contains("world_")) {
      if(args.length >= 2) {
        return CurrencyFormatter.format(
            args[1],
            TNE.instance().api().getHoldings(id, args[1])
        );
      }
    }

    //%tne_currency_<currency name>%
    if(identifier.toLowerCase().contains("currency_")) {
      if(args.length >= 2) {
        return CurrencyFormatter.format(
            TNE.instance().defaultWorld,
            args[2],
            TNE.instance().api().getHoldings(id, TNE.instance().defaultWorld, TNE.manager().currencyManager().get(TNE.instance().defaultWorld, args[2]))
        );
      }
    }
    return null;
  }
}