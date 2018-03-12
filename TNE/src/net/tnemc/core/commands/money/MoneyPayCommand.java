package net.tnemc.core.commands.money;

import com.github.tnerevival.commands.TNECommand;
import com.github.tnerevival.core.Message;
import com.github.tnerevival.user.IDFinder;
import net.tnemc.core.TNE;
import net.tnemc.core.common.WorldVariant;
import net.tnemc.core.common.account.WorldFinder;
import net.tnemc.core.common.currency.CurrencyFormatter;
import net.tnemc.core.common.currency.TNECurrency;
import net.tnemc.core.common.transaction.MultiTransactionHandler;
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
    return new String[0];
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
  public boolean execute(CommandSender sender, String command, String[] arguments) {
    TNE.debug("===START MoneyPayCommand ===");
    String world = WorldFinder.getWorld(sender, WorldVariant.BALANCE);
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
        return false;
      }

      BigDecimal value = new BigDecimal(parsed);

      MultiTransactionHandler handler = new MultiTransactionHandler(TNE.manager().parsePlayerArgument(arguments[0]),
                                                                    "pay", value, currency, world,
                                                                    TNE.manager().getAccount(IDFinder.getID(sender)));
      handler.handle(true);
      TNE.debug("===END MoneyPayCommand ===");
      return handler.getData().isProceed();
    }
    help(sender);
    TNE.debug("===END MoneyPayCommand ===");
    return false;
  }
}