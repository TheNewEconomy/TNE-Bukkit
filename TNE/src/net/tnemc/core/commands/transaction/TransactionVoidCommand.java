package net.tnemc.core.commands.transaction;

import com.github.tnerevival.commands.TNECommand;
import com.github.tnerevival.core.Message;
import net.tnemc.core.TNE;
import net.tnemc.core.common.WorldVariant;
import net.tnemc.core.common.account.WorldFinder;
import net.tnemc.core.common.transaction.TNETransaction;
import org.bukkit.command.CommandSender;

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
public class TransactionVoidCommand extends TNECommand {

  public TransactionVoidCommand(TNE plugin) {
    super(plugin);
  }

  @Override
  public String getName() {
    return "void";
  }

  @Override
  public String[] getAliases() {
    return new String[0];
  }

  @Override
  public String getNode() {
    return "tne.transaction.void";
  }

  @Override
  public boolean console() {
    return true;
  }

  @Override
  public String getHelp() {
    return "Messages.Commands.Transaction.Void";
  }

  @Override
  public boolean execute(CommandSender sender, String command, String[] arguments) {
    if(arguments.length >= 1) {
      String world = WorldFinder.getWorld(sender, WorldVariant.BALANCE);
      UUID uuid = null;
      try {
        uuid = UUID.fromString(arguments[0]);
      } catch(IllegalArgumentException exception) {
        TNE.debug(exception);
      }
      if(uuid == null || !TNE.transactionManager().isValid(uuid)) {
        Message message = new Message("Messages.Transaction.Invalid");
        message.addVariable("$transaction", arguments[0]);
        message.translate(world, sender);
        return false;
      }

      TNETransaction transaction = TNE.transactionManager().get(uuid);
      if(transaction.voided()) {
        Message message = new Message("Messages.Transaction.Already");
        message.addVariable("$transaction", arguments[0]);
        message.translate(world, sender);
        return false;
      }

      boolean result = transaction.voidTransaction();
      if(!result) {
        Message message = new Message("Messages.Transaction.Unable");
        message.addVariable("$transaction", arguments[0]);
        message.translate(world, sender);
        return false;
      }
      Message message = new Message("Messages.Transaction.Voided");
      message.addVariable("$transaction", arguments[0]);
      message.translate(world, sender);
      return true;
    }
    help(sender);
    return false;
  }
}