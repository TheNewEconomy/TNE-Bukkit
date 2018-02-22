package net.tnemc.core.commands.money;

import com.github.tnerevival.commands.TNECommand;
import com.github.tnerevival.core.Message;
import com.github.tnerevival.user.IDFinder;
import net.tnemc.core.TNE;
import net.tnemc.core.common.WorldVariant;
import net.tnemc.core.common.account.TNEAccount;
import net.tnemc.core.common.account.WorldFinder;
import net.tnemc.core.common.currency.CurrencyFormatter;
import net.tnemc.core.common.currency.TNECurrency;
import net.tnemc.core.common.transaction.TNETransaction;
import net.tnemc.core.economy.transaction.charge.TransactionCharge;
import net.tnemc.core.economy.transaction.result.TransactionResult;
import org.bukkit.command.CommandSender;

import java.math.BigDecimal;
import java.util.*;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * Created by Daniel on 7/10/2017.
 */
public class MoneyBalanceCommand extends TNECommand {

  public MoneyBalanceCommand(TNE plugin) {
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
    return "tne.money.balance";
  }

  @Override
  public boolean console() {
    return true;
  }

  @Override
  public String getHelp() {
    return "Messages.Commands.Money.Balance";
  }

  @Override
  public boolean execute(CommandSender sender, String command, String[] arguments) {
    TNE.debug("===START MoneyBalanceCommand  ===");
    String world = (arguments.length >= 1)? arguments[0] : WorldFinder.getWorld(sender, WorldVariant.BALANCE);
    if(TNE.instance().getWorldManager(world) == null) world = WorldFinder.getWorld(sender, WorldVariant.BALANCE);
    world = TNE.instance().getWorldManager(world).getBalanceWorld();
    TNE.debug("MoneyBalanceCommand.execute, World: " + world);
    if(TNE.manager() == null) TNE.debug("Economy Manager is null");
    if(TNE.manager().currencyManager() == null) TNE.debug("TNECurrency Manager is null");
    if(TNE.manager().currencyManager().get(world) == null) TNE.debug("World TNECurrency is null");
    String currencyName = (arguments.length >= 2)? arguments[1] : TNE.manager().currencyManager().get(world).name();
    UUID id = IDFinder.getID(sender);
    TNEAccount account = TNE.manager().getAccount(id);
    TNE.debug("World: " + world);
    TNE.debug("Args Length: " + arguments.length);

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
      TNE.debug("BalanceCommand Currency Loop.. Currency: " + cur.name());
      TNETransaction transaction = new TNETransaction(account, account, world, TNE.transactionManager().getType("inquiry"));
      transaction.setRecipientCharge(new TransactionCharge(world, cur, BigDecimal.ZERO));
      TNE.debug("BalanceCommand RecipientChargeCurrency: " + transaction.recipientCharge().getCurrency().name());
      TNE.debug("BalanceCommand RecipientCharge: " + transaction.recipientCharge().getAmount().toPlainString());
      result = TNE.transactionManager().perform(transaction);
      balances.put(cur.name(), transaction.recipientBalance().getAmount());
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
        m.addVariable("$amount", balance.toPlainString());
        m.translate(w, sender, id.toString());
      });
      TNE.debug("===END MoneyBalanceCommand ===");
      return result.proceed();
    }
    Message message = new Message(result.recipientMessage());
    message.addVariable("$player", account.displayName());
    message.addVariable("$world", world);
    message.addVariable("$currency", currencyName);
    message.addVariable("$amount", CurrencyFormatter.format(world, currencyName, balances.get(currencyName)));
    message.translate(world, sender, id.toString());
    TNE.debug("===END MoneyBalanceCommand ===");
    return result.proceed();
  }
}