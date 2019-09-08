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
import net.tnemc.core.economy.currency.CurrencyEntry;
import net.tnemc.core.economy.transaction.charge.TransactionCharge;
import net.tnemc.core.economy.transaction.result.TransactionResult;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by Daniel on 7/10/2017.
 */
public class MoneyBalanceCommand extends TNECommand {

  public MoneyBalanceCommand(TNE plugin) {
    super(plugin);
  }

  @Override
  public String name() {
    return "balance";
  }

  @Override
  public String[] aliases() {
    return new String[] {
        "bal"
    };
  }

  @Override
  public String node() {
    return "tne.money.balance";
  }

  @Override
  public boolean console() {
    return true;
  }

  @Override
  public String helpLine() {
    return "Messages.Commands.Money.Balance";
  }

  /*@Override
  public List<String> onTab(CommandSender sender, Command command, String alias, String[] arguments, boolean shortened) {
    Map<Integer, String> argTypes = new HashMap<>();
    argTypes.put(0, "world");
    argTypes.put(1, "currency");
    return buildSuggestions(sender, shortened, arguments, argTypes, 0);
  }*/

  @Override
  public boolean execute(CommandSender sender, String command, String[] arguments) {
    Bukkit.getScheduler().runTaskAsynchronously(plugin, ()->{

      TNE.debug("===START MoneyBalanceCommand  ===");
      String world = (arguments.length >= 1)? arguments[0] : WorldFinder.getWorld(sender, WorldVariant.BALANCE);
      if(TNE.instance().getWorldManager(world) == null) world = WorldFinder.getWorld(sender, WorldVariant.BALANCE);
      world = TNE.instance().getWorldManager(world).getBalanceWorld();
      TNE.debug("MoneyBalanceCommand.execute, World: " + world);
      final UUID id = IDFinder.getID(sender);
      if(TNE.manager() == null) TNE.debug("Economy Manager is null");
      if(TNE.manager().currencyManager() == null) TNE.debug("TNECurrency Manager is null");
      if(TNE.manager().currencyManager().get(world) == null) TNE.debug("World TNECurrency is null");
      String currencyName = (arguments.length >= 2)? arguments[1] : TNE.manager().currencyManager().get(world).name();

      if(sender instanceof Player == false && arguments.length == 0){
        new Message("Messages.General.IsConsole");
        return;
      }

      if(arguments.length < 2) {
        currencyName = MISCUtils.findCurrencyName(world, Bukkit.getPlayer(id).getLocation());
      }

      final TNEAccount account = TNE.manager().getAccount(id);
      TNE.debug("World: " + world);
      TNE.debug("Args Length: " + arguments.length);

      if(TNE.instance().getWorldManager(world).isEconomyDisabled()) {
        new Message("Messages.General.Disabled").translate(world, sender);
        return;
      }

      Map<String, BigDecimal> balances = new HashMap<>();

      List<TNECurrency> currencies = new ArrayList<>();
      if(arguments.length >= 2) {
        currencies.add(TNE.manager().currencyManager().get(world, currencyName));
      } else {
        currencies.addAll(TNE.instance().getWorldManager(world).getCurrencies());
      }
      TNE.debug("Pre transactions of MoneyBalanceCommand");
      TransactionResult result = null;

      for(TNECurrency cur : currencies) {
        if(cur.getCurrencyType().offline() || Bukkit.getPlayer(id) != null) {
          TNE.debug("BalanceCommand Currency Loop.. Currency: " + cur.name());
          TNETransaction transaction = new TNETransaction(account, account, world, TNE.transactionManager().getType("inquiry"));
          transaction.setRecipientCharge(new TransactionCharge(world, cur, BigDecimal.ZERO));
          transaction.setRecipientBalance(new CurrencyEntry(world, cur, account.getHoldings(world, cur)));
          TNE.debug("BalanceCommand RecipientChargeCurrency: " + transaction.recipientCharge().getCurrency().name());
          TNE.debug("BalanceCommand RecipientCharge: " + transaction.recipientCharge().getAmount().toPlainString());
          result = TNE.transactionManager().perform(transaction);
          balances.put(cur.name(), transaction.recipientBalance().getAmount());
        }
      }

      TNE.debug("Balances Size: " + balances.size());
      TNE.debug("Post transactions of MoneyBalanceCommand");
      if(balances.size() > 1) {
        final String w = world;
        Message message = new Message("Messages.Money.HoldingsMulti");
        message.addVariable("$player", account.displayName());
        message.addVariable("$world", world);
        message.translate(world, sender, id.toString());
        balances.forEach((curName, balance)->{
          Message m = new Message("Messages.Money.HoldingsMultiSingle");
          m.addVariable("$currency", curName);
          m.addVariable("$amount", CurrencyFormatter.format(TNE.manager().currencyManager().get(w, curName), w, balances.get(curName), id.toString()));
          if(!TNE.instance().api().getBoolean("Core.Currency.Info.FormatMoney")) {
            m.addVariable("$amount", balances.get(curName).toPlainString());
          } else {
            m.addVariable("$amount", CurrencyFormatter.format(TNE.manager().currencyManager().get(w, curName), w, balances.get(curName), id.toString()));
          }
          m.translate(w, sender, id.toString());
        });
        TNE.debug("===END MoneyBalanceCommand ===");
        return;
      }

      if(balances.size() > 0) {
        Message message = new Message(result.recipientMessage());
        message.addVariable("$player", account.displayName());
        message.addVariable("$world", world);
        message.addVariable("$currency", currencyName);
        if (!TNE.instance().api().getBoolean("Core.Currency.Info.FormatMoney")) {
          message.addVariable("$amount", balances.get(currencyName).toPlainString());
        } else {
          message.addVariable("$amount", CurrencyFormatter.format(TNE.manager().currencyManager().get(world, currencyName), world, balances.get(currencyName), id.toString()));
        }
        message.translate(world, sender, id.toString());
        return;
      }
      help(sender);
      TNE.debug("===END MoneyBalanceCommand ===");
    });
    return true;
  }
}