package net.tnemc.core.common.transaction;

import net.tnemc.core.TNE;
import net.tnemc.core.common.account.TNEAccount;
import net.tnemc.core.economy.transaction.charge.TransactionCharge;
import net.tnemc.core.economy.transaction.result.TransactionResult;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
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

    if(handler.getTransactionType().equalsIgnoreCase("pay")) {
      if(handler.getInitiator() != null) {
        for (TNEAccount affect : affected) {
          if(affect.identifier().toString().equalsIgnoreCase(handler.getInitiator().identifier().toString())) {
            messages.put(handler.getInitiator().identifier(), TNE.transactionManager().getResult("failed").initiatorMessage());
            return;
          }
        }
      }
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