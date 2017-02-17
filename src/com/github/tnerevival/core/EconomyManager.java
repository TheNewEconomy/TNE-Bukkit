package com.github.tnerevival.core;

import com.github.tnerevival.TNE;
import com.github.tnerevival.account.Account;
import com.github.tnerevival.account.IDFinder;
import com.github.tnerevival.core.collection.EventMap;
import com.github.tnerevival.core.shops.Shop;
import com.github.tnerevival.core.signs.TNESign;
import com.github.tnerevival.listeners.collections.AccountsListener;
import com.github.tnerevival.listeners.collections.IDSListener;
import com.github.tnerevival.listeners.collections.ShopsListener;
import com.github.tnerevival.listeners.collections.SignsListener;
import com.github.tnerevival.serializable.SerializableLocation;
import com.github.tnerevival.utils.AccountUtils;
import com.github.tnerevival.utils.MISCUtils;
import org.bukkit.entity.Player;

import java.math.BigDecimal;
import java.util.*;

public class EconomyManager {

  /**
   * A HashMap holding all accounts for the economy.
   * Format: Player UUID, Account Class Instance
   */
  public EventMap<UUID, Account> accounts = new EventMap<>();

  public EventMap<String, UUID> ecoIDs = new EventMap<>();

  public  EventMap<String, Shop> shops = new EventMap<>();

  public List<UUID> confirmed = new ArrayList<>();

  public EventMap<SerializableLocation, TNESign> signs = new EventMap<>();

  public AuctionManager auctionManager = new AuctionManager();
  public CurrencyManager currencyManager = new CurrencyManager();
  public TransactionManager transactions = new TransactionManager();

  public EconomyManager() {
    accounts.setListener(new AccountsListener());
    ecoIDs.setListener(new IDSListener());
    shops.setListener(new ShopsListener());
    signs.setListener(new SignsListener());
  }

  public List<Map.Entry<String, BigDecimal>> parseTop(String currency, String world, Boolean bank, Integer limit) {
    List<Map.Entry<String, BigDecimal>> top = new ArrayList<>();

    return top;
  }

  public void purge(String world) {
    Iterator<Account> it = accounts.values().iterator();
    while(it.hasNext()) {
      Account acc = it.next();

      List<com.github.tnerevival.core.currency.Currency> worldCurrencies = TNE.instance.manager.currencyManager.getWorldCurrencies(world);
      Boolean remove = true;
      for(com.github.tnerevival.core.currency.Currency c : worldCurrencies) {
        if(acc.getBalances().containsKey(world + ":" + c.getName()) && !acc.getBalance(world, c.getName()).equals(AccountUtils.getInitialBalance(world, c.getName()))) {
          remove = false;
        }
      }
      if(remove) {
        TNE.instance.saveManager.versionInstance.deleteAccount(acc.getUid());
        TNE.instance.saveManager.versionInstance.removeID(acc.getUid());
        it.remove();
        ecoIDs.remove(MISCUtils.getPlayer(acc.getUid()).getDisplayName());
      }
    }
  }

  public void purgeAll() {
    Iterator<Account> it = accounts.values().iterator();
    while(it.hasNext()) {
      Account acc = it.next();
      boolean remove = true;
      for(String s : acc.getBalances().keySet()) {
        String[] split = s.split(":");
        if(!acc.getBalance(split[0], split[1]).equals(AccountUtils.getInitialBalance(split[0], split[1]))) {
          remove = false;
        }
      }

      if(remove) {
        TNE.instance.saveManager.versionInstance.deleteAccount(acc.getUid());
        TNE.instance.saveManager.versionInstance.removeID(acc.getUid());
        it.remove();
        ecoIDs.remove(MISCUtils.getPlayer(acc.getUid()).getDisplayName());
      }
    }
  }

  public boolean enabled(UUID id, String world) {
    return TNE.instance.api.getBoolean("Core.Pins.Enabled", world, id);
  }

  public boolean confirmed(UUID id, String world) {
    Boolean enabled = TNE.instance.api.getBoolean("Core.Pins.Enabled", world, id);
    Boolean force = TNE.instance.api.getBoolean("Core.Pins.Force", world, id);

    if(!enabled) {
      Player p = MISCUtils.getPlayer(id);
      new Message("Messages.Money.NoPins").translate(IDFinder.getActualWorld(p), p);
      return true;
    }
    return !force || confirmed.contains(id);
  }
}