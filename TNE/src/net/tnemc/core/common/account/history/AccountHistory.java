package net.tnemc.core.common.account.history;

import net.tnemc.core.TNE;
import net.tnemc.core.common.transaction.TNETransaction;

import java.util.*;

/**
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 07/05/2017.
 */
public class AccountHistory {

  /**
   * Collection is a {@link ArrayList} that contains {@link UUID transaction id}.
   */
  private List<UUID> away = new ArrayList<>();

  /**
   * Dictionary is a {@link Map} collection that contains {@link String World Name} as
   * the key and {@link WorldHistory World TNETransaction History} as the value.
   */
  private Map<String, WorldHistory> worldHistory = new HashMap<>();

  /**
   * The {@link UUID} of the player this history is about.
   */
  private UUID id;

  public void logAway(UUID id) {
    away.add(id);
  }

  public void clearAway() {
    away.clear();
  }

  public List<UUID> getAway() {
    return away;
  }

  public void populateAway(long time) {
    worldHistory.forEach((world, history)-> {
      history.getTransactions().forEach((id)->{
        final TNETransaction trans = TNE.transactionManager().get(id);
        if(trans.time() > time) logAway(id);
      });
    });
  }

  public void log(TNETransaction transaction) {
    WorldHistory history = (worldHistory.containsKey(transaction.getWorld()))? worldHistory.get(transaction.getWorld())
                                                                             : new WorldHistory(transaction.getWorld());
    history.addTransaction(transaction);
    worldHistory.put(history.getWorld(), history);
  }

  public List<UUID> getHistroy(String world) {
    List<UUID> transactions = new ArrayList<>();

    if(!world.equalsIgnoreCase("all")) {
      if(worldHistory.containsKey(world)) return worldHistory.get(world).getTransactions();
      return transactions;
    }

    worldHistory.forEach((key, value)->{
      if(key.equalsIgnoreCase(world)) {
        transactions.addAll(value.getTransactions());
      }
    });
    return transactions;
  }
}