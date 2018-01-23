package net.tnemc.core.commands.currency;

import com.github.tnerevival.commands.TNECommand;
import com.github.tnerevival.core.Message;
import net.tnemc.core.TNE;
import net.tnemc.core.common.account.WorldFinder;
import org.bukkit.command.CommandSender;

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
 * Created by Daniel on 7/16/2017.
 */
public class CurrencyRenameCommand extends TNECommand {

  public CurrencyRenameCommand(TNE plugin) {
    super(plugin);
  }

  @Override
  public String getName() {
    return "rename";
  }

  @Override
  public String[] getAliases() {
    return new String[0];
  }

  @Override
  public String getNode() {
    return "tne.currency.rename";
  }

  @Override
  public boolean console() {
    return true;
  }

  @Override
  public String getHelp() {
    return "Messages.Commands.currency.Rename";
  }

  @Override
  public boolean execute(CommandSender sender, String command, String[] arguments) {
    if(arguments.length >= 2) {
      String world = WorldFinder.getWorld(sender);
      String currency = arguments[0];
      String newName = arguments[1];

      if(!TNE.manager().currencyManager().contains(world, currency)) {
        Message m = new Message("Messages.Money.NoCurrency");
        m.addVariable("$currency", currency);
        m.addVariable("$world", world);
        m.translate(world, sender);
        return false;
      }

      if(TNE.manager().currencyManager().contains(world, newName)) {
        Message m = new Message("Messages.Currency.AlreadyExists");
        m.addVariable("$currency", newName);
        m.addVariable("$world", world);
        m.translate(world, sender);
        return false;
      }

      TNE.manager().currencyManager().rename(world, currency, newName, true);
      Message m = new Message("Messages.Currency.Renamed");
      m.addVariable("$currency", currency);
      m.addVariable("$new_name", newName);
      m.addVariable("$world", world);
      m.translate(world, sender);
      return true;
    }
    help(sender);
    return false;
  }
}