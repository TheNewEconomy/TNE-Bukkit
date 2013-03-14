package com.github.tnerevival.core;

import java.util.HashMap;

public class Account extends AccountHolder {
	
	/**
	 * A Map containing information about all player accounts.
	 */
	HashMap<String, AccountHolder> accounts = new HashMap<String, AccountHolder>();
	
	//Default Constructor
	public Account () {
		
	}
	
	//SECTION: methods
	
	
	/**
	 * Used to create an account.
	 * @param owner
	 */
	public void createAccount(String owner) {
		
	}
	
	/**
	 * Used to delete an account.
	 * @param owner
	 */
	public void deleteAccount(String owner) {
		
	}
	
	/**
	 * Used to backup every account just in-case.
	 */
	public void backupAccounts() {
		
	}
	
	/**
	 * Used to initialize the account directory and add a README for the account system and an example account.
	 */
	public void initAccountDirectory() {
		
	}
	
	
	//SECTION: boolean
	
	
	/**
	 * Used to check if a particular account exists.
	 * @param owner
	 * @return Whether or not the account exists.
	 */
	public boolean doesAccountExist(String owner) {
		return false;
	}
	
}
