package net.tnemc.core.common.data;

import com.github.tnerevival.core.DataManager;
import com.github.tnerevival.core.db.DataProvider;
import net.tnemc.core.TNE;
import net.tnemc.core.common.WorldVariant;
import net.tnemc.core.common.account.TNEAccount;
import net.tnemc.core.common.account.WorldFinder;
import net.tnemc.core.common.api.IDFinder;
import net.tnemc.core.common.transaction.TNETransaction;
import net.tnemc.core.common.utils.TopBalance;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
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
  public abstract String loadUsername(String identifier) throws SQLException;
  public abstract UUID loadID(String username) throws SQLException;
  public abstract Map<String, UUID> loadEconomyIDS() throws SQLException;
  public abstract Map<String, UUID> loadEconomyAccountIDS() throws SQLException;
  public void saveIDS(Map<String, UUID> ids) throws SQLException {

  }

  public int accountCount(String username) {
    return 0;
  }
  public abstract void createTables(List<String> tables) throws SQLException;
  public abstract void saveID(String username, UUID id) throws SQLException;
  public abstract void removeID(String username) throws SQLException;
  public abstract void removeID(UUID id) throws SQLException;
  public abstract Collection<TNEAccount> loadAccounts() throws SQLException;
  public abstract TNEAccount loadAccount(UUID id) throws SQLException;
  public void saveAccounts(List<TNEAccount> accounts) throws SQLException {

  }
  public abstract void saveAccount(TNEAccount account) throws SQLException;
  public abstract void deleteAccount(UUID id) throws SQLException;

  public abstract Map<String, BigDecimal> loadAllBalances(UUID id) throws SQLException;
  public abstract BigDecimal loadBalance(UUID id, String world, String currency) throws SQLException;
  public abstract void saveBalance(UUID id, String world, String currency, BigDecimal balance) throws SQLException;
  public abstract void setAllBalance(String world, BigDecimal balance) throws SQLException;
  public abstract void deleteBalance(UUID id, String world, String currency) throws SQLException;

  public abstract TNETransaction loadTransaction(UUID id) throws SQLException;
  public abstract Collection<TNETransaction> loadTransactions() throws SQLException;
  public abstract void saveTransaction(TNETransaction transaction) throws SQLException;
  public abstract void deleteTransaction(UUID id) throws SQLException;
  public abstract String nullAccounts() throws SQLException;
  //balances
  public abstract int topPos(String identifier, String world, String currency) throws SQLException;
  public abstract int balanceCount(String world, String currency, int limit) throws SQLException;
  public abstract LinkedList<TopBalance> topBalances(String world, String currency, int limit, int page) throws SQLException;
  //transactions
  public abstract int transactionCount(UUID recipient, String world, String type, String time, int limit) throws SQLException;
  public abstract LinkedHashMap<UUID, TNETransaction> transactionHistory(UUID recipient, String world, String type, String time, int limit, int page) throws SQLException;

  public void preLoad(Double version) throws SQLException {

  }

  public void preSave(Double version) throws SQLException {

  }

  @Override
  public Double version() throws SQLException {
    return null;
  }

  @Override
  public void initialize() throws SQLException {

  }

  @Override
  public Boolean first() throws SQLException {
    return false;
  }

  @Override
  public void update(Double aDouble) throws SQLException {

  }

  @Override
  public void load(Double version) throws SQLException {
    preLoad(version);
  }

  protected String generateLike(String column, List<String> like, boolean not, boolean andStart) {
    StringBuilder builder = new StringBuilder();

    int i = 0;
    for(String l : like) {

      if(andStart || i > 0) builder.append(" AND ");
      builder.append(column + " ");
      if(not) builder.append("NOT ");
      builder.append("LIKE '" + l + "'");
      i++;
    }
    builder.append(" ");
    return builder.toString();
  }

  @Override
  public void save(Double version) throws SQLException {
    TNE.debug("TNEDataProvider.save");
    preSave(version);
    long start = System.nanoTime();

    if(supportUpdate()) {
      List<TNEAccount> accounts = new ArrayList<>();
      Map<String, UUID> ids = new HashMap<>();

      TNE.instance().getServer().getOnlinePlayers().forEach((player)->{
        UUID id = IDFinder.getID(player.getName());
        TNEAccount account = TNE.manager().getAccount(id);
        account.saveItemCurrency(WorldFinder.getWorld(player.getName(), WorldVariant.BALANCE), false, player.getInventory());
        accounts.add(account);
        ids.put(account.displayName(), account.identifier());
      });
      saveIDS(ids);
      saveAccounts(accounts);
    }
    long end = System.nanoTime();
    TNE.debug("Saving data finished in milis: " + ((end - start) / 1e6));
  }
}