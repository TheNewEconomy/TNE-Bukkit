package com.github.tnerevival.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.github.tnerevival.TNE;
import com.github.tnerevival.account.Access;
import com.github.tnerevival.account.Account;
import com.github.tnerevival.core.inventory.View;
import com.github.tnerevival.core.shops.Shop;
import com.github.tnerevival.utils.AccountUtils;
import com.github.tnerevival.utils.MISCUtils;

public class EconomyManager {
	
	/**
	 * A HashMap holding all accounts for the economy.
	 * Format: Player UUID, Account Class Instance
	 */
	public Map<UUID, Account> accounts = new HashMap<UUID, Account>();
	
	public Map<UUID, View> viewers = new HashMap<UUID, View>();
	
	public Map<UUID, Access> accessing = new HashMap<UUID, Access>();
	
	/**
	 * A Map, which holds the economy UUIDs for each player that are used when UUID support is turned off.
	 */
	public Map<String, UUID> ecoIDs = new HashMap<String, UUID>();
	
	public  Map<String, Shop> shops = new HashMap<String, Shop>();
	
	public List<UUID> confirmed = new ArrayList<UUID>();
	
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