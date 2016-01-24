package com.github.tnerevival.core;

import java.util.HashMap;
import java.util.UUID;

import com.github.tnerevival.account.Access;
import com.github.tnerevival.account.Account;
import com.github.tnerevival.auction.Auction;
import com.github.tnerevival.core.companies.Company;
import com.github.tnerevival.core.inventory.View;
import com.github.tnerevival.lottery.Lottery;

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
	
	public HashMap<UUID, Auction> auctions = new HashMap<UUID, Auction>();
	
	public HashMap<UUID, View> viewers = new HashMap<UUID, View>();
	
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