package com.github.tnerevival.core;

import java.util.Iterator;
import java.util.Map.Entry;

import com.github.tnerevival.TNE;
import com.github.tnerevival.account.Account;

public class SaveConversion {
	
	public static void alphaTwo() {
		Iterator<Entry<String, Account>> it = TNE.instance.manager.accounts.entrySet().iterator();
		
		while(it.hasNext()) {
			Entry<String, Account> entry = it.next();
			
			if(TNE.instance.manager.banks.containsKey(entry.getKey())) {
				entry.getValue().setBank(TNE.instance.defaultWorld, TNE.instance.manager.banks.get(entry.getKey()));
			}
			entry.getValue().setBalance(TNE.instance.defaultWorld, entry.getValue().getBalance());
		}
	}
}