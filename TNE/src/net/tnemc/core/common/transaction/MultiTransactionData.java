package net.tnemc.core.common.transaction;

import net.tnemc.core.TNE;
import net.tnemc.core.common.account.TNEAccount;
import net.tnemc.core.economy.transaction.charge.TransactionCharge;
import net.tnemc.core.economy.transaction.result.TransactionResult;

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
 * Created by Daniel on 11/8/2017.
 */
public class MultiTransactionData {

  private Collection<TNEAccount> affected = new ArrayList<>();
  private Collection<String> succeed = new ArrayList<>();
  private Map<UUID, String> messages = new HashMap<>();

  private boolean proceed = false;

  public MultiTransactionData(Collection<TNEAccount> affected) {
    this.affected = affected;
  }

  public void handle(MultiTransactionHandler handler) {
    if(!TNE.instance().api().hasHoldings(handler.getInitiator().identifier().toString(),
       handler.initiatorCost(), handler.getCurrency(), handler.getWorld())) {
      messages.put(handler.getInitiator().identifier(), TNE.transactionManager().getResult("failed").initiatorMessage());
      return;
    }

    for(TNEAccount account : affected) {
      handleTransaction(account, handler);
    }
  }

  private void handleTransaction(TNEAccount account, MultiTransactionHandler handler) {
    TNETransaction transaction = new TNETransaction(handler.getInitiator(), account,
                                                    handler.getWorld(),
                                                    TNE.transactionManager().getType(handler.getTransactionType()));

    transaction.setRecipientCharge(new TransactionCharge(handler.getWorld(),
                                                         handler.getCurrency(),
                                                         handler.getAmount(),
                                                         handler.getChargeType()));

    if(handler.getInitiator() != null) {
      transaction.setInitiatorCharge(new TransactionCharge(handler.getWorld(),
          handler.getCurrency(),
          handler.initiatorCost(),
          handler.getInitiatorType()));
    }

    TransactionResult result = TNE.transactionManager().perform(transaction);
    if(result.proceed()) {
      proceed = true;
      succeed.add(account.displayName());
      messages.put(account.identifier(), result.recipientMessage());
      messages.put(handler.getInitiator().identifier(), result.initiatorMessage());
    }
  }

  public Collection<TNEAccount> getAffected() {
    return affected;
  }

  public void setAffected(Collection<TNEAccount> affected) {
    this.affected = affected;
  }

  public Collection<String> getSucceed() {
    return succeed;
  }

  public void setSucceed(Collection<String> succeed) {
    this.succeed = succeed;
  }

  public boolean isProceed() {
    return proceed;
  }

  public void setProceed(boolean proceed) {
    this.proceed = proceed;
  }

  public void addMessage(UUID id, String message) {
    messages.put(id, message);
  }

  public Map<UUID, String> getMessages() {
    return messages;
  }

  public void setMessages(Map<UUID, String> messages) {
    this.messages = messages;
  }
}