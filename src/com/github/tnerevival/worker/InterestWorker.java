package com.github.tnerevival.worker;

import java.util.Iterator;
import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.scheduler.BukkitRunnable;

import com.github.tnerevival.TNE;
import com.github.tnerevival.account.Account;
import com.github.tnerevival.utils.BankUtils;

public class InterestWorker extends BukkitRunnable {
	
	private TNE plugin;
	
	public InterestWorker(TNE plugin) {
		this.plugin = plugin;
	}

	@Override
	public void run() {
		doInterest();
	}
	
	private void doInterest() {
		Iterator<Entry<UUID, Account>> it = plugin.manager.accounts.entrySet().iterator();
		
		while(it.hasNext()) {
			Entry<UUID, Account> entry = it.next();
			
			BankUtils.applyInterest(entry.getValue().getUid());
		}
	}
}