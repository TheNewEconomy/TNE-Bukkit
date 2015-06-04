package com.github.tnerevival.core;

import java.util.HashMap;
import java.util.UUID;

import com.github.tnerevival.account.Account;
import com.github.tnerevival.account.Bank;
import com.github.tnerevival.core.companies.Company;
import com.github.tnerevival.lottery.Lottery;

public class EconomyManager {
	
	/**
	 * A HashMap holding all accounts for the economy.
	 * Format: Player name, Account Class Instance
	 */
	@Deprecated
	public HashMap<String, Account> legacy = new HashMap<String, Account>();
	
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
	
	@Deprecated
	public HashMap<String, Bank> banks = new HashMap<String, Bank>();
	
	//public CurrencyManager currencyManager;

	public EconomyManager() {
		//currencyManager = new CurrencyManager();
	}
}