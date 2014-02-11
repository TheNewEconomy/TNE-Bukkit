package com.github.tnerevival.run;

import java.util.Iterator;
import java.util.Map.Entry;

import org.bukkit.scheduler.BukkitRunnable;

import com.github.tnerevival.TNE;
import com.github.tnerevival.account.Account;
import com.github.tnerevival.utils.BankUtils;

public class InterestRate extends BukkitRunnable {
	
	private TNE plugin;
	
	public InterestRate(TNE plugin) {
		this.plugin = plugin;
	}

	@Override
	public void run() {
		doInterest();
	}
	
	public void startTask(Long time) {
		this.runTaskTimer(plugin, 0L, time);
	}
	
	public void cancelTask() {
		this.cancel();
	}
	
	private void doInterest() {
		Iterator<Entry<String, Account>> it = TNE.instance.manager.accounts.entrySet().iterator();
		
		while(it.hasNext()) {
			Entry<String, Account> entry = it.next();
			
			BankUtils.applyInterest(entry.getValue().getOwner());
		}
	}
}