package com.github.tnerevival.commands.money;

import com.github.tnerevival.TNE;
import com.github.tnerevival.account.IDFinder;
import com.github.tnerevival.commands.TNECommand;
import com.github.tnerevival.core.Message;
import com.github.tnerevival.core.currency.CurrencyFormatter;
import com.github.tnerevival.core.transaction.Record;
import com.github.tnerevival.core.transaction.TransactionHistory;
import com.github.tnerevival.utils.AccountUtils;
import com.github.tnerevival.utils.MISCUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
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
 * Created by creatorfromhell on 10/20/2016.
 */
public class MoneyHistoryCommand extends TNECommand {

  public MoneyHistoryCommand(TNE plugin) {
    super(plugin);
  }

  @Override
  public String getName() {
    return "history";
  }

  @Override
  public String[] getAliases() {
    return new String[0];
  }

  @Override
  public String getNode() {
    return "tne.money.history";
  }

  @Override
  public boolean console() {
    return false;
  }

  @Override
  public boolean execute(CommandSender sender, String command, String[] arguments) {
    Player player = getPlayer(sender);
    String world = getWorld(sender);
    String type = "all";
    int page = 1;

    for(String s : arguments) {
      if(s.contains(":")) {
        String[] split = s.toLowerCase().split(":");
        switch (split[0]) {
          case "page":
            if(MISCUtils.isInteger(split[1])) {
              page = Integer.parseInt(split[1]);
            }
            break;
          case "world":
            world = split[1];
            break;
          case "type":
            type = split[1];
            break;
        }
      } else {
        if(MISCUtils.isInteger(s)) {
          page = Integer.parseInt(s);
        }
      }
    }

    String id = IDFinder.getID(player).toString();
    TransactionHistory history = TNE.instance.manager.transactions.getHistory(id);

    if(history != null) {
      List<Record> records = history.getRecords(world, type, page);
      MISCUtils.debug(history.getMaxPages(world, type, 5) + "");
      Integer max = history.getMaxPages(world, type, 5);

      player.sendMessage(ChatColor.WHITE + "Type | Player | World | Amount | BalanceAfter | Time - Page " + page + "/" + max);
      if (records.size() > 0) {
        for (Record r : records) {
          MISCUtils.debug((r == null) + "");
          Double difference = AccountUtils.round(r.getBalance() - r.getOldBalance());
          String amount = ((difference >= 0.0) ? ChatColor.GREEN + "+" : ChatColor.RED + "") + difference;

          String time = r.convert(world, IDFinder.getID(player), TNE.instance.api.getString("Core.Transactions.Timezone", world, IDFinder.getID(player)));

          Player p = null;
          if(r.getPlayer() != null && IDFinder.isUUID(r.getPlayer())) {
            p = MISCUtils.getPlayer(UUID.fromString(r.getPlayer()));
          }
          StringBuilder builder = new StringBuilder();
          builder.append(ChatColor.GREEN + r.getType() + ChatColor.WHITE + " | ");
          builder.append(ChatColor.GREEN + ((p == null)? r.getPlayer() : p.getDisplayName()) + ChatColor.WHITE + " | ");
          builder.append(ChatColor.GREEN + r.getWorld() + ChatColor.WHITE + " | ");
          builder.append(ChatColor.GREEN + amount + ChatColor.WHITE + " | ");
          builder.append(ChatColor.GREEN + CurrencyFormatter.format(r.getWorld(), r.getBalance()) + ChatColor.WHITE + " | ");
          builder.append(ChatColor.GREEN + (time + "") + ChatColor.WHITE);

          sender.sendMessage(builder.toString());
        }
        return true;
      }
    }
    new Message("Messages.Account.NoTransactions").translate(world, player);
    return true;
  }

  @Override
  public String[] getHelpLines() {
    return new String[] {
        "/money history [page:#] [world:name/all] [type:type/all] - See a detailed break down of your transaction history.",
        "[page] - The page number you wish to view",
        "[world] - The world name you wish to filter, or all for every world. Defaults to current world.",
        "[type] - The transaction type you wish to filter, or all for every transaction."
    };
  }
}