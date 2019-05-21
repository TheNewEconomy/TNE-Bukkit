package net.tnemc.core.common;

import com.github.tnerevival.core.collection.EventMap;
import net.tnemc.core.TNE;
import net.tnemc.core.common.account.TNEAccount;
import net.tnemc.core.common.transaction.TNETransaction;
import net.tnemc.core.common.transaction.result.TransactionResultConversion;
import net.tnemc.core.common.transaction.result.TransactionResultFailed;
import net.tnemc.core.common.transaction.result.TransactionResultGave;
import net.tnemc.core.common.transaction.result.TransactionResultHoldings;
import net.tnemc.core.common.transaction.result.TransactionResultInsufficient;
import net.tnemc.core.common.transaction.result.TransactionResultLost;
import net.tnemc.core.common.transaction.result.TransactionResultNoteClaimed;
import net.tnemc.core.common.transaction.result.TransactionResultNoted;
import net.tnemc.core.common.transaction.result.TransactionResultPaid;
import net.tnemc.core.common.transaction.result.TransactionResultSelfPay;
import net.tnemc.core.common.transaction.result.TransactionResultSet;
import net.tnemc.core.common.transaction.result.TransactionResultWorldChange;
import net.tnemc.core.common.transaction.type.TransactionConversion;
import net.tnemc.core.common.transaction.type.TransactionGive;
import net.tnemc.core.common.transaction.type.TransactionHasFunds;
import net.tnemc.core.common.transaction.type.TransactionInquiry;
import net.tnemc.core.common.transaction.type.TransactionNote;
import net.tnemc.core.common.transaction.type.TransactionNoteClaim;
import net.tnemc.core.common.transaction.type.TransactionPay;
import net.tnemc.core.common.transaction.type.TransactionSet;
import net.tnemc.core.common.transaction.type.TransactionTake;
import net.tnemc.core.common.transaction.type.TransactionWorldChange;
import net.tnemc.core.economy.transaction.result.TransactionResult;
import net.tnemc.core.economy.transaction.type.TransactionType;
import net.tnemc.core.event.transaction.TNEPreTransaction;
import net.tnemc.core.listeners.collections.TransactionListener;
import org.bukkit.Bukkit;

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
 * Created by creatorfromhell on 06/30/2017.
 */
public class TransactionManager {

  /**
   * Dictionary is a {@link Map} collection that contains {@link UUID TNETransaction Identifier} as
   * the key and {@link TNETransaction TNETransaction} as the value.
   */
  private EventMap<UUID, TNETransaction> transactions = new EventMap<>();
  private Map<String, TransactionResult> results = new HashMap<>();
  private Map<String, TransactionType> types = new HashMap<>();

  public TransactionManager() {
    this.transactions.setListener(new TransactionListener());
    loadResults();
    loadTypes();
  }

  public TNETransaction get(UUID id) {
    return transactions.get(id);
  }

  private void loadResults() {
    results.put("conversion", new TransactionResultConversion());
    results.put("failed", new TransactionResultFailed());
    results.put("gave", new TransactionResultGave());
    results.put("holdings", new TransactionResultHoldings());
    results.put("insufficient", new TransactionResultInsufficient());
    results.put("lost", new TransactionResultLost());
    results.put("noteclaimed", new TransactionResultNoteClaimed());
    results.put("noted", new TransactionResultNoted());
    results.put("paid", new TransactionResultPaid());
    results.put("selfpay", new TransactionResultSelfPay());
    results.put("set", new TransactionResultSet());
    results.put("worldchange", new TransactionResultWorldChange());

    TNE.loader().getModules().forEach((key, value)->{
      value.getModule().registerResults().forEach((k, v)->{
        results.put(k, v);
      });
    });
  }

  public void loadTypes() {
    types.put("conversion", new TransactionConversion());
    types.put("give", new TransactionGive());
    types.put("hasfunds", new TransactionHasFunds());
    types.put("inquiry", new TransactionInquiry());
    types.put("note", new TransactionNote());
    types.put("noteclaim", new TransactionNoteClaim());
    types.put("pay", new TransactionPay());
    types.put("set", new TransactionSet());
    types.put("take", new TransactionTake());
    types.put("worldchange", new TransactionWorldChange());

    TNE.loader().getModules().forEach((key, value)->{
      value.getModule().registerTypes().forEach((k, v)->{
        types.put(k, v);
      });
    });
  }

  public void addResult(TransactionResult result) {
    results.put(result.name(), result);
  }

  public TransactionResult getResult(String name) {
    return results.get(name);
  }

  public TransactionType getType(String name) {
    return types.get(name);
  }

  public void add(TNETransaction transaction) {
    if(!transactions.containsKey(transaction.transactionID())) {
      log(transaction);
      return;
    }
    transactions.put(transaction.transactionID(), transaction);
  }

  public TransactionResult perform(TNETransaction transaction) {
    TNEPreTransaction event = new TNEPreTransaction(transaction, !Bukkit.getServer().isPrimaryThread());
    Bukkit.getServer().getPluginManager().callEvent(event);
    if(event.isCancelled()) {
      return new TransactionResultFailed();
    }
    TransactionResult result = transaction.perform();
    log(transaction);
    return result;
  }

  void log(TNETransaction transaction) {
    transactions.put(transaction.transactionID(), transaction);
    if(transaction.initiator() != null) {
      TNEAccount.getAccount(transaction.initiator()).log(transaction);
    }

    if(transaction.recipient() != null) {
      TNEAccount.getAccount(transaction.recipient()).log(transaction);
    }
  }

  public EventMap<UUID, TNETransaction> getTransactions() {
    return transactions;
  }

  public Map<UUID, TNETransaction> getTransactions(UUID id) {
    Map<UUID, TNETransaction> accountTransactions = new HashMap<>();

    for(UUID transactionID : TNE.manager().getAccount(id).getHistory().getHistroy("all")) {
      if(transactions.containsKey(transactionID)) {
        accountTransactions.put(transactionID, transactions.get(transactionID));
      }
    }
    return accountTransactions;
  }

  public UUID generateTransactionID() {
    UUID id = UUID.randomUUID();

    while(transactions.containsKey(id)) {
      id = UUID.randomUUID();
    }
    return id;
  }

  public void addType(TransactionType type) {
    types.put(type.name(), type);
  }

  public Collection<TransactionType> getTypes() {
    return types.values();
  }

  public boolean isValid(UUID id) {
    return transactions.containsKey(id);
  }

  public boolean isVoided(UUID id) {
    return transactions.get(id).voided();
  }

  public boolean voidTransaction(UUID id) {
    return transactions.get(id).voidTransaction();
  }
}