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
import org.bukkit.entity.Player;

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
public class MoneyConvertCommand extends TNECommand {

  public MoneyConvertCommand(TNE plugin) {
    super(plugin);
  }

  @Override
  public String getName() {
    return "convert";
  }

  @Override
  public String[] getAliases() {
    return new String[0];
  }

  @Override
  public String getNode() {
    return "tne.money.convert";
  }

  @Override
  public boolean console() {
    return false;
  }

  @Override
  public String getHelp() {
    return "Messages.Commands.Money.Convert";
  }

  @Override
  public boolean execute(CommandSender sender, String command, String[] arguments) {
    Bukkit.getScheduler().runTaskAsynchronously(plugin, ()->{
      final Player player = getPlayer(sender);
      if(arguments.length >= 2) {
        final UUID id = IDFinder.getID(sender);
        final TNEAccount account = TNE.manager().getAccount(id);
        final String worldTo = (arguments[1].contains(":"))? arguments[1].split(":")[1] : WorldFinder.getWorld(sender, WorldVariant.BALANCE);
        final String currencyTo = (arguments[1].contains(":"))? arguments[1].split(":")[0] : arguments[1];
        final TNECurrency to = TNE.manager().currencyManager().get(worldTo, currencyTo);
        String worldFrom = WorldFinder.getWorld(sender, WorldVariant.BALANCE);
        TNECurrency from = MISCUtils.findCurrency(worldFrom, player.getLocation());

        if(TNE.instance().getWorldManager(worldFrom).isEconomyDisabled()) {
          new Message("Messages.General.Disabled").translate(worldFrom, sender);
          return;
        }

        if(TNE.instance().getWorldManager(worldTo).isEconomyDisabled()) {
          new Message("Messages.General.Disabled").translate(worldTo, sender);
          return;
        }

        if(arguments.length >= 3) {
          worldFrom = (arguments[2].contains(":"))? arguments[2].split(":")[1] : WorldFinder.getWorld(sender, WorldVariant.BALANCE);
          String currencyFrom = (arguments[2].contains(":"))? arguments[2].split(":")[0] : arguments[2];
          from = TNE.manager().currencyManager().get(worldFrom, currencyFrom);
        }

        String parsed = CurrencyFormatter.parseAmount(to, worldTo, arguments[0]);
        if(parsed.contains("Messages")) {
          Message max = new Message(parsed);
          max.addVariable("$currency", to.name());
          max.addVariable("$world", worldTo);
          max.addVariable("$player", player.getDisplayName());
          max.translate(worldTo, player);
          return;
        }

        BigDecimal value = new BigDecimal(parsed);
        TNETransaction transaction = new TNETransaction(account, account, worldFrom, TNE.transactionManager().getType("conversion"));
        transaction.setInitiatorCharge(new TransactionCharge(worldFrom, from, value, TransactionChargeType.LOSE));
        transaction.setRecipientCharge(new TransactionCharge(worldTo, to, value, TransactionChargeType.GAIN));
        TransactionResult result = TNE.transactionManager().perform(transaction);

        Message message = new Message(result.initiatorMessage());
        message.addVariable("$player", arguments[0]);
        message.addVariable("$world", worldTo);
        message.addVariable("$currency", to.name());
        message.addVariable("$worldFrom", worldFrom);
        message.addVariable("$from_amount", CurrencyFormatter.format(from, worldFrom, value, arguments[0]));
        message.addVariable("$amount", CurrencyFormatter.format(to, worldTo, transaction.recipientCharge().getEntry().getAmount(), arguments[0]));
        message.translate(worldTo, IDFinder.getID(sender));
        return;
      }
      help(sender);
    });
    return true;
  }
}