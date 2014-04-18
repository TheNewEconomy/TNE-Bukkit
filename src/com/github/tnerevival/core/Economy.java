package com.github.tnerevival.core;

import java.util.HashMap;

import com.github.tnerevival.account.Account;
import com.github.tnerevival.account.Bank;
import com.github.tnerevival.lottery.Lottery;

public class Economy {
	
	public HashMap<String, Account> accounts = new HashMap<String, Account>();
	public HashMap<String, Lottery> lotteries = new HashMap<String, Lottery>();
	
	/*
	 * @Deprecated
	 */
	public HashMap<String, Bank> banks = new HashMap<String, Bank>();
	
	//public CurrencyManager currencyManager;
	public SaveManager saveManager;

	public Economy() {
		//currencyManager = new CurrencyManager();
	}
}