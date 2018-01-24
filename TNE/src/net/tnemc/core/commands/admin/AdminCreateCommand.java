package net.tnemc.core.commands.admin;

import com.github.tnerevival.commands.TNECommand;
import com.github.tnerevival.core.Message;
import com.github.tnerevival.user.IDFinder;
import net.tnemc.core.TNE;
import net.tnemc.core.common.WorldVariant;
import net.tnemc.core.common.account.TNEAccount;
import net.tnemc.core.common.account.WorldFinder;
import net.tnemc.core.common.currency.CurrencyFormatter;
import org.bukkit.command.CommandSender;

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
public class AdminCreateCommand extends TNECommand {

  public AdminCreateCommand(TNE plugin) {
    super(plugin);
  }

  @Override
  public String getName() {
    return "create";
  }

  @Override
  public String[] getAliases() {
    return new String[0];
  }

  @Override
  public String getNode() {
    return "tne.admin.create";
  }

  @Override
  public boolean console() {
    return true;
  }

  @Override
  public String getHelp() {
    return "Messages.Commands.Admin.Create";
  }

  @Override
  public boolean execute(CommandSender sender, String command, String[] arguments) {
    if(arguments.length >= 1) {
      String world = WorldFinder.getWorld(sender, WorldVariant.BALANCE);
      UUID id = IDFinder.getID(arguments[0]);
      if(!TNE.manager().exists(id)) {
        BigDecimal initial = new BigDecimal(0.0);
        if(arguments.length >= 2) {
          try {
            initial = new BigDecimal(arguments[1]);
          } catch(Exception e) {
            TNE.debug("Invalid amount: " + arguments[1]);
          }
        }
        TNEAccount acc = new TNEAccount(id, IDFinder.getUsername(arguments[0]));
        acc.initializeHoldings(world);
        if(initial.compareTo(new BigDecimal(0.0)) > 0) {
          acc.setHoldings(world, TNE.manager().currencyManager().get(world).name(), initial);
        }
        TNE.manager().addAccount(acc);

        Message m = new Message("Messages.Admin.Created");
        m.addVariable("$player", arguments[0]);
        m.addVariable("$amount", CurrencyFormatter.format(world, TNE.instance().api().getHoldings(id.toString(), world)));
        m.translate(world, sender);
        return true;
      }
      new Message("Messages.Admin.Exists").translate(world, sender);
      return false;
    }
    help(sender);
    return false;
  }
}