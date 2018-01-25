package net.tnemc.core.commands.currency;

import com.github.tnerevival.commands.TNECommand;
import com.github.tnerevival.core.Message;
import net.tnemc.core.TNE;
import net.tnemc.core.common.currency.TNECurrency;
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
 * Created by Daniel on 7/10/2017.
 */
public class CurrencyListCommand extends TNECommand {

  public CurrencyListCommand(TNE plugin) {
    super(plugin);
  }

  @Override
  public String getName() {
    return "list";
  }

  @Override
  public String[] getAliases() {
    return new String[] {
        "l"
    };
  }

  @Override
  public String getNode() {
    return "tne.currency.list";
  }

  @Override
  public boolean console() {
    return true;
  }

  @Override
  public String getHelp() {
    return "Messages.Commands.Currency.List";
  }

  @Override
  public boolean execute(CommandSender sender, String command, String[] arguments) {
    String world = (arguments.length >= 1)? arguments[0] : TNE.instance().defaultWorld;
    StringBuilder builder = new StringBuilder();

    for(TNECurrency currency : TNE.manager().currencyManager().getWorldCurrencies(world)) {
      if(builder.length() > 0) builder.append(", ");
      builder.append(currency.name());
    }

    Message message = new Message("Messages.Currency.List");
    message.addVariable("$world", world);
    message.addVariable("$currencies", builder.toString());
    message.translate(world, sender);
    return true;
  }
}