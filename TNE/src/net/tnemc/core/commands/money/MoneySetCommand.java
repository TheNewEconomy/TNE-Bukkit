package net.tnemc.core.commands.money;

import net.tnemc.core.TNE;
import net.tnemc.core.commands.TNECommand;
import net.tnemc.core.common.Message;
import net.tnemc.core.common.WorldVariant;
import net.tnemc.core.common.account.TNEAccount;
import net.tnemc.core.common.account.WorldFinder;
import net.tnemc.core.common.api.IDFinder;
import net.tnemc.core.common.currency.CurrencyFormatter;
import net.tnemc.core.common.currency.TNECurrency;
import net.tnemc.core.common.transaction.TNETransaction;
import net.tnemc.core.common.utils.MISCUtils;
import net.tnemc.core.economy.currency.CurrencyEntry;
import net.tnemc.core.economy.transaction.charge.TransactionCharge;
import net.tnemc.core.economy.transaction.charge.TransactionChargeType;
import net.tnemc.core.economy.transaction.result.TransactionResult;
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
    return new String[] {
        "="
    };
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
      final String currencyName = (arguments.length >= 4)? arguments[3] : TNE.manager().currencyManager().get(world).name();
      final UUID id = IDFinder.getID(arguments[0]);
      final TNEAccount account = TNE.manager().getAccount(id);

      if(TNE.instance().getWorldManager(world).isEconomyDisabled()) {
        new Message("Messages.General.Disabled").translate(world, sender);
        return false;
      }

      TNE.debug("MoneySetCommand Currency: " + currencyName);
      final TNECurrency currency = TNE.manager().currencyManager().get(world, currencyName);
      final String parsed = CurrencyFormatter.parseAmount(currency, world, arguments[1]);
      if (parsed.contains("Messages")) {
        Message max = new Message(parsed);
        max.addVariable("$currency", currency.name());
        max.addVariable("$world", world);
        max.addVariable("$player", getPlayer(sender).getDisplayName());
        max.translate(world, sender);
        return false;
      }

      final BigDecimal value = new BigDecimal(parsed);
      TNE.debug("Value: " + value.toPlainString());
      final BigDecimal balance = account.getHoldings(world, currency);
      TNE.debug("Balance: " + balance.toPlainString());
      final TransactionChargeType type = (balance.compareTo(value) >= 0)? TransactionChargeType.LOSE
                                                                  : TransactionChargeType.GAIN;
      TNE.debug("ChargeType: " + type.name());

      final BigDecimal newBalance = (type.equals(TransactionChargeType.GAIN))? value.subtract(balance) : balance.subtract(value);

      TNE.debug("New Balance: " + newBalance.toPlainString());

      TNETransaction transaction = new TNETransaction(TNE.manager().getAccount(IDFinder.getID(sender)), account, world, TNE.transactionManager().getType("set"));
      transaction.setRecipientCharge(new TransactionCharge(world, currency, newBalance, type));
      transaction.setRecipientBalance(new CurrencyEntry(world, currency, balance));
      final TransactionResult result = TNE.transactionManager().perform(transaction);


      if(result.proceed() && transaction.recipient() != null && MISCUtils.isOnline(id, world)) {
        Message message = new Message(result.recipientMessage());
        message.addVariable("$player", account.displayName());
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