package com.github.tnerevival.core;

import com.github.tnerevival.TNE;
import com.github.tnerevival.account.Account;
import com.github.tnerevival.core.shops.Shop;
import com.github.tnerevival.core.signs.TNESign;
import com.github.tnerevival.serializable.SerializableLocation;
import com.github.tnerevival.utils.AccountUtils;
import com.github.tnerevival.utils.MISCUtils;

import java.util.*;

public class EconomyManager {
	
	/**
	 * A HashMap holding all accounts for the economy.
	 * Format: Player UUID, Account Class Instance
	 */
	public Map<UUID, Account> accounts = new HashMap<>();

	public Map<String, UUID> ecoIDs = new HashMap<>();
	
	public  Map<String, Shop> shops = new HashMap<>();
	
	public List<UUID> confirmed = new ArrayList<>();

	public Map<SerializableLocation, TNESign> signs = new HashMap<>();
	
	public void purge() {
	  purge(TNE.instance.defaultWorld);
	}
	
	public void purge(String world) {
	  Iterator<Account> it = accounts.values().iterator();
	  while(it.hasNext()) {
	    Account acc = it.next();
	    
	    if(acc.getBalance(world).equals(AccountUtils.getInitialBalance(world))) {
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
        if(!acc.getBalance(s).equals(AccountUtils.getInitialBalance(s))) {
          remove = false;
        }
      }
      
      if(remove) {
        it.remove();
        ecoIDs.remove(MISCUtils.getPlayer(acc.getUid()).getDisplayName());
      }
    }
	}
}