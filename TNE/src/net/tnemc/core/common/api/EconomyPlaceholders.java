package net.tnemc.core.common.api;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import net.tnemc.core.TNE;
import net.tnemc.core.common.currency.formatter.CurrencyFormatter;
import org.bukkit.entity.Player;

import java.sql.SQLException;

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

  private TNE plugin;

  public EconomyPlaceholders(TNE plugin) {
    this.plugin = plugin;
  }
  @Override
  public String getIdentifier() {
    return "tne";
  }

  @Override
  public String getPlugin() {
    return "TheNewEconomy";
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
  public boolean persist() {
    return true;
  }

  @Override
  public boolean canRegister() {
    return true;
  }

  @Override
  public String onPlaceholderRequest(Player player, String identifier) {

    if(player == null) {
      return null;
    }

    final String id = IDFinder.getID(player).toString();

    final String[] args = identifier.split("_");

    //%tne_balance%
    if(identifier.contains("balance")) {
      if(args.length >= 2 && args[1].equalsIgnoreCase("formatted")) {
        return CurrencyFormatter.format(
            TNE.manager().currencyManager().get(TNE.instance().defaultWorld),
            TNE.instance().defaultWorld,
            TNE.instance().api().getHoldings(id),
            id
        );
      } else {
        return TNE.instance().api().getHoldings(id).toPlainString();
      }
    }

    //%tne_wcur_<world name>_<currency name>
    if(identifier.toLowerCase().contains("wcur_")) {
      if(args.length >= 3) {
        if(args.length >= 4 && args[3].equalsIgnoreCase("formatted")) {
          return CurrencyFormatter.format(
              TNE.manager().currencyManager().get(args[1], args[2]),
              args[1],
              TNE.instance().api().getHoldings(id, args[1], TNE.manager().currencyManager().get(args[1], args[2])),
              id
          );
        } else {
          return TNE.instance().api().getHoldings(id, args[1], TNE.manager().currencyManager().get(args[1], args[2])).toPlainString();
        }
      }
    }

    //%tne_world_<world name>%
    if(identifier.toLowerCase().contains("world_")) {
      if(args.length >= 2) {
        if(args.length >= 3 && args[2].equalsIgnoreCase("formatted")) {
          return CurrencyFormatter.format(
              TNE.manager().currencyManager().get(args[1]),
              args[1],
              TNE.instance().api().getHoldings(id, args[1]),
              id
          );
        } else {
          return TNE.instance().api().getHoldings(id, args[1], TNE.manager().currencyManager().get(args[1], args[2])).toPlainString();
        }
      }
    }

    //%tne_currency_<currency name>%
    if(identifier.toLowerCase().contains("currency_")) {
      if(args.length >= 2) {
        if(args.length >= 3 && args[2].equalsIgnoreCase("formatted")) {
          return CurrencyFormatter.format(
              TNE.manager().currencyManager().get(TNE.instance().defaultWorld, args[1]),
              TNE.instance().defaultWorld,
              TNE.instance().api().getHoldings(id, TNE.instance().defaultWorld, TNE.manager().currencyManager().get(TNE.instance().defaultWorld, args[1])),
              id
          );
        } else {
          return TNE.instance().api().getHoldings(id, args[1], TNE.manager().currencyManager().get(args[1], args[2])).toPlainString();
        }
      }
    }

    //%tne_toppos%
    //%tne_toppos_<world name or all>%
    //%tne_toppos_<world name or all>_<currency name or all>%
    if(identifier.toLowerCase().contains("toppos")) {
      int pos = 0;
      if(args.length == 1) {
        try {
          pos = TNE.saveManager().getTNEManager().getTNEProvider().topPos(id, TNE.instance().defaultWorld, TNE.manager().currencyManager().get(TNE.instance().defaultWorld).getIdentifier());
        } catch (SQLException e) {
          e.printStackTrace();
        }
      } else if(args.length == 2) {
        try {
          pos = TNE.saveManager().getTNEManager().getTNEProvider().topPos(id, args[1], TNE.manager().currencyManager().get(args[1]).getIdentifier());
        } catch (SQLException e) {
          e.printStackTrace();
        }
      } else if(args.length >= 3) {
        try {
          pos = TNE.saveManager().getTNEManager().getTNEProvider().topPos(id, args[1], args[2]);
        } catch (SQLException e) {
          e.printStackTrace();
        }
      }
      return pos + "";
    }
    return null;
  }
}