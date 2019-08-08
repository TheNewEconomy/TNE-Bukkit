package net.tnemc.core.commands.money;

import net.tnemc.core.TNE;
import net.tnemc.core.commands.TNECommand;
import net.tnemc.core.common.Message;
import net.tnemc.core.common.WorldVariant;
import net.tnemc.core.common.account.WorldFinder;
import net.tnemc.core.common.api.IDFinder;
import net.tnemc.core.common.currency.TNECurrency;
import net.tnemc.core.common.currency.formatter.CurrencyFormatter;
import net.tnemc.core.common.transaction.TNETransaction;
import net.tnemc.core.common.utils.MISCUtils;
import net.tnemc.core.economy.transaction.charge.TransactionCharge;
import net.tnemc.core.economy.transaction.result.TransactionResult;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * Created by creatorfromhell on 5/20/2019.
 * <p>
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */
public class MoneyOtherCommand extends TNECommand {

  public MoneyOtherCommand(TNE plugin) {
    super(plugin);
  }

  @Override
  public String getName() {
    return "other";
  }

  @Override
  public String[] getAliases() {
    return new String[] {
        "balo", "balother", "balanceother"
    };
  }

  @Override
  public String getNode() {
    return "tne.money.other";
  }

  @Override
  public boolean console() {
    return true;
  }

  @Override
  public String getHelp() {
    return "Messages.Commands.Admin.Balance";
  }

  /*@Override
  public List<String> onTab(CommandSender sender, Command command, String alias, String[] arguments, boolean shortened) {
    Map<Integer, String> argTypes = new HashMap<>();
    argTypes.put(0, "player");
    argTypes.put(1, "world");
    argTypes.put(2, "currency");
    return buildSuggestions(sender, shortened, arguments, argTypes, 1);
  }*/

  @Override
  public boolean execute(CommandSender sender, String command, String[] arguments) {
    TNE.debug("===START MoneyOtherCommand  ===");
    if(arguments.length >= 1 && arguments.length <= 3) {

      UUID id = IDFinder.getID(arguments[0]);
      String world = (arguments.length >= 2) ? WorldFinder.getWorldName(arguments[1], WorldVariant.BALANCE) : WorldFinder.getWorld(sender, WorldVariant.BALANCE);
      String currencyName = (arguments.length >= 3) ? arguments[2] : TNE.manager().currencyManager().get(world).name();

      if(MISCUtils.isSingularPlayer(arguments[0]) && arguments.length < 3) {
        currencyName = MISCUtils.findCurrencyName(world, Bukkit.getPlayer(IDFinder.getID(arguments[0])).getLocation());
      }

      final TNECurrency currency = TNE.manager().currencyManager().get(world, currencyName);


      if(!currency.getCurrencyType().offline() && Bukkit.getPlayer(IDFinder.getID(arguments[0])) == null) {
        Message offlineType = new Message("Messages.Money.TypeOffline");
        offlineType.addVariable("$type", currency.getCurrencyType().name());
        offlineType.translate(world, sender);
        return false;
      }

      if(TNE.instance().getWorldManager(world).isEconomyDisabled()) {
        new Message("Messages.General.Disabled").translate(world, sender);
        return false;
      }

      TNETransaction transaction = new TNETransaction(TNE.manager().getAccount(IDFinder.getID(sender)), TNE.manager().getAccount(id), world, TNE.transactionManager().getType("inquiry"));
      transaction.setRecipientCharge(new TransactionCharge(world, currency, BigDecimal.ZERO));
      TransactionResult result = transaction.perform();
      Message message = new Message(result.initiatorMessage());
      message.addVariable("$player", arguments[0]);
      message.addVariable("$world", world);
      message.addVariable("$amount", CurrencyFormatter.format(TNE.manager().currencyManager().get(world, transaction.recipientBalance().getCurrency().name()), world, transaction.recipientBalance().getAmount(), transaction.recipient()));
      message.translate(world, IDFinder.getID(sender));
      TNE.debug("===END MoneyOtherCommand  ===");
      return result.proceed();
    }
    help(sender);
    TNE.debug("===END MoneyOtherCommand  ===");
    return false;
  }
}