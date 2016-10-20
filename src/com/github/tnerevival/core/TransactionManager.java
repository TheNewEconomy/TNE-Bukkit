package com.github.tnerevival.core;

import com.github.tnerevival.TNE;
import com.github.tnerevival.core.transaction.*;

import java.util.*;

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
public class TransactionManager {
  private Map<String, TransactionHistory> transactionHistory = new HashMap<>();

  public void add(Transaction transaction) {
    if(transaction.getInitiator() != null) {
      add(transaction.getInitiator(),
          transaction.getRecipient(),
          transaction.getWorld(),
          transaction.getType(),
          transaction.getCost(),
          transaction.getInitiatorOldBalance(),
          transaction.getInitiatorBalance()
      );
    }

    if(transaction.getRecipient() != null) {
      add(transaction.getRecipient(),
          transaction.getInitiator(),
          transaction.getWorld(),
          transaction.getType(),
          transaction.getCost(),
          transaction.getRecipientOldBalance(),
          transaction.getRecipientBalance()
      );
    }
  }

  public void add(String id, String player, String world, TransactionType type, TransactionCost cost, Double oldBalance, Double balance) {
    if(TNE.instance.api.getBoolean("Core.Transactions.Track", world, id)) {
      Calendar time = new GregorianCalendar(TimeZone.getTimeZone(TNE.instance.api.getString("Core.Transactions.Timezone", world, id)));
      time.setTimeInMillis(Calendar.getInstance().getTimeInMillis());

      String playerFrom = (player == null)? "N/A" : player;

      Record r = new Record(id, player, world, type.getID(), cost.getAmount(), oldBalance, balance, time.getTimeInMillis(), time.getTimeZone().getID());
      if(!transactionHistory.containsKey(id)) {
        TransactionHistory history = new TransactionHistory();
        history.add(r);
        transactionHistory.put(id, history);
      }
      transactionHistory.get(id).add(r);
    }
  }

  public boolean hasHistory(String id) {
    return transactionHistory.containsKey(id);
  }

  public TransactionHistory getHistory(String id) {
    if (hasHistory(id)) {
      return transactionHistory.get(id);
    }
    return null;
  }
}
