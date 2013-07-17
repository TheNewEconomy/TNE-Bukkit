package com.github.tnerevival.core;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;

import com.github.tnerevival.TheNewEconomy;
import com.github.tnerevival.core.accounts.Account;
import com.github.tnerevival.core.areas.Area;
import com.github.tnerevival.core.auctions.Auction;
import com.github.tnerevival.core.companies.Company;
import com.github.tnerevival.core.lottery.Lottery;

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
	 * A HashMap holding every Lottery that is currently running.
	 * We have this so we can have multiple lotteries at once.
	 * Format: Lottery Name, Lottery File.
	 */
	public HashMap<String, Lottery> lotteries = new HashMap<String, Lottery>();
	
	File accountsFile;
	File areasFile;
	File companiesFile;
	
	public Economy() {
		accountsFile = new File(TheNewEconomy.instance.getDataFolder(), "accounts.tne");
		areasFile = new File(TheNewEconomy.instance.getDataFolder(), "areas.tne");
		companiesFile = new File(TheNewEconomy.instance.getDataFolder(), "companies.tne");
		initializeEconomy();
	}
	
	/**
	 * Used to initialize the economy if this is the first run.
	 */
	void initializeEconomy() {
		if(!accountsFile.exists()) {
			try {
				accountsFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if(!areasFile.exists()) {
			try {
				areasFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if(!companiesFile.exists()) {
			try {
				companiesFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		loadData();
	}
	
	public void loadData() {
		System.out.println("[TNE]Loading data....");
		try {
			accounts = EconomyIO.loadAccounts();
			areas = EconomyIO.loadAreas();
			companies = EconomyIO.loadCompanies();
		} catch (FileNotFoundException e) {
			System.out.println("[TNE] Data File(s) not found. Generating file...");
		} catch (ClassNotFoundException e) {
			System.out.println("[TNE] Data File(s) not found. Generating file...");
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("[TNE]All data has been loaded.");
	}
	
	public void saveData() {
		System.out.println("[TNE]Saving data....");
		EconomyIO.saveAccounts(accounts);
		EconomyIO.saveAreas(areas);
		EconomyIO.saveCompanies(companies);
		System.out.println("[TNE]All data has been saved.");
	}
}