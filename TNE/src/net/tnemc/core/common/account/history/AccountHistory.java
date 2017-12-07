package net.tnemc.core.common.account.history;

import net.tnemc.core.TNE;
import net.tnemc.core.common.transaction.TNETransaction;

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