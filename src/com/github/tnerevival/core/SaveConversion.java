package com.github.tnerevival.core;

import java.util.HashMap;
import java.util.UUID;

import com.github.tnerevival.TNE;
import com.github.tnerevival.account.Account;
import com.github.tnerevival.account.Bank;
import com.github.tnerevival.core.api.MojangAPI;

public class SaveConversion {
	
	public static void alphaTwo() {
		for(String s : TNE.instance.manager.legacy.keySet()) {
			Account acc = TNE.instance.manager.legacy.get(s);
			acc.setBanks(new HashMap<String, Bank>());
			acc.setBalances(new HashMap<String, Double>());
			if(TNE.instance.manager.banks.containsKey(s)) {
				acc.getBanks().put(TNE.instance.defaultWorld, TNE.instance.manager.banks.get(s));
				TNE.instance.manager.banks.remove(s);
			}
			acc.setBalance(TNE.instance.defaultWorld, acc.getBalance());
		}
	}
	
	public static void alphaTwoOne() {
		for(String s : TNE.instance.manager.legacy.keySet()) {
			Account acc = TNE.instance.manager.legacy.get(s);
			UUID uid = MojangAPI.getPlayerUUID(acc.getOwner());
			acc.setUid(uid);
			TNE.instance.manager.accounts.put(uid, acc);
		}
	}
}