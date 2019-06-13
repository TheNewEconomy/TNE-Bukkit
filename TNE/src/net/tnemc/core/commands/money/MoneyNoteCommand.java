package net.tnemc.core.commands.money;

import net.tnemc.core.TNE;
import net.tnemc.core.commands.TNECommand;
import net.tnemc.core.common.Message;
import net.tnemc.core.common.WorldVariant;
import net.tnemc.core.common.account.TNEAccount;
import net.tnemc.core.common.account.WorldFinder;
import net.tnemc.core.common.api.IDFinder;
import net.tnemc.core.common.currency.TNECurrency;
import net.tnemc.core.common.currency.formatter.CurrencyFormatter;
import net.tnemc.core.common.transaction.TNETransaction;
import net.tnemc.core.common.utils.MISCUtils;
import net.tnemc.core.economy.transaction.charge.TransactionCharge;
import net.tnemc.core.economy.transaction.charge.TransactionChargeType;
import net.tnemc.core.economy.transaction.result.TransactionResult;
import org.bukkit.Bukkit;
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

  /*@Override
  public List<String> onTab(CommandSender sender, Command command, String alias, String[] arguments, boolean shortened) {
    Map<Integer, String> argTypes = new HashMap<>();
    argTypes.put(0, "amount");
    argTypes.put(1, "currency");
    return buildSuggestions(sender, shortened, arguments, argTypes, 5);
  }*/

  @Override
  public boolean execute(CommandSender sender, String command, String[] arguments) {
    Bukkit.getScheduler().runTaskAsynchronously(plugin, ()->{
      if(arguments.length >= 1) {
        final String world = WorldFinder.getWorld(sender, WorldVariant.BALANCE);
        String currencyName = (arguments.length >= 2) ? arguments[1] : TNE.manager().currencyManager().get(world).name();

        if(MISCUtils.isSingularPlayer(arguments[0]) && arguments.length < 2) {
          currencyName = MISCUtils.findCurrencyName(world, Bukkit.getPlayer(IDFinder.getID(arguments[0])).getLocation());
        }

        final TNECurrency currency = TNE.manager().currencyManager().get(world, currencyName);
        final UUID id = IDFinder.getID(sender);
        final TNEAccount account = TNE.manager().getAccount(id);

        if(TNE.instance().getWorldManager(world).isEconomyDisabled()) {
          new Message("Messages.General.Disabled").translate(world, sender);
          return;
        }

        if(!currency.isNotable()) {
          new Message("Messages.Money.NoteFailed").translate(world, sender);
          return;
        }

        final String parsed = CurrencyFormatter.parseAmount(currency, world, arguments[0]);
        if(parsed.contains("Messages")) {
          Message max = new Message(parsed);
          max.addVariable("$currency", currency.name());
          max.addVariable("$world", world);
          max.addVariable("$player", getPlayer(sender).getDisplayName());
          max.translate(world, sender);
          return;
        }

        final BigDecimal value = new BigDecimal(parsed);
        if(value.compareTo(currency.getMinimum()) < 0) {
          Message minimum = new Message("Messages.Money.NoteMinimum");
          minimum.addVariable("$amount", CurrencyFormatter.format(currency, world, currency.getMinimum(), getPlayer(sender).getUniqueId().toString()));
          minimum.translate(world, sender);
          return;
        }

        TNETransaction transaction = new TNETransaction(account, account, world, TNE.transactionManager().getType("note"));
        transaction.setRecipientCharge(new TransactionCharge(world, currency, value.add(currency.getFee()), TransactionChargeType.LOSE));
        final TransactionResult result = TNE.transactionManager().perform(transaction);


        if(result.proceed()) {
          ItemStack stack = TNE.manager().currencyManager().createNote(currency.name(), world, value);
          Bukkit.getScheduler().runTask(plugin,()->getPlayer(sender).getInventory().addItem(stack));
          Message message = new Message(result.recipientMessage());
          message.addVariable("$player", arguments[0]);
          message.addVariable("$world", world);
          message.addVariable("$currency", currencyName);
          message.addVariable("$amount", CurrencyFormatter.format(TNECurrency.fromReserve(transaction.recipientCharge().getEntry().getCurrency()), world, value, transaction.recipient()));
          message.translate(world, sender);
          return;
        }
        Message message = new Message(result.recipientMessage());
        message.translate(world, sender);
        return;
      }
      help(sender);
      return;
    });
    return true;
  }
}