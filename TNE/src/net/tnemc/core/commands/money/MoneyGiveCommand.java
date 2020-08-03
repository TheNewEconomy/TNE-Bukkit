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
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by Daniel on 7/10/2017.
 */
public class MoneyGiveCommand extends TNECommand {

  public MoneyGiveCommand(TNE plugin) {
    super(plugin);
  }

  @Override
  public String name() {
    return "give";
  }

  @Override
  public String[] aliases() {
    return new String[] {
        "+"
    };
  }

  @Override
  public String node() {
    return "tne.money.give";
  }

  @Override
  public boolean console() {
    return true;
  }

  @Override
  public String helpLine() {
    return "Messages.Commands.Money.Give";
  }

  /*@Override
  public List<String> onTab(CommandSender sender, Command command, String alias, String[] arguments, boolean shortened) {
    Map<Integer, String> argTypes = new HashMap<>();
    argTypes.put(0, "player");
    argTypes.put(1, "amount");
    argTypes.put(2, "world");
    argTypes.put(3, "currency");
    return buildSuggestions(sender, shortened, arguments, argTypes, 2);
  }*/

  @Override
  public boolean execute(CommandSender sender, String command, String[] arguments) {
    Bukkit.getScheduler().runTaskAsynchronously(plugin, ()->{
      TNE.debug("===START MoneyGiveCommand ===");
      if(arguments.length >= 2) {
        String world = (arguments.length >= 3) ? arguments[2] : WorldFinder.getWorld(sender, WorldVariant.BALANCE);
        String currencyName = (arguments.length >= 4) ? arguments[3] : TNE.manager().currencyManager().get(world).name();

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

        if(MISCUtils.isSingularPlayer(arguments[0]) && arguments.length < 4) {
          currencyName = MISCUtils.findCurrencyName(world, Bukkit.getPlayer(IDFinder.getID(arguments[0])).getLocation());
        }

        if (!TNE.manager().currencyManager().contains(world, currencyName)) {
          Message m = new Message("Messages.Money.NoCurrency");
          m.addVariable("$currency", currencyName);
          m.addVariable("$world", world);
          m.translate(world, sender);
          return;
        }

        if(TNE.configurations().getBoolean("Core.Currency.Info.Advanced") && !sender.hasPermission("tne.money.give." + currencyName)) {
          Message unable = new Message("Messages.Command.Unable");
          unable.addVariable("$commands", "/" + name());
          unable.translate(world, sender);
          return;
        }

        final TNECurrency currency = TNE.manager().currencyManager().get(world, currencyName);

        if(!currency.getCurrencyType().offline() && Bukkit.getPlayer(IDFinder.getID(arguments[0])) == null) {
          Message offlineType = new Message("Messages.Money.TypeOffline");
          offlineType.addVariable("$type", currency.getCurrencyType().name());
          offlineType.translate(world, sender);
          return;
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

        String parsed = CurrencyFormatter.parseAmount(currency, world, arguments[1]);
        if(parsed.contains("Messages")) {
          Message max = new Message(parsed);
          max.addVariable("$currency", currency.name());
          max.addVariable("$world", world);
          max.addVariable("$player", getPlayer(sender).getDisplayName());
          max.translate(world, sender);
          TNE.debug("===END MoneyGiveCommand ===");
          return;
        }

        final BigDecimal value = new BigDecimal(parsed);

        final MultiTransactionHandler handler = new MultiTransactionHandler(TNE.manager().parsePlayerArgument(arguments[0]),
            "give", value, currency, world,
            TNE.manager().getAccount(IDFinder.getID(sender)));
        handler.handle(true);
        TNE.debug("===END MoneyGiveCommand ===");
        return;
      }
      help(sender);
      TNE.debug("===END MoneyGiveCommand ===");
    });
    return true;
  }
}