package net.tnemc.core.commands.transaction;

import com.github.tnerevival.core.collection.paginate.Page;
import com.github.tnerevival.core.collection.paginate.Paginator;
import com.github.tnerevival.user.IDFinder;
import net.tnemc.core.TNE;
import net.tnemc.core.commands.TNECommand;
import net.tnemc.core.common.Message;
import net.tnemc.core.common.WorldVariant;
import net.tnemc.core.common.account.WorldFinder;
import net.tnemc.core.common.transaction.TNETransaction;
import net.tnemc.core.common.utils.MISCUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by Daniel on 7/10/2017.
 */
public class TransactionHistoryCommand extends TNECommand {

  public TransactionHistoryCommand(TNE plugin) {
    super(plugin);
  }

  @Override
  public String getName() {
    return "history";
  }

  @Override
  public String[] getAliases() {
    return new String[] {
        "h"
    };
  }

  @Override
  public String getNode() {
    return "tne.transaction.history";
  }

  @Override
  public boolean console() {
    return true;
  }

  @Override
  public String getHelp() {
    return "Messages.Commands.Transaction.History";
  }

  @Override
  public boolean execute(CommandSender sender, String command, String[] arguments) {
    Map<String, String> parsed = getArguments(arguments);
    Player player = getPlayer(sender);
    String world = WorldFinder.getWorld(sender, WorldVariant.BALANCE);
    String type = "all";
    int page = 1;

    if(parsed.containsKey("page") && MISCUtils.isInteger(parsed.get("page"))) {
      page = Integer.parseInt(parsed.get("page"));
    }

    if(parsed.containsKey("world")) {
      world = parsed.get("world");
    }

    if(parsed.containsKey("type")) {
      type = parsed.get("type");
    }
    UUID id = IDFinder.getID(player);
    List<UUID> history = TNE.manager().getAccount(IDFinder.getID(player)).getHistory().getHistroy(world);
    if(history.size() > 0) {
      Paginator paginator = new Paginator(new ArrayList<>(history), 5);

      if (page > paginator.getMaxPages()) page = paginator.getMaxPages();
      Page p = paginator.getPage(page);

      Message transactions = new Message("Messages.Transaction.History");
      transactions.addVariable("$page", page + "");
      transactions.addVariable("$page_top", paginator.getMaxPages() + "");
      transactions.translate(world, sender);

      for(Object obj : p.getElements()) {
        if(obj != null && obj instanceof UUID) {
          TNETransaction transaction = TNE.transactionManager().get((UUID)obj);
          String initiator = (transaction.initiator() == null)? "N/A" : IDFinder.getUsername(transaction.initiator());
          String recipient = (transaction.recipient() == null)? "N/A" : IDFinder.getUsername(transaction.recipient());

          Message entry = new Message("Messages.Transaction.HistoryEntry");
          entry.addVariable("$id", transaction.transactionID().toString());
          entry.addVariable("$type", transaction.type().name());
          entry.addVariable("$initiator", initiator);
          entry.addVariable("$recipient", recipient);
          entry.translate(world, sender);
        }
      }
      return true;
    }
    new Message("Messages.Account.NoTransactions").translate(world, player);
    return false;
  }
}