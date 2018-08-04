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
import net.tnemc.core.economy.transaction.charge.TransactionCharge;
import net.tnemc.core.economy.transaction.charge.TransactionChargeType;
import net.tnemc.core.economy.transaction.result.TransactionResult;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemStack;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by Daniel on 7/31/2017.
 */
public class MoneyNoteCommand extends TNECommand {

  public MoneyNoteCommand(TNE plugin) {
    super(plugin);
  }

  @Override
  public String getName() {
    return "note";
  }

  @Override
  public String[] getAliases() {
    return new String[0];
  }

  @Override
  public String getNode() {
    return "tne.money.note";
  }

  @Override
  public boolean console() {
    return false;
  }

  @Override
  public String getHelp() {
    return "Messages.Commands.Money.Note";
  }

  @Override
  public boolean execute(CommandSender sender, String command, String[] arguments) {
    if(arguments.length >= 1) {
      String world = WorldFinder.getWorld(sender, WorldVariant.BALANCE);
      String currencyName = (arguments.length >= 2) ? arguments[1] : TNE.manager().currencyManager().get(world).name();
      TNECurrency currency = TNE.manager().currencyManager().get(world, currencyName);
      UUID id = IDFinder.getID(sender);
      TNEAccount account = TNE.manager().getAccount(id);

      if(TNE.instance().getWorldManager(world).isEconomyDisabled()) {
        new Message("Messages.General.Disabled").translate(world, sender);
        return false;
      }

      String parsed = CurrencyFormatter.parseAmount(currency, world, arguments[0]);
      if(parsed.contains("Messages")) {
        Message max = new Message(parsed);
        max.addVariable("$currency", currency.name());
        max.addVariable("$world", world);
        max.addVariable("$player", getPlayer(sender).getDisplayName());
        max.translate(world, sender);
        return false;
      }

      BigDecimal value = new BigDecimal(parsed);
      if(value.compareTo(currency.getMinimum()) < 0) {
        Message minimum = new Message(parsed);
        minimum.addVariable("$amount", CurrencyFormatter.format(currency, world, currency.getMinimum()));
        minimum.translate(world, sender);
        return false;
      }

      TNETransaction transaction = new TNETransaction(account, account, world, TNE.transactionManager().getType("note"));
      transaction.setRecipientCharge(new TransactionCharge(world, currency, value.add(currency.getFee()), TransactionChargeType.LOSE));
      TransactionResult result = TNE.transactionManager().perform(transaction);


      if(result.proceed()) {
        ItemStack stack = TNE.manager().currencyManager().createNote(id, currency.name(), world, value);
        getPlayer(sender).getInventory().addItem(stack);
        Message message = new Message(result.recipientMessage());
        message.addVariable("$player", arguments[0]);
        message.addVariable("$world", world);
        message.addVariable("$currency", currencyName);
        message.addVariable("$amount", CurrencyFormatter.format(transaction.recipientCharge().getEntry().getCurrency(), world, value));
        message.translate(world, sender);
        return true;
      }
      Message message = new Message(result.recipientMessage());
      message.translate(world, sender);
      return false;
    }
    help(sender);
    return false;
  }
}