package net.tnemc.core.commands.currency;

import com.github.tnerevival.commands.TNECommand;
import com.github.tnerevival.core.Message;
import net.tnemc.core.TNE;
import net.tnemc.core.common.currency.TNECurrency;
import net.tnemc.core.common.currency.TNETier;
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
public class CurrencyTiersCommand extends TNECommand {

  public CurrencyTiersCommand(TNE plugin) {
    super(plugin);
  }

  @Override
  public String getName() {
    return "tiers";
  }

  @Override
  public String[] getAliases() {
    return new String[] {
        "t"
    };
  }

  @Override
  public String getNode() {
    return "tne.currency.tiers";
  }

  @Override
  public boolean console() {
    return true;
  }

  @Override
  public String getHelp() {
    return "Messages.Commands.Currency.Tiers";
  }

  @Override
  public boolean execute(CommandSender sender, String command, String[] arguments) {
    String world = (arguments.length >= 2)? arguments[1] : TNE.instance().defaultWorld;
    String currencyName = (arguments.length >= 1)? arguments[0] : TNE.manager().currencyManager().get(world).name();
    TNECurrency currency = TNE.manager().currencyManager().get(world, currencyName);

    if(!TNE.manager().currencyManager().contains(world, currencyName)) {
      Message m = new Message("Messages.Money.NoCurrency");
      m.addVariable("$currency", currencyName);
      m.addVariable("$world", world);
      m.translate(world, sender);
      return false;
    }

    StringBuilder major = new StringBuilder();
    StringBuilder minor = new StringBuilder();

    for(TNETier tier : currency.getTNEMajorTiers().values()) {
      if(major.length() > 0) major.append(", ");
      major.append(tier.singular());
    }

    for(TNETier tier : currency.getTNEMinorTiers().values()) {
      if(minor.length() > 0) minor.append(", ");
      minor.append(tier.singular());
    }
    Message message = new Message("Messages.Currency.Tiers");
    message.addVariable("$currency", world);
    message.addVariable("$major_tiers", major.toString());
    message.addVariable("$minor_tiers", minor.toString());
    message.translate(world, sender);
    return true;
  }
}