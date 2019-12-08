package net.tnemc.core.commands.money;

import net.tnemc.core.TNE;
import net.tnemc.core.commands.TNECommand;
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

import java.math.BigDecimal;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * Created by creatorfromhell on 12/7/2019.
 * <p>
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */
public class MoneyPayFromCommand extends TNECommand {

  public MoneyPayFromCommand(TNE plugin) {
    super(plugin);
  }

  @Override
  public String name() {
    return "payfrom";
  }

  @Override
  public String[] aliases() {
    return new String[] {
        "payf", "payg", "ghostpay"
    };
  }

  @Override
  public String node() {
    return "tne.money.payfrom";
  }

  @Override
  public boolean console() {
    return false;
  }

  @Override
  public String helpLine() {
    return "Messages.Commands.Money.PayFrom";
  }

  @Override
  public boolean execute(CommandSender sender, String command, String[] arguments) {
    if(arguments.length <= 0) {
      help(sender);
      return false;
    }
    Bukkit.getScheduler().runTaskAsynchronously(plugin, ()->{
      TNE.debug("===START MoneyPayCommand ===");
      String world = WorldFinder.getWorld(sender, WorldVariant.BALANCE);

      if(TNE.instance().getWorldManager(world).isEconomyDisabled()) {
        new Message("Messages.General.Disabled").translate(world, sender);
        return;
      }

      if(!arguments[1].contains(",") && !arguments[1].contains(TNE.instance().api().getString("Core.Server.ThirdParty.Faction")) &&
          !arguments[1].contains(TNE.instance().api().getString("Core.Server.ThirdParty.Town")) &&
          !arguments[1].contains(TNE.instance().api().getString("Core.Server.ThirdParty.Nation")) && IDFinder.getOffline(arguments[1], true) == null) {
        new Message(TNE.transactionManager().getResult("failed").initiatorMessage()).translate(world, sender);
        return;
      }

      if(!TNE.instance().api().hasAccount(arguments[0])) {
        new Message(TNE.transactionManager().getResult("failed").initiatorMessage()).translate(world, sender);
        return;
      }

      if(arguments.length >= 3) {
        String currencyName = (arguments.length >= 4) ? arguments[3] : TNE.manager().currencyManager().get(world).name();

        if(MISCUtils.isSingularPlayer(arguments[1]) && arguments.length < 4) {
          currencyName = MISCUtils.findCurrencyName(world, Bukkit.getPlayer(IDFinder.getID(arguments[1])).getLocation());
        }

        if (!TNE.manager().currencyManager().contains(world, currencyName)) {
          Message m = new Message("Messages.Money.NoCurrency");
          m.addVariable("$currency", currencyName);
          m.addVariable("$world", world);
          m.translate(world, sender);
          return;
        }

        if(TNE.configurations().getBoolean("Core.Currency.Info.Advanced") && !sender.hasPermission("tne.money.payfrom." + currencyName)) {
          Message unable = new Message("Messages.Command.Unable");
          unable.addVariable("$commands", "/" + name());
          unable.translate(world, sender);
          return;
        }

        final TNECurrency currency = TNE.manager().currencyManager().get(world, currencyName);

        if(!currency.getCurrencyType().offline() && Bukkit.getPlayer(IDFinder.getID(arguments[1])) == null) {
          Message offlineType = new Message("Messages.Money.TypeOffline");
          offlineType.addVariable("$type", currency.getCurrencyType().name());
          offlineType.translate(world, sender);
          return;
        }

        String parsed = CurrencyFormatter.parseAmount(currency, world, arguments[2]);
        if(parsed.contains("Messages")) {
          Message msg = new Message(parsed);
          msg.addVariable("$currency", currency.name());
          msg.addVariable("$world", world);
          msg.addVariable("$player", getPlayer(sender).getDisplayName());
          msg.translate(world, sender);
          TNE.debug("===END MoneyPayCommand ===");
          return;
        }

        BigDecimal value = new BigDecimal(parsed);

        if(value.compareTo(BigDecimal.ZERO) <= 0) {
          Message msg = new Message("Messages.Money.Negative");
          msg.addVariable("$currency", currency.name());
          msg.addVariable("$world", world);
          msg.addVariable("$player", getPlayer(sender).getDisplayName());
          msg.translate(world, sender);
          return;
        }

        MultiTransactionHandler handler = new MultiTransactionHandler(TNE.manager().parsePlayerArgument(arguments[1], true),
            "pay", value, currency, world,
            TNE.manager().getAccount(IDFinder.getID(arguments[0])), TNE.manager().getAccount(IDFinder.getID(sender)));
        handler.handle(true);
        TNE.debug("===END MoneyPayCommand ===");
        return;
      }
      help(sender);
      TNE.debug("===END MoneyPayCommand ===");
    });
    return true;
  }
}