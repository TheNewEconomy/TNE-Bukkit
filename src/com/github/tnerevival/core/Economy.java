package com.github.tnerevival.core;

import java.io.File;
import java.util.HashMap;

import com.github.tnerevival.core.accounts.Account;
import com.github.tnerevival.core.areas.Area;
import com.github.tnerevival.core.auctions.Auction;
import com.github.tnerevival.core.companies.Company;

/**
 * The heart of TNE. This class holds all maps, instances, etc.
 * @author creatorfromhell
 *
 */
public class Economy {
	
	/**
	 * A HashMap holding all accounts for the economy.
	 * Format: Player, Account File
	 */
	public HashMap<String, Account> accounts = new HashMap<String, Account>();
	
	/**
	 * A HashMap holding all areas that have been created.
	 * Format: Area Owner, Area File
	 */
	public HashMap<String, Area> areas = new HashMap<String, Area>();
	
	/**
	 * A HashMap holding every auction.
	 * Format: Auction Starter, Auction File
	 */
	public HashMap<String, Auction> auctions = new HashMap<String, Auction>();
	
	/**
	 * A HashMap holding every company created.
	 * Format: Company Name, Company File
	 */
	public HashMap<String, Company> companies = new HashMap<String, Company>();
	
	/**
	 * The directory that holds account files.
	 */
	File accountDirectory;
	
	public Economy() {
		//accountDirectory = new File(TheNewEconomy.instance.getDataFolder(), "Accounts");
	}
	
	/**
	 * Used to initialize the economy if this is the first run.
	 */
	public void initializeEconomy() {
		if(accountDirectory.exists()) {
			accountDirectory.mkdirs();
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