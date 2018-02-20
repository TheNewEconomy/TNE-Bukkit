package net.tnemc.core.commands.money;

import com.github.tnerevival.commands.TNECommand;
import com.github.tnerevival.core.Message;
import com.github.tnerevival.user.IDFinder;
import net.tnemc.core.TNE;
import net.tnemc.core.common.WorldVariant;
import net.tnemc.core.common.account.WorldFinder;
import net.tnemc.core.common.currency.CurrencyFormatter;
import net.tnemc.core.common.currency.TNECurrency;
import net.tnemc.core.common.transaction.MultiTransactionHandler;
import org.bukkit.command.CommandSender;

import java.math.BigDecimal;

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
public class MoneyPayCommand extends TNECommand {

  public MoneyPayCommand(TNE plugin) {
    super(plugin);
  }

  @Override
  public String getName() {
    return "pay";
  }

  @Override
  public String[] getAliases() {
    return new String[0];
  }

  @Override
  public String getNode() {
    return "tne.money.pay";
  }

  @Override
  public boolean console() {
    return false;
  }

  @Override
  public String getHelp() {
    return "Messages.Commands.Money.Pay";
  }

  @Override
  public boolean execute(CommandSender sender, String command, String[] arguments) {
    TNE.debug("===START MoneyPayCommand ===");
    String world = WorldFinder.getWorld(sender, WorldVariant.BALANCE);
    if(arguments.length >= 2) {
      String currencyName = (arguments.length >= 3) ? arguments[2] : TNE.manager().currencyManager().get(world).name();
      TNECurrency currency = TNE.manager().currencyManager().get(world, currencyName);

      String parsed = CurrencyFormatter.parseAmount(currency, world, arguments[1]);
      if(parsed.contains("Messages")) {
        Message msg = new Message(parsed);
        msg.addVariable("$currency", currency.name());
        msg.addVariable("$world", world);
        msg.addVariable("$player", getPlayer(sender).getDisplayName());
        msg.translate(world, sender);
        TNE.debug("===END MoneyPayCommand ===");
        return false;
      }

      BigDecimal value = new BigDecimal(parsed);

      MultiTransactionHandler handler = new MultiTransactionHandler(TNE.manager().parsePlayerArgument(arguments[0]),
                                                                    "pay", value, currency, world,
                                                                    TNE.manager().getAccount(IDFinder.getID(sender)));
      handler.handle(true);
      TNE.debug("===END MoneyPayCommand ===");
      return handler.getData().isProceed();
    }
    help(sender);
    TNE.debug("===END MoneyPayCommand ===");
    return false;
  }
}