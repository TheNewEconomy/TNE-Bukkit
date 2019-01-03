package net.tnemc.core.commands.money;

import net.tnemc.core.TNE;
import net.tnemc.core.commands.TNECommand;
import net.tnemc.core.common.Message;
import net.tnemc.core.common.WorldVariant;
import net.tnemc.core.common.account.WorldFinder;
import net.tnemc.core.common.api.IDFinder;
import net.tnemc.core.common.currency.CurrencyFormatter;
import net.tnemc.core.common.currency.TNECurrency;
import net.tnemc.core.common.transaction.MultiTransactionHandler;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by Daniel on 7/10/2017.
 */
public class MoneyPayCommand extends TNECommand {

  public MoneyPayCommand(TNE plugin) {
    super(plugin);
  }

  @Override
  public String getName() {
    return "pay";
  }

  @Override
  public String[] getAliases() {
    return new String[] {
        "-+"
    };
  }

  @Override
  public String getNode() {
    return "tne.money.pay";
  }

  @Override
  public boolean console() {
    return false;
  }

  @Override
  public String getHelp() {
    return "Messages.Commands.Money.Pay";
  }

  @Override
  public List<String> onTab(CommandSender sender, Command command, String alias, String[] arguments, boolean shortened) {
    Map<Integer, String> argTypes = new HashMap<>();
    argTypes.put(0, "player");
    argTypes.put(1, "amount");
    argTypes.put(2, "currency");
    return buildSuggestions(sender, shortened, arguments, argTypes, 5);
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

      if(!arguments[0].contains(",") && !arguments[0].contains(TNE.instance().api().getString("Core.Server.ThirdParty.Faction")) &&
         !arguments[0].contains(TNE.instance().api().getString("Core.Server.ThirdParty.Town")) &&
         !arguments[0].contains(TNE.instance().api().getString("Core.Server.ThirdParty.Nation")) && IDFinder.getOffline(arguments[0], true) == null) {
        new Message(TNE.transactionManager().getResult("failed").initiatorMessage()).translate(world, sender);
        return;
      }

      if(arguments.length >= 2) {
        String currencyName = (arguments.length >= 3) ? arguments[2] : TNE.manager().currencyManager().get(world).name();
        TNECurrency currency = TNE.manager().currencyManager().get(world, currencyName);

        String parsed = CurrencyFormatter.parseAmount(currency, world, arguments[1]);
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

        if(value.compareTo(BigDecimal.ZERO) < 0) {
          Message msg = new Message("Messages.Money.Negative");
          msg.addVariable("$currency", currency.name());
          msg.addVariable("$world", world);
          msg.addVariable("$player", getPlayer(sender).getDisplayName());
          msg.translate(world, sender);
          return;
        }

        MultiTransactionHandler handler = new MultiTransactionHandler(TNE.manager().parsePlayerArgument(arguments[0], true),
            "pay", value, currency, world,
            TNE.manager().getAccount(IDFinder.getID(sender)));
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