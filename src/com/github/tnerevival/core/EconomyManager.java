package com.github.tnerevival.core;

import com.github.tnerevival.TNE;
import com.github.tnerevival.account.Account;
import com.github.tnerevival.core.shops.Shop;
import com.github.tnerevival.core.signs.TNESign;
import com.github.tnerevival.serializable.SerializableLocation;
import com.github.tnerevival.utils.AccountUtils;
import com.github.tnerevival.utils.MISCUtils;
import org.bukkit.entity.Player;

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
	
	public void purge(String world) {
	  Iterator<Account> it = accounts.values().iterator();
	  while(it.hasNext()) {
	    Account acc = it.next();
	    
	    if(acc.getBalances().containsKey(world) && acc.getBalance(world).equals(AccountUtils.getInitialBalance(world))) {
        deleteAccount(acc.getUid());
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
        deleteAccount(acc.getUid());
        it.remove();
        ecoIDs.remove(MISCUtils.getPlayer(acc.getUid()).getDisplayName());
      }
    }
	}

	public void deleteAccount(UUID id) {
	  TNE.instance.saveManager.deleteAccount(id);
  }

  public void deleteShop(String name, String world) {
    shops.remove(name + ":" + world);
    TNE.instance.saveManager.deleteShop(name, world);
  }

	public boolean enabled(UUID id, String world) {
	  return TNE.instance.api.getBoolean("Core.Pins.Enabled", world, id);
  }

	public boolean confirmed(UUID id, String world) {
	  Boolean enabled = TNE.instance.api.getBoolean("Core.Pins.Enabled", world, id);
    Boolean force = TNE.instance.api.getBoolean("Core.Pins.Force", world, id);

    if(!enabled) {
    	Player p = MISCUtils.getPlayer(id);
      new Message("Messages.Money.NoPins").translate(MISCUtils.getWorld(p), p);
      return true;
    }

    if(!force) return true;
    return confirmed.contains(id);
  }
}