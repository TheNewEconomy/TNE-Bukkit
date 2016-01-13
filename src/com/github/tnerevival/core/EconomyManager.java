package com.github.tnerevival.core;

import java.util.HashMap;
import java.util.UUID;

import com.github.tnerevival.account.Access;
import com.github.tnerevival.account.Account;
import com.github.tnerevival.core.companies.Company;
import com.github.tnerevival.lottery.Lottery;
import com.github.tnerevival.worker.InventoryTimeWorker;

public class EconomyManager {
	
	/**
	 * A HashMap holding all accounts for the economy.
	 * Format: Player UUID, Account Class Instance
	 */
	public HashMap<UUID, Account> accounts = new HashMap<UUID, Account>();
	
	/**
	 * A HashMap holding every company created.
	 * Format: Company Name, Company Class Instance
	 */
	public HashMap<String, Company> companies = new HashMap<String, Company>();
	
	/**
	 * A HashMap holding every Lottery that is currently running.
	 * We have this so we can have multiple lotteries at once.
	 * Format: Lottery Name, Lottery Class Instance.
	 */
	public HashMap<String, Lottery> lotteries = new HashMap<String, Lottery>();
	
	public HashMap<UUID, String[]> commandCredits = new HashMap<UUID, String[]>();
	
	public HashMap<UUID, InventoryTimeWorker> invWorkers = new HashMap<UUID, InventoryTimeWorker>();
	
	public HashMap<UUID, Access> accessing = new HashMap<UUID, Access>();
	
	/**
	 * A Map, which holds the economy UUIDs for each player that are used when UUID support is turned off.
	 */
	public HashMap<String, UUID> ecoIDs = new HashMap<String, UUID>();
	
	//public CurrencyManager currencyManager;

	public EconomyManager() {
		//currencyManager = new CurrencyManager();
	}
}