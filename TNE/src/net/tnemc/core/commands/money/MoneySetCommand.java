package net.tnemc.core.commands.money;

import com.github.tnerevival.commands.TNECommand;
import com.github.tnerevival.core.Message;
import com.github.tnerevival.user.IDFinder;
import net.tnemc.core.TNE;
import net.tnemc.core.common.WorldVariant;
import net.tnemc.core.common.account.WorldFinder;
import net.tnemc.core.common.currency.CurrencyFormatter;
import net.tnemc.core.common.currency.TNECurrency;
import net.tnemc.core.common.transaction.TNETransaction;
import net.tnemc.core.economy.transaction.charge.TransactionCharge;
import net.tnemc.core.economy.transaction.charge.TransactionChargeType;
import net.tnemc.core.economy.transaction.result.TransactionResult;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

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
public class MoneySetCommand extends TNECommand {

  public MoneySetCommand(TNE plugin) {
    super(plugin);
  }

  @Override
  public String getName() {
    return "set";
  }

  @Override
  public String[] getAliases() {
    return new String[0];
  }

  @Override
  public String getNode() {
    return "tne.money.set";
  }

  @Override
  public boolean console() {
    return true;
  }

  @Override
  public String getHelp() {
    return "Messages.Commands.Money.Set";
  }

  @Override
  public boolean execute(CommandSender sender, String command, String[] arguments) {
    if(arguments.length >= 2) {
      String world = (arguments.length >= 3) ? arguments[2] : WorldFinder.getWorld(sender, WorldVariant.BALANCE);
      String currencyName = (arguments.length >= 4)? arguments[3] : TNE.manager().currencyManager().get(world).name();
      UUID id = IDFinder.getID(arguments[0]);

      TNE.debug("MoneySetCommand Currency: " + currencyName);
      TNECurrency currency = TNE.manager().currencyManager().get(world, currencyName);
      String parsed = CurrencyFormatter.parseAmount(currency, world, arguments[1]);
      if (parsed.contains("Messages")) {
        Message max = new Message(parsed);
        max.addVariable("$currency", currency.name());
        max.addVariable("$world", world);
        max.addVariable("$player", getPlayer(sender).getDisplayName());
        max.translate(world, sender);
        return false;
      }

      BigDecimal value = new BigDecimal(parsed);
      BigDecimal balance = TNE.instance().api().getHoldings(id.toString(), world, currency);
      TransactionChargeType type = (balance.compareTo(value) >= 0)? TransactionChargeType.LOSE
                                                                  : TransactionChargeType.GAIN;

      BigDecimal newBalance = (type.equals(TransactionChargeType.GAIN))? value.subtract(balance) : balance.subtract(value);

      TNETransaction transaction = new TNETransaction(TNE.manager().getAccount(IDFinder.getID(sender)), TNE.manager().getAccount(id), world, TNE.transactionManager().getType("set"));
      transaction.setRecipientCharge(new TransactionCharge(world, currency, newBalance, type));
      TransactionResult result = TNE.transactionManager().perform(transaction);


      if(result.proceed() && transaction.recipient() != null && IDFinder.getPlayer(id.toString()) != null && Bukkit.getOnlinePlayers().contains(IDFinder.getPlayer(id.toString()))) {
        Message message = new Message(result.recipientMessage());
        message.addVariable("$player", IDFinder.ecoToUsername(IDFinder.getID(sender)));
        message.addVariable("$world", world);
        message.addVariable("$currency", currencyName);
        message.addVariable("$amount", CurrencyFormatter.format(world, currencyName, value));
        message.translate(world, id);
      }

      Message message = new Message(result.initiatorMessage());
      message.addVariable("$player", arguments[0]);
      message.addVariable("$world", world);
      message.addVariable("$currency", currencyName);
      message.addVariable("$amount", CurrencyFormatter.format(world, currencyName, value));
      message.translate(world, IDFinder.getID(sender));
      return result.proceed();
    }
    help(sender);
    return false;
  }
}