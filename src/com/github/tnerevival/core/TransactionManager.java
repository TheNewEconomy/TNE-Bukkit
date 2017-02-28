package com.github.tnerevival.core;

import com.github.tnerevival.TNE;
import com.github.tnerevival.core.transaction.*;
import com.github.tnerevival.listeners.collections.TransactionsListener;
import com.github.tnerevival.utils.MISCUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
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
public class TransactionManager {
  public static TransactionsListener transListener = new TransactionsListener();
  public Map<String, TransactionHistory> transactionHistory = new HashMap<>();

  public void add(Transaction transaction) {
    if(transaction.getInitiator() != null) {
      if(transaction.getRecipient() == null ||
         !transaction.getInitiator().equals(transaction.getRecipient())) {
        add(transaction.getInitiator(),
            transaction.getRecipient(),
            transaction.getWorld(),
            transaction.getType(),
            transaction.getCost(),
            transaction.getInitiatorOldBalance(),
            transaction.getInitiatorBalance()
        );
      }
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

  public void add(String initiator, String player, String world, TransactionType type, TransactionCost cost, BigDecimal oldBalance, BigDecimal balance) {
    Date date = new Date();

    add(UUID.randomUUID().toString(), initiator, player, world, type, cost, oldBalance, balance, date.getTime());
  }

  public void add(String id, String initiator, String player, String world, TransactionType type, TransactionCost cost, BigDecimal oldBalance, BigDecimal balance, Long time) {
    if(type.equals(TransactionType.MONEY_INQUIRY)) return;
    if(TNE.instance().api().getBoolean("Core.Transactions.Track", world, initiator)) {

      String playerFrom = (player == null)? "N/A" : player;

      Record r = new Record(id, initiator, playerFrom, world, type.getID(), cost.getAmount(), oldBalance, balance, time);
      if(!transactionHistory.containsKey(initiator)) {
        TransactionHistory history = new TransactionHistory();
        history.add(r);
        transactionHistory.put(initiator, history);
      }
      transactionHistory.get(initiator).add(r);
    }
  }

  public boolean hasHistory(String initiator) {
    return transactionHistory.containsKey(initiator);
  }

  public TransactionHistory getHistory(String initiator) {
    MISCUtils.debug(initiator);
    return transactionHistory.get(initiator);
  }
}
