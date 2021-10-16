package net.tnemc.core.common.currency.formatter.impl;

import net.tnemc.core.TNE;
import net.tnemc.core.common.api.IDFinder;
import net.tnemc.core.common.currency.ItemCalculations;
import net.tnemc.core.common.currency.TNECurrency;
import net.tnemc.core.common.currency.TNETier;
import net.tnemc.core.common.currency.formatter.FormatRule;
import net.tnemc.core.common.utils.MISCUtils;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.math.BigDecimal;
import java.util.UUID;

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
public class MaterialRule implements FormatRule {
  @Override
  public String name() {
    return "material";
  }

  @Override
  public String format(TNECurrency currency, BigDecimal amount, Location location, String player, String formatted) {
    String format = formatted;

    if(player != null && !player.equalsIgnoreCase("") && currency.isItem()) {
      final UUID id = IDFinder.getID(player);
      TNE.debug("PLAYER: " + player);
      TNE.debug("ID: " + id.toString());
      if(id != null) {
        Player p = MISCUtils.getPlayer(id);
        if(p != null) {
          for(TNETier tier : currency.getTNETiers()) {
            if(format.contains("<" + tier.singular() + ">")) {
              format = format.replace("<" + tier.singular() + ">", "" + ItemCalculations.getCount(tier.getItemInfo().toStack(), p.getInventory()));
            }
          }
        }
      }
    }

    return format;
  }
}
