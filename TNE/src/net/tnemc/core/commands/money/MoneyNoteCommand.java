package net.tnemc.core.commands.money;

import net.tnemc.commands.core.CommandExecution;
import net.tnemc.commands.core.provider.PlayerProvider;
import net.tnemc.core.TNE;
import net.tnemc.core.common.Message;
import net.tnemc.core.common.WorldVariant;
import net.tnemc.core.common.account.TNEAccount;
import net.tnemc.core.common.account.WorldFinder;
import net.tnemc.core.common.api.IDFinder;
import net.tnemc.core.common.currency.TNECurrency;
import net.tnemc.core.common.currency.formatter.CurrencyFormatter;
import net.tnemc.core.common.transaction.TNETransaction;
import net.tnemc.core.common.transaction.charge.TransactionCharge;
import net.tnemc.core.common.transaction.charge.TransactionChargeType;
import net.tnemc.core.common.transaction.result.TransactionResult;
import net.tnemc.core.common.utils.MISCUtils;
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
public class MoneyNoteCommand implements CommandExecution {

  @Override
  public boolean execute(PlayerProvider provider, String label, String[] arguments) {
    Bukkit.getScheduler().runTaskAsynchronously(TNE.instance(), ()->{
      CommandSender sender = MISCUtils.getSender(provider);
      if(arguments.length >= 1) {
        final String world = WorldFinder.getWorld(sender, WorldVariant.BALANCE);
        String currencyName = (arguments.length >= 2) ? arguments[1] : TNE.manager().currencyManager().get(world).name();

        if(arguments.length < 2) {
          currencyName = MISCUtils.findCurrencyName(world, MISCUtils.getPlayer(sender).getLocation());
        }

        if (!TNE.manager().currencyManager().contains(world, currencyName)) {
          Message m = new Message("Messages.Money.NoCurrency");
          m.addVariable("$currency", currencyName);
          m.addVariable("$world", world);
          m.translate(world, sender);
          return;
        }

        if(TNE.configurations().getBoolean("Core.Currency.Info.Advanced") && !sender.hasPermission("tne.money.note." + currencyName)) {
          Message unable = new Message("Messages.Command.Unable");
          unable.addVariable("$commands", "/" + label);
          unable.translate(world, sender);
          return;
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
          max.addVariable("$player", MISCUtils.getPlayer(sender).getDisplayName());
          max.translate(world, sender);
          return;
        }

        final BigDecimal value = new BigDecimal(parsed);
        if(value.compareTo(currency.getMinimum()) < 0) {
          Message minimum = new Message("Messages.Money.NoteMinimum");
          minimum.addVariable("$amount", CurrencyFormatter.format(currency, world, currency.getMinimum(), MISCUtils.getPlayer(sender).getUniqueId().toString()));
          minimum.translate(world, sender);
          return;
        }

        TNETransaction transaction = new TNETransaction(null, account, world, TNE.transactionManager().getType("note"));
        transaction.setRecipientCharge(new TransactionCharge(world, currency, value.add(currency.getFee()), TransactionChargeType.LOSE));
        final TransactionResult result = TNE.transactionManager().perform(transaction);


        if(result.proceed()) {
          ItemStack stack = TNE.manager().currencyManager().createNote(currency.name(), world, value);
          Bukkit.getScheduler().runTask(TNE.instance(),()->{
            if(MISCUtils.getPlayer(sender).getInventory().firstEmpty() == -1) {
              MISCUtils.getPlayer(sender).getWorld().dropItemNaturally(MISCUtils.getPlayer(sender).getLocation(), stack);
            } else {
              MISCUtils.getPlayer(sender).getInventory().addItem(stack);
            }
          });
          Message message = new Message(result.recipientMessage());
          message.addVariable("$player", arguments[0]);
          message.addVariable("$world", world);
          message.addVariable("$currency", currencyName);
          message.addVariable("$amount", CurrencyFormatter.format(TNE.manager().currencyManager().get(world, transaction.recipientCharge().getCurrency().name()), world, value, transaction.recipient()));
          message.translate(world, sender);
          return;
        }
        Message message = new Message(result.recipientMessage());
        message.translate(world, sender);
        return;
      }
      MISCUtils.help(sender, label, arguments);
      return;
    });
    return true;
  }
}