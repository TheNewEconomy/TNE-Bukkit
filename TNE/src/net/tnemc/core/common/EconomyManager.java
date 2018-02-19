package net.tnemc.core.common;

import com.github.tnerevival.core.collection.EventMap;
import com.github.tnerevival.user.IDFinder;
import net.tnemc.core.TNE;
import net.tnemc.core.common.account.TNEAccount;
import net.tnemc.core.common.utils.TopBalance;
import net.tnemc.core.economy.Account;
import net.tnemc.core.event.account.TNEAccountCreationEvent;
import net.tnemc.core.listeners.collections.AccountListener;
import org.bukkit.Bukkit;

import java.math.BigDecimal;
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
 * Created by creatorfromhell on 06/30/2017.
 */
public class EconomyManager {


  /**
   * Used to hold all accounts.
   * Dictionary is a {@link Map} collection that contains {@link UUID Player UUID} as
   * the key and {@link TNEAccount account} as the value.
   */
  private EventMap<UUID, TNEAccount> accounts = new EventMap<>();

  /**
   * The {@link CurrencyManager currency manager}, which manages all loaded currencies, and their respective tiers.
   */
  private CurrencyManager currencyManager;

  /**
   * The {@link TransactionManager transaction manager}, which manages all transactions performed on the server.
   */
  private TransactionManager transactionManager;

  public EconomyManager() {
    this.accounts.setListener(new AccountListener());
    TNE.instance().registerEventMap(accounts);
    currencyManager = new CurrencyManager();
    transactionManager = new TransactionManager();
  }

  public CurrencyManager currencyManager() {
    return currencyManager;
  }

  public TransactionManager transactionManager() {
    return transactionManager;
  }

  public boolean exists(UUID id) {
    return accounts.containsKey(id);
  }

  public void addAccount(TNEAccount account) {
    accounts.put(account.identifier(), account);
  }

  public TNEAccount getAccount(UUID id) {
    if(!exists(id)) {
      if(!createAccount(id, IDFinder.getUsername(id.toString()))) {
        return null;
      }
    }
    return accounts.get(id);
  }

  public void removeAccount(UUID id) {
    accounts.remove(id, false);
  }

  public boolean deleteAccount(UUID id) {
    if(exists(id)) {
      accounts.remove(id);
      return true;
    }
    return false;
  }

  public boolean createAccount(UUID id, String displayName) {
    TNE.debug("=====START EconomyManager.createAccount =====");
    TNE.debug("UUID: " + id.toString());
    TNEAccount account = new TNEAccount(id, displayName);
    account.setPlayerAccount(!TNE.instance().special.contains(id));

    TNEAccountCreationEvent event = new TNEAccountCreationEvent(id, account);
    Bukkit.getServer().getPluginManager().callEvent(event);
    if(event.isCancelled()) {
      return false;
    }
    accounts.put(account.identifier(), event.getAccount());
    TNE.debug("=====END EconomyManager.createAccount =====");
    return true;
  }

  public EventMap<UUID, TNEAccount> getAccounts() {
    return accounts;
  }

  public void purge(String world) {
    Iterator<TNEAccount> it = accounts.values().iterator();

    while(it.hasNext()) {
      TNEAccount account = it.next();
      boolean remove = true;

      for(Map.Entry<String, BigDecimal> balance : account.getWorldHoldings(world).getHoldings().entrySet()) {
        if(!balance.getValue().equals(TNE.manager().currencyManager().get(world, balance.getKey()).defaultBalance())) {
          remove = false;
        }
      }

      if(remove) {
        TNE.manager().getAccounts().remove(account.identifier(), false);
        TNE.uuidManager().remove(account.displayName());
        TNE.saveManager().getTNEManager().getTNEProvider().deleteAccount(account.identifier());
        it.remove();
      }
    }
  }

  public LinkedHashSet<Object> parseTop(String currency, String world, Integer limit) {
    LinkedHashSet<Object> finalBalances = new LinkedHashSet<>();
    TreeMap<Double, List<String>> ordered = new TreeMap<>(Collections.reverseOrder());

    for(TNEAccount account : accounts.values()) {
      Double balance = account.addAll(world).doubleValue();
      List<String> usernames = (ordered.containsKey(balance))? ordered.get(balance) : new ArrayList<>();
      usernames.add(account.displayName());
      ordered.put(balance, usernames);
    }

    parse:
    for(Map.Entry<Double, List<String>> entry : ordered.entrySet()) {
      if(finalBalances.size() >= limit) break;
      for(String username : entry.getValue()) {
        if(finalBalances.size() >= limit) break parse;
        finalBalances.add(new TopBalance(username, entry.getKey()));
      }
    }
    return finalBalances;
  }

  public Collection<TNEAccount> parsePlayerArgument(String argument) {
    TNE.debug("EconomyManager.parsePlayerArgument: " + argument);
    argument = argument.trim();
    if(argument.equalsIgnoreCase("all")) return getAccounts().values();

    List<TNEAccount> accounts = new ArrayList<>();
    if(argument.contains(",")) {
      String[] names = argument.split(",");

      for(String name : names) {
        UUID id = IDFinder.getID(name.trim());

        if(exists(id)) accounts.add(getAccount(id));
      }
      return accounts;
    }
    UUID id = IDFinder.getID(argument);
    Account account = TNE.instance().api().getOrCreate(id);
    if(account != null) {
      accounts.add(getAccount(id));
      return accounts;
    }
    return null;
  }
}