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
import net.tnemc.core.common.transaction.MultiTransactionHandler;
import net.tnemc.core.common.utils.MISCUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.math.BigDecimal;

/**
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by Daniel on 7/10/2017.
 */
public class MoneyTakeCommand implements CommandExecution {

  @Override
  public boolean execute(PlayerProvider provider, String label, String[] arguments) {
    CommandSender sender = MISCUtils.getSender(provider);
    Bukkit.getScheduler().runTaskAsynchronously(TNE.instance(), ()->{
      if(arguments.length >= 2) {
        String world = (arguments.length >= 3) ? arguments[2] : WorldFinder.getWorld(sender, WorldVariant.BALANCE);
        String currencyName = "";

        if(arguments.length == 3 && !TNE.instance().hasWorldManager(world)) {
          world = WorldFinder.getWorld(sender, WorldVariant.BALANCE);
          currencyName = arguments[2];
        }

        if (!TNE.instance().hasWorldManager(world)) {
          Message m = new Message("Messages.Money.NoWorld");
          m.addVariable("$world", world);
          m.translate(world, sender);
          return;
        }

        if(currencyName.equalsIgnoreCase("")) {
          currencyName = (arguments.length >= 4) ? arguments[3] : TNE.manager().currencyManager().get(world).name();

          final Player player = Bukkit.getPlayer(IDFinder.getID(arguments[0]));
          if(MISCUtils.isSingularPlayer(arguments[0]) && arguments.length < 4 && player != null) {
            currencyName = MISCUtils.findCurrencyName(world, player.getLocation());
          }
        }

        if(TNE.instance().getWorldManager(world).isEconomyDisabled()) {
          new Message("Messages.General.Disabled").translate(world, sender);
          return;
        }

        if(!arguments[0].contains(",") && !arguments[0].contains(TNE.instance().api().getString("Core.Server.ThirdParty.Faction")) &&
            !arguments[0].contains(TNE.instance().api().getString("Core.Server.ThirdParty.Town")) &&
            !arguments[0].contains(TNE.instance().api().getString("Core.Server.ThirdParty.Nation")) && IDFinder.getOffline(arguments[0], true) == null) {
          new Message(TNE.transactionManager().getResult("failed").initiatorMessage()).translate(world, sender);
          return;
        }

        if (!TNE.manager().currencyManager().contains(world, currencyName)) {
          Message m = new Message("Messages.Money.NoCurrency");
          m.addVariable("$currency", currencyName);
          m.addVariable("$world", world);
          m.translate(world, sender);
          return;
        }

        if(TNE.configurations().getBoolean("Core.Currency.Info.Advanced") && !sender.hasPermission("tne.money.take." + currencyName)) {
          Message unable = new Message("Messages.Command.Unable");
          unable.addVariable("$commands", "/" + label);
          unable.translate(world, sender);
          return;
        }

        final TNECurrency currency = TNE.manager().currencyManager().get(world, currencyName);

        if(!currency.getCurrencyType().offline() && MISCUtils.getPlayer(IDFinder.getID(arguments[0])) == null) {
          Message offlineType = new Message("Messages.Money.TypeOffline");
          offlineType.addVariable("$type", currency.getCurrencyType().name());
          offlineType.translate(world, sender);
          return;
        }

        final String parsed = CurrencyFormatter.parseAmount(currency, world, arguments[1]);
        if (parsed.contains("Messages")) {
          Message max = new Message(parsed);
          max.addVariable("$currency", currency.name());
          max.addVariable("$world", world);
          max.addVariable("$player", MISCUtils.getPlayer(sender).getDisplayName());
          max.translate(world, sender);
          return;
        }

        MultiTransactionHandler handler;
        if(!parsed.contains("\\%")) {
          final BigDecimal value = new BigDecimal(parsed);

          handler = new MultiTransactionHandler(TNE.manager().parsePlayerArgument(provider, arguments[0]),
              "take", value, currency, world,
              TNE.manager().getAccount(IDFinder.getID(sender)));
        } else {
          handler = new MultiTransactionHandler(TNE.manager().parsePlayerArgument(provider, arguments[0]),
              "take", parsed, currency, world,
              TNE.manager().getAccount(IDFinder.getID(sender)));
        }
        handler.handle(true);
        return;
      }
      MISCUtils.help(sender, label, arguments);
    });
    return true;
  }
}