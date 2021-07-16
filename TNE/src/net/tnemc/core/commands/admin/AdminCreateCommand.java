package net.tnemc.core.commands.admin;

import net.tnemc.commands.core.CommandExecution;
import net.tnemc.commands.core.provider.PlayerProvider;
import net.tnemc.core.TNE;
import net.tnemc.core.common.Message;
import net.tnemc.core.common.WorldVariant;
import net.tnemc.core.common.account.TNEAccount;
import net.tnemc.core.common.account.WorldFinder;
import net.tnemc.core.common.api.IDFinder;
import net.tnemc.core.common.currency.formatter.CurrencyFormatter;
import net.tnemc.core.common.utils.MISCUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by Daniel on 7/10/2017.
 */
public class AdminCreateCommand implements CommandExecution {

  @Override
  public boolean execute(PlayerProvider provider, String label, String[] arguments) {
    CommandSender sender = MISCUtils.getSender(provider);
    if(arguments.length >= 1) {
      String world = WorldFinder.getWorld(sender, WorldVariant.BALANCE);
      UUID id = IDFinder.getID(arguments[0]);
      if(!TNE.manager().exists(id)) {
        BigDecimal initial = BigDecimal.ZERO;
        if(arguments.length >= 2) {
          try {
            initial = new BigDecimal(arguments[1]);
          } catch(Exception ex) {
            TNE.debug("Invalid amount: " + arguments[1]);
          }
        }
        TNEAccount acc = new TNEAccount(id, arguments[0]);
        TNE.manager().addAccount(acc);
        acc.initializeHoldings(world);
        if(initial.compareTo(BigDecimal.ZERO) > 0) {
          if(sender instanceof Player) {
            acc.setHoldings(world, MISCUtils.findCurrencyName(world, ((Player) sender).getLocation()), initial);
          } else {
            acc.setHoldings(world, TNE.manager().currencyManager().get(world).name(), initial);
          }
        }
        TNE.manager().addAccount(acc);

        Message m = new Message("Messages.Admin.Created");
        m.addVariable("$player", arguments[0]);
        m.addVariable("$amount", CurrencyFormatter.format(TNE.manager().currencyManager().get(world), world, TNE.instance().api().getHoldings(id.toString(), world), id.toString()));
        m.translate(world, sender);
        return true;
      }
      new Message("Messages.Admin.Exists").translate(world, sender);
      return false;
    }
    MISCUtils.help(sender, label, arguments);
    return false;
  }
}