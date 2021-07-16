package net.tnemc.core.commands.transaction;

import net.tnemc.commands.core.CommandExecution;
import net.tnemc.commands.core.provider.PlayerProvider;
import net.tnemc.core.TNE;
import net.tnemc.core.common.Message;
import net.tnemc.core.common.WorldVariant;
import net.tnemc.core.common.account.WorldFinder;
import net.tnemc.core.common.transaction.TNETransaction;
import net.tnemc.core.common.utils.MISCUtils;
import org.bukkit.command.CommandSender;

import java.util.UUID;

/**
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by Daniel on 7/10/2017.
 */
public class TransactionVoidCommand implements CommandExecution {

  @Override
  public boolean execute(PlayerProvider provider, String label, String[] arguments) {
    CommandSender sender = MISCUtils.getSender(provider);
    if(arguments.length >= 1) {
      String world = WorldFinder.getWorld(sender, WorldVariant.BALANCE);
      UUID uuid = null;

      if(TNE.instance().getWorldManager(world).isEconomyDisabled()) {
        new Message("Messages.General.Disabled").translate(world, sender);
        return false;
      }

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
    MISCUtils.help(sender, label, arguments);
    return false;
  }
}