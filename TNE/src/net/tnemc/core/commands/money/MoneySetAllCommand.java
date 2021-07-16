package net.tnemc.core.commands.money;

import net.tnemc.commands.core.CommandExecution;
import net.tnemc.commands.core.provider.PlayerProvider;
import net.tnemc.core.TNE;
import net.tnemc.core.common.Message;
import net.tnemc.core.common.WorldVariant;
import net.tnemc.core.common.account.WorldFinder;
import net.tnemc.core.common.api.IDFinder;
import net.tnemc.core.common.currency.TNECurrency;
import net.tnemc.core.common.currency.formatter.CurrencyFormatter;
import net.tnemc.core.common.utils.MISCUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.math.BigDecimal;
import java.sql.SQLException;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * Created by creatorfromhell on 6/27/2019.
 * <p>
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */
public class MoneySetAllCommand implements CommandExecution {

  @Override
  public boolean execute(PlayerProvider provider, String label, String[] arguments) {
    Bukkit.getScheduler().runTaskAsynchronously(TNE.instance(), ()->{
      CommandSender sender = MISCUtils.getSender(provider);
      if(arguments.length >= 1) {
        final String world = (arguments.length >= 2)? TNE.instance().getWorldManager(arguments[1]).getBalanceWorld() : WorldFinder.getWorld(sender, WorldVariant.BALANCE);

        if(world == null) {
          MISCUtils.help(sender, label, arguments);
          return;
        }

        if(TNE.instance().getWorldManager(world).isEconomyDisabled()) {
          new Message("Messages.General.Disabled").translate(world, sender);
          return;
        }
        final TNECurrency currency = TNE.manager().currencyManager().get(world);

        if(!currency.getCurrencyType().offline() && MISCUtils.getPlayer(IDFinder.getID(arguments[0])) == null) {
          Message offlineType = new Message("Messages.Money.TypeOffline");
          offlineType.addVariable("$type", currency.getCurrencyType().name());
          offlineType.translate(world, sender);
          return;
        }

        final String parsed = CurrencyFormatter.parseAmount(currency, world, arguments[0]);

        BigDecimal value = new BigDecimal(parsed);
        if(TNE.manager().currencyManager().get(world).getTNEMinorTiers().size() <= 0) {
          value = value.setScale(0, BigDecimal.ROUND_FLOOR);
        }

        try {
          TNE.saveManager().getTNEManager().getTNEProvider().setAllBalance(world, value);
        } catch (SQLException ignore) {
          MISCUtils.help(sender, label, arguments);
          return;
        }

        Message message = new Message("Messages.Money.SetAll");
        message.addVariable("$amount", CurrencyFormatter.format(currency, world, value, arguments[0]));
        message.addVariable("$world", world);
        message.translate(world, IDFinder.getID(sender));
        return;
      }
      MISCUtils.help(sender, label, arguments);
    });
    return true;
  }
}