package net.tnemc.core.common.data;

import com.github.tnerevival.core.DataManager;
import com.github.tnerevival.core.db.DataProvider;
import com.github.tnerevival.user.IDFinder;
import net.tnemc.core.TNE;
import net.tnemc.core.common.WorldVariant;
import net.tnemc.core.common.account.TNEAccount;
import net.tnemc.core.common.account.WorldFinder;
import net.tnemc.core.common.transaction.TNETransaction;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
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

  public abstract void createTables(List<String> tables);
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
  public abstract String nullAccounts();
  public abstract int balanceCount(String world, String currency, int limit);
  public abstract Map<UUID, BigDecimal> topBalances(String world, String currency, int limit, int page);

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