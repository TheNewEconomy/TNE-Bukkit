package net.tnemc.core.commands.admin;

import net.tnemc.commands.core.CommandExecution;
import net.tnemc.commands.core.provider.PlayerProvider;
import net.tnemc.core.TNE;
import net.tnemc.core.common.Message;
import net.tnemc.core.common.WorldVariant;
import net.tnemc.core.common.account.WorldFinder;
import net.tnemc.core.common.api.IDFinder;
import net.tnemc.core.common.currency.TNECurrency;
import net.tnemc.core.common.currency.formatter.CurrencyFormatter;
import net.tnemc.core.common.transaction.TNETransaction;
import net.tnemc.core.common.transaction.charge.TransactionCharge;
import net.tnemc.core.common.transaction.result.TransactionResult;
import net.tnemc.core.common.utils.MISCUtils;
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
public class AdminBalanceCommand implements CommandExecution {

  @Override
  public boolean execute(PlayerProvider provider, String label, String[] arguments) {
    Bukkit.getScheduler().runTaskAsynchronously(TNE.instance(), ()->{
      CommandSender sender = MISCUtils.getSender(provider);
      TNE.debug("===START AdminBalanceCommand  ===");
      if (arguments.length >= 1 && arguments.length <= 3) {

        UUID id = IDFinder.getID(arguments[0]);
        String world = (arguments.length >= 2) ? WorldFinder.getWorldName(arguments[1], WorldVariant.BALANCE) : WorldFinder.getWorld(sender, WorldVariant.BALANCE);
        String currencyName = (arguments.length >= 3) ? arguments[2] : TNE.manager().currencyManager().get(world).name();
        if (MISCUtils.isSingularPlayer(arguments[0]) && arguments.length < 3) {
          currencyName = MISCUtils.findCurrencyName(world, MISCUtils.getPlayer(IDFinder.getID(arguments[0])).getLocation());
        }

        TNECurrency currency = TNE.manager().currencyManager().get(world, currencyName);

        if (TNE.instance().getWorldManager(world).isEconomyDisabled()) {
          new Message("Messages.General.Disabled").translate(world, sender);
          return;
        }
        TNE.debug("Pre Transaction");

        TNETransaction transaction = new TNETransaction(TNE.manager().getAccount(IDFinder.getID(sender)), TNE.manager().getAccount(id), world, TNE.transactionManager().getType("inquiry"));
        transaction.setRecipientCharge(new TransactionCharge(world, currency, BigDecimal.ZERO));
        TNE.debug("pre perform");
        TransactionResult result = transaction.perform();
        TNE.debug("post perform");
        Message message = new Message(result.initiatorMessage());
        message.addVariable("$player", arguments[0]);
        message.addVariable("$world", world);
        if (TNE.instance().api().getBoolean("Core.Currency.Info.FormatMoney")) {
          message.addVariable("$amount", CurrencyFormatter.format(TNE.manager().currencyManager().get(world, transaction.recipientBalance().getCurrency().name()), world, transaction.recipientBalance().getAmount(), transaction.recipient()));
        } else {
          message.addVariable("$amount", transaction.recipientBalance().getAmount().toPlainString());
        }
        message.translate(world, IDFinder.getID(sender));
        TNE.debug("===END AdminBalanceCommand  ===");
        return;
      }
      MISCUtils.help(sender, label, arguments);
      TNE.debug("===END AdminBalanceCommand  ===");
    });
    return true;
  }
}