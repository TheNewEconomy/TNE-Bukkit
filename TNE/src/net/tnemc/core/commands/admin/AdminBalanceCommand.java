package net.tnemc.core.commands.admin;

import net.tnemc.core.TNE;
import net.tnemc.core.commands.TNECommand;
import net.tnemc.core.common.Message;
import net.tnemc.core.common.WorldVariant;
import net.tnemc.core.common.account.WorldFinder;
import net.tnemc.core.common.api.IDFinder;
import net.tnemc.core.common.currency.CurrencyFormatter;
import net.tnemc.core.common.currency.TNECurrency;
import net.tnemc.core.common.transaction.TNETransaction;
import net.tnemc.core.economy.transaction.charge.TransactionCharge;
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
public class AdminBalanceCommand extends TNECommand {

  public AdminBalanceCommand(TNE plugin) {
    super(plugin);
  }

  @Override
  public String getName() {
    return "balance";
  }

  @Override
  public String[] getAliases() {
    return new String[] {
        "bal"
    };
  }

  @Override
  public String getNode() {
    return "tne.admin.balance";
  }

  @Override
  public boolean console() {
    return true;
  }

  @Override
  public String getHelp() {
    return "Messages.Commands.Admin.Balance";
  }

  @Override
  public boolean execute(CommandSender sender, String command, String[] arguments) {
    TNE.debug("===START AdminBalanceCommand  ===");
    if(arguments.length >= 1 && arguments.length <= 3) {

      UUID id = IDFinder.getID(arguments[0]);
      String world = (arguments.length >= 2) ? WorldFinder.getWorld(arguments[1], WorldVariant.BALANCE) : WorldFinder.getWorld(sender, WorldVariant.BALANCE);
      String currencyName = (arguments.length == 3) ? arguments[2] : TNE.manager().currencyManager().get(world).name();
      TNECurrency currency = TNE.manager().currencyManager().get(world, currencyName);

      TNETransaction transaction = new TNETransaction(TNE.manager().getAccount(IDFinder.getID(sender)), TNE.manager().getAccount(id), world, TNE.transactionManager().getType("inquiry"));
      transaction.setRecipientCharge(new TransactionCharge(world, currency, BigDecimal.ZERO));
      TransactionResult result = transaction.perform();
      Message message = new Message(result.initiatorMessage());
      message.addVariable("$player", arguments[0]);
      message.addVariable("$world", world);
      message.addVariable("$amount", CurrencyFormatter.format(transaction.recipientBalance().getCurrency(), world, transaction.recipientBalance().getAmount()));
      message.translate(world, IDFinder.getID(sender));
      TNE.debug("===END AdminBalanceCommand  ===");
      return result.proceed();
    }
    help(sender);
    TNE.debug("===END AdminBalanceCommand  ===");
    return false;
  }
}