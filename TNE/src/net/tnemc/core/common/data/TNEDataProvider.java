package net.tnemc.core.common.data;

import com.github.tnerevival.core.DataManager;
import com.github.tnerevival.core.db.DataProvider;
import net.tnemc.core.TNE;
import net.tnemc.core.common.WorldVariant;
import net.tnemc.core.common.account.TNEAccount;
import net.tnemc.core.common.account.WorldFinder;
import net.tnemc.core.common.transaction.TNETransaction;
import net.tnemc.core.common.utils.MISCUtils;

import java.util.Collection;
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
 * Created by Daniel on 8/30/2017.
 */
public abstract class TNEDataProvider extends DataProvider {

  public TNEDataProvider(DataManager manager) {
    super(manager);
  }

  public abstract Boolean backupData();
  public abstract UUID loadID(String username);
  public abstract Map<String, UUID> loadEconomyIDS();
  public abstract void saveID(String username, UUID id);
  public abstract void removeID(String username);
  public abstract void removeID(UUID id);
  public abstract Collection<TNEAccount> loadAccounts();
  public abstract TNEAccount loadAccount(UUID id);
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
    TNE.manager().getAccounts().forEach((id, account)->{
      if(MISCUtils.isOnline(id)) {
        String world = WorldFinder.getWorld(id, WorldVariant.BALANCE);
        account.saveItemCurrency(world);
      }
      saveAccount(account);
    });
    TNE.debug("OffLine Length: " + TNE.uuidManager().getUuids().size());
    TNE.uuidManager().getUuids().forEach((username, id)->{
      TNE.debug("Saving Offline id: " + username + ", " + id.toString());
      saveID(username, id);
    });
    TNE.transactionManager().getTransactions().forEach((id, transaction)->saveTransaction(transaction));
  }
}