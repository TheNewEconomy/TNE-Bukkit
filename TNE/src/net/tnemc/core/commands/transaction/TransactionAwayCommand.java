package net.tnemc.core.commands.transaction;

import com.github.tnerevival.core.collection.paginate.Page;
import com.github.tnerevival.core.collection.paginate.Paginator;
import net.tnemc.commands.core.CommandExecution;
import net.tnemc.commands.core.provider.PlayerProvider;
import net.tnemc.core.TNE;
import net.tnemc.core.common.Message;
import net.tnemc.core.common.WorldVariant;
import net.tnemc.core.common.account.WorldFinder;
import net.tnemc.core.common.api.IDFinder;
import net.tnemc.core.common.transaction.TNETransaction;
import net.tnemc.core.common.utils.MISCUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by Daniel on 7/12/2017.
 */
public class TransactionAwayCommand implements CommandExecution {

  @Override
  public boolean execute(PlayerProvider provider, String label, String[] arguments) {
    CommandSender sender = MISCUtils.getSender(provider);
    String world = WorldFinder.getWorld(sender, WorldVariant.BALANCE);
    Player player = MISCUtils.getPlayer(sender);
    int page = 1;

    if(TNE.instance().getWorldManager(world).isEconomyDisabled()) {
      new Message("Messages.General.Disabled").translate(world, sender);
      return false;
    }

    if(arguments.length >= 1 && MISCUtils.isInteger(arguments[0])) {
      page = Integer.parseInt(arguments[0]);
    }

    List<UUID> away = TNE.manager().getAccount(IDFinder.getID(sender)).getHistory().getAway();

    if(away.size() > 0) {
      Paginator paginator = new Paginator(new ArrayList<>(away), 5);

      if (page > paginator.getMaxPages()) page = paginator.getMaxPages();
      Page p = paginator.getPage(page);

      Message transactions = new Message("Messages.Transaction.Away");
      transactions.addVariable("$page", page + "");
      transactions.addVariable("$page_top", paginator.getMaxPages() + "");
      transactions.translate(world, sender);

      for(Object obj : p.getElements()) {
        if(obj != null && obj instanceof UUID) {
          TNETransaction transaction = TNE.transactionManager().get((UUID)obj);

          Message entry = new Message("Messages.Transaction.AwayEntry");
          entry.addVariable("$id", transaction.transactionID().toString());
          entry.addVariable("$type", transaction.type().name());
          entry.translate(world, sender);
        }
      }
      return true;
    }
    new Message("Messages.Transaction.AwayNone").translate(world, player);
    return false;
  }
}