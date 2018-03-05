package net.tnemc.core.common.data;

import com.github.tnerevival.core.DataManager;
import com.github.tnerevival.core.db.DataProvider;
import com.github.tnerevival.user.IDFinder;
import net.tnemc.core.TNE;
import net.tnemc.core.common.WorldVariant;
import net.tnemc.core.common.account.TNEAccount;
import net.tnemc.core.common.account.WorldFinder;
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
 * Created by Daniel on 8/30/2017.
 */
public abstract class TNEDataProvider extends DataProvider {

  public TNEDataProvider(DataManager manager) {
    super(manager);
  }

  public abstract Boolean backupData();
  public abstract UUID loadID(String username);
  public abstract Map<String, UUID> loadEconomyIDS();
  public void saveIDS(Map<String, UUID> ids) {

  }
  public abstract void saveID(String username, UUID id);
  public abstract void removeID(String username);
  public abstract void removeID(UUID id);
  public abstract Collection<TNEAccount> loadAccounts();
  public abstract TNEAccount loadAccount(UUID id);
  public void saveAccounts(List<TNEAccount> accounts) {

  }
  public abstract void saveAccount(TNEAccount account);
  public abstract void deleteAccount(UUID id);
  public abstract TNETransaction loadTransaction(UUID id);
  public abstract Collection<TNETransaction> loadTransactions();
  public abstract void saveTransaction(TNETransaction transaction);
  public abstract void deleteTransaction(UUID id);

  public void preLoad(Double version) {

  }

  public void preSave(Double version) {

  }


  @Override
  public void load(Double version) {
    preLoad(version);

    if(!supportUpdate()) {
      TNE.debug("Inside !supportUpdate() || manager.isCacheData()");
      Collection<TNEAccount> accounts = loadAccounts();
      TNE.debug("loadAccounts() size: " + accounts.size());

      accounts.forEach((acc)-> {
        TNE.manager().addAccount(acc);
        TNE.debug("Loading account with ID of: " + acc.identifier() + " and username of: " + acc.displayName());
      });

      Map<String, UUID> ids = loadEconomyIDS();
      TNE.debug("loadEconomyIDS() size: " + ids.size());
      ids.forEach((key, value)->{
        TNE.debug("Loading id: " + value.toString() + " for username: " + key);
        TNE.uuidManager().addUUID(key, value);
      });

      Collection<TNETransaction> transactions = loadTransactions();
      transactions.forEach((value)->TNE.transactionManager().add(value));
    }
  }

  @Override
  public void save(Double version) {
    TNE.debug("TNEDataProvider.save");
    preSave(version);
    long start = System.nanoTime();

    if(supportUpdate()) {
      List<TNEAccount> accounts = new ArrayList<>();
      Map<String, UUID> ids = new HashMap<>();

      TNE.instance().getServer().getOnlinePlayers().forEach((player)->{
        UUID id = IDFinder.getID(player.getName());
        TNEAccount account = TNE.manager().getAccount(id);
        account.saveItemCurrency(WorldFinder.getWorld(id, WorldVariant.BALANCE), false);
        accounts.add(account);
        ids.put(account.displayName(), account.identifier());
      });
      saveIDS(ids);
      saveAccounts(accounts);
    } else {
      TNE.instance().getServer().getOnlinePlayers().forEach((player)->{
        UUID id = IDFinder.getID(player);
        TNE.manager().getAccount(id).saveItemCurrency(WorldFinder.getWorld(id, WorldVariant.BALANCE));
      });

      TNE.debug("OffLine Length: " + TNE.uuidManager().getUuids().size());
      TNE.uuidManager().getUuids().forEach((username, id)->{
        TNE.debug("Saving Offline id: " + username + ", " + id.toString());
        saveID(username, id);
      });
      TNE.transactionManager().getTransactions().forEach((id, transaction)->saveTransaction(transaction));
    }
    long end = System.nanoTime();
    TNE.debug("Saving data finished in milis: " + ((end - start) / 1e6));
  }
}