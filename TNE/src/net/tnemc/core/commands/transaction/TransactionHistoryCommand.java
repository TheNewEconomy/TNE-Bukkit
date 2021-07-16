package net.tnemc.core.commands.transaction;

import net.tnemc.commands.core.CommandExecution;
import net.tnemc.commands.core.provider.PlayerProvider;
import net.tnemc.core.TNE;
import net.tnemc.core.common.Message;
import net.tnemc.core.common.WorldVariant;
import net.tnemc.core.common.account.WorldFinder;
import net.tnemc.core.common.api.IDFinder;
import net.tnemc.core.common.transaction.TNETransaction;
import net.tnemc.core.common.utils.MISCUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.SQLException;
import java.util.LinkedHashMap;
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
public class TransactionHistoryCommand implements CommandExecution {

  @Override
  public boolean execute(PlayerProvider provider, String label, String[] arguments) {
    CommandSender sender = MISCUtils.getSender(provider);
    Map<String, String> parsed = MISCUtils.getArguments(arguments);
    Player player = MISCUtils.getPlayer(sender);
    String world = WorldFinder.getWorld(sender, WorldVariant.BALANCE);
    String type = "all";
    int page = 1;

    if(TNE.instance().getWorldManager(world).isEconomyDisabled()) {
      new Message("Messages.General.Disabled").translate(world, sender);
      return false;
    }

    if(parsed.containsKey("player") && sender.hasPermission("tne.transactions.history.other")) {
      if(Bukkit.getServer().getPlayer(parsed.get("player")) != null) {
        player = Bukkit.getServer().getPlayer(parsed.get("player"));
      }
    }

    if(player == null) {
      Message noPlayer = new Message("Messages.General.NoPlayer");
      noPlayer.addVariable("$player", parsed.getOrDefault("player", "console"));
      return false;
    }

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
    int max = 0;
    try {
      max = TNE.saveManager().getTNEManager().getTNEProvider().transactionCount(id, world, type, "all", 10);
    } catch (SQLException e) {
      TNE.debug(e);
    }
    if(max == 0) max = 1;
    if (page > max) page = max;
    LinkedHashMap<UUID, TNETransaction> history = new LinkedHashMap<>();
    try {
      history = TNE.saveManager().getTNEManager().getTNEProvider().transactionHistory(id, world, type, "all", 10, page);
    } catch (SQLException e) {
      TNE.debug(e);
    }

    if(history.size() == 0) {
      new Message("Messages.Account.NoTransactions").translate(world, sender);
      return false;
    }


    Message transactions = new Message("Messages.Transaction.History");
    transactions.addVariable("$page", page + "");
    transactions.addVariable("$page_top", max + "");
    transactions.translate(world, sender);

    for(TNETransaction transaction : history.values()) {
      TNE.debug("Transaction null?" + (transaction == null));
      if(transaction != null) {
        String initiator = (transaction.initiator() == null || IDFinder.getUsername(transaction.initiator()) == null) ? "N/A" : IDFinder.getUsername(transaction.initiator());
        String recipient = (transaction.recipient() == null || IDFinder.getUsername(transaction.recipient()) == null) ? "N/A" : IDFinder.getUsername(transaction.recipient());

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
}