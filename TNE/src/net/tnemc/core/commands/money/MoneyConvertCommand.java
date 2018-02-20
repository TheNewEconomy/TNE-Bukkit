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
import net.tnemc.core.economy.transaction.charge.TransactionChargeType;
import net.tnemc.core.economy.transaction.result.TransactionResult;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.math.BigDecimal;
import java.util.UUID;

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
    Player player = getPlayer(sender);
    if(arguments.length >= 2) {
      UUID id = IDFinder.getID(sender);
      TNEAccount account = TNE.manager().getAccount(id);
      String worldTo = (arguments[1].contains(":"))? arguments[1].split(":")[1] : WorldFinder.getWorld(sender, WorldVariant.BALANCE);
      String currencyTo = (arguments[1].contains(":"))? arguments[1].split(":")[0] : arguments[1];
      TNECurrency to = TNE.manager().currencyManager().get(worldTo, currencyTo);
      TNECurrency from = TNE.manager().currencyManager().get(WorldFinder.getWorld(sender, WorldVariant.BALANCE));
      String worldFrom = WorldFinder.getWorld(sender, WorldVariant.BALANCE);
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
        return false;
      }

      BigDecimal value = new BigDecimal(parsed);
      TNETransaction transaction = new TNETransaction(account, account, worldFrom, TNE.transactionManager().getType("conversion"));
      transaction.setInitiatorCharge(new TransactionCharge(worldFrom, from, value, TransactionChargeType.LOSE));
      transaction.setRecipientCharge(new TransactionCharge(worldTo, to, value, TransactionChargeType.GAIN));
      TransactionResult result = TNE.transactionManager().perform(transaction);

      Message message = new Message(result.recipientMessage());
      message.addVariable("$player", arguments[0]);
      message.addVariable("$world", worldTo);
      message.addVariable("$currency", to.name());
      message.addVariable("$worldFrom", worldFrom);
      message.addVariable("$currencyFrom", from.name());
      message.addVariable("$amount", CurrencyFormatter.format(currencyTo, worldTo, transaction.recipientCharge().getEntry().getAmount()));
      message.translate(worldTo, IDFinder.getID(sender));
      return result.proceed();
    }
    help(sender);
    return false;
  }
}