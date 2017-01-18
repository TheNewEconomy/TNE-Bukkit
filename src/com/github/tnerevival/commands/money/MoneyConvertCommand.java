/*
 * The New Economy Minecraft Server Plugin
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.

 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.github.tnerevival.commands.money;

import com.github.tnerevival.TNE;
import com.github.tnerevival.account.IDFinder;
import com.github.tnerevival.commands.TNECommand;
import com.github.tnerevival.core.Message;
import com.github.tnerevival.core.currency.Currency;
import com.github.tnerevival.core.currency.CurrencyFormatter;
import com.github.tnerevival.core.transaction.TransactionType;
import com.github.tnerevival.utils.AccountUtils;
import com.github.tnerevival.utils.MISCUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by creatorfromhell on 11/23/2016.
 **/
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
  public boolean execute(CommandSender sender, String command, String[] arguments) {
    Player player = getPlayer(sender);
    if(arguments.length >= 2) {
      Double value = CurrencyFormatter.translateDouble(arguments[0], IDFinder.getWorld(getPlayer(sender)));
      if(value < 0) {
        help(sender);
        return false;
      }

      String worldTo = (arguments[1].contains(":"))? arguments[1].split(":")[1] : getWorld(sender);
      String currencyTo = (arguments[1].contains(":"))? arguments[1].split(":")[0] : arguments[1];

      if(!TNE.instance.manager.currencyManager.contains(worldTo, currencyTo)) {
        Message paid = new Message("Messages.Money.NoCurrency");
        paid.addVariable("$currency", currencyTo);
        paid.addVariable("$world", worldTo);
        paid.translate(getWorld(sender), player);
        return false;
      }

      Currency to = TNE.instance.manager.currencyManager.get(worldTo, currencyTo);
      Currency from = TNE.instance.manager.currencyManager.get(getWorld(sender));

      if(arguments.length >= 3) {
        String worldFrom = (arguments[2].contains(":"))? arguments[2].split(":")[1] : getWorld(sender);
        String currencyFrom = (arguments[2].contains(":"))? arguments[2].split(":")[0] : arguments[2];

        if(!TNE.instance.manager.currencyManager.contains(worldFrom, currencyFrom)) {
          Message paid = new Message("Messages.Money.NoCurrency");
          paid.addVariable("$currency", currencyFrom);
          paid.addVariable("$world", worldFrom);
          paid.translate(getWorld(sender), player);
          return false;
        }
        from = TNE.instance.manager.currencyManager.get(worldFrom, currencyFrom);
      }

      if(!AccountUtils.transaction(IDFinder.getID(player).toString(), null, AccountUtils.round(value), from, TransactionType.MONEY_INQUIRY, IDFinder.getWorld(player))) {
        Message insufficient = new Message("Messages.Money.Insufficient");
        insufficient.addVariable("$amount", CurrencyFormatter.format(player.getWorld().getName(), AccountUtils.round(Double.valueOf(arguments[1]))));
        insufficient.translate(IDFinder.getWorld(player), player);
        return false;
      }

      AccountUtils.transaction(IDFinder.getID(player).toString(), null, AccountUtils.round(value), from, TransactionType.MONEY_REMOVE, IDFinder.getWorld(player));

      double converted = TNE.instance.manager.currencyManager.convert(from, to, value);
      AccountUtils.transaction(IDFinder.getID(player).toString(), null, AccountUtils.round(converted), to, TransactionType.MONEY_GIVE, IDFinder.getWorld(player));

      Message success = new Message("Messages.Money.Converted");
      success.addVariable("$from_amount", CurrencyFormatter.format(from, AccountUtils.round(value)));
      success.addVariable("$amount", CurrencyFormatter.format(to, AccountUtils.round(converted)));
      success.addVariable("$from_currency", from.toString());
      success.addVariable("$currency", to.toString());
      success.translate(IDFinder.getWorld(player), player);
      return false;
    }
    help(sender);
    return false;
  }

  @Override
  public String getHelp() {
    return "/money convert <amount> <to currency[:world]> [from currency[:world]] - Convert some of your funds from one currency to another.";
  }

}