package com.github.tnerevival.core;

import java.io.File;
import java.util.HashMap;

import com.github.tnerevival.TheNewEconomy;
import com.github.tnerevival.core.accounts.Account;
import com.github.tnerevival.core.areas.Area;

public class Economy {
	/**
	 * A HashMap holding all accounts for the economy.
	 */
	HashMap<String, Account> accounts = new HashMap<String, Account>();
	HashMap<String, Area> areas = new HashMap<String, Area>();
	File accountDirectory;
	
	public Economy() {
		accountDirectory = new File(TheNewEconomy.instance.getDataFolder(), "Accounts");
	}
	
	/**
	 * Used to initialize the economy if this is the first run.
	 */
	public void initializeEconomy() {
		if(TheNewEconomy.instance.config.getBoolean("firstrun")) {
			if(accountDirectory.exists()) {
				accountDirectory.mkdirs();
			}
		}
	}
	
	/**
	 * Used to load all accounts into the HashMap.
	 */
	public void loadAccounts() {
		
	}
	
	/**
	 * Used to save all accounts that are in the HashMap.
	 */
	public void saveAccounts() {
		
	}
}