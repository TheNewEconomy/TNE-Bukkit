package net.tnemc.core.common;

import com.github.tnerevival.core.collection.EventMap;
import net.tnemc.core.TNE;
import net.tnemc.core.common.account.TNEAccount;
import net.tnemc.core.common.api.IDFinder;
import net.tnemc.core.economy.Account;
import net.tnemc.core.event.account.TNEAccountCreationEvent;
import net.tnemc.core.listeners.collections.AccountListener;
import org.bukkit.Bukkit;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
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
    TNE.instance().getUuidManager().addUUID(account.displayName(), account.identifier());
  }

  public TNEAccount getAccount(UUID id) {
    if(!exists(id) && !createAccount(id, IDFinder.getUsername(id.toString()))) {
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
    TNE.instance().getUuidManager().addUUID(account.displayName(), account.identifier());
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