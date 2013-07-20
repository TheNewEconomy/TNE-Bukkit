package com.github.tnerevival.core.accounts;

import com.github.tnerevival.TheNewEconomy;

public class Transaction {
	
	/**
	 * Used to check if the specified player has enough money.
	 * @param The player's name.
	 * @param The amount that the player needs.
	 * @return If the player has enough money or not.
	 */
	public static Boolean hasEnough(String player, Double amount) {
		Account acc = TheNewEconomy.instance.eco.accounts.get(player);
		if(acc.getBalance() >= amount) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Used to add money to a player's balance.
	 * @param The player's name.
	 * @param The amount of money to add.
	 */
	public static void addFunds(String player, Double amount) {
		Account acc = TheNewEconomy.instance.eco.accounts.get(player);
		acc.setBalance(acc.getBalance() + amount);
	}
	
	/**
	 * Used to take money from a player's balance.
	 * @param The player's name.
	 * @param The amount of money to take.
	 */
	public static void takeFunds(String player, Double amount) {
		Account acc = TheNewEconomy.instance.eco.accounts.get(player);
		acc.setBalance(acc.getBalance() - amount);
	}
	
	/**
	 * Used to withdraw money from a player's bank account.
	 */
	public void withdrawMoney() {
		
	}
	
	/**
	 * Used to deposit money into a player's bank account.
	 */
	public void depositMoney() {
		
	}
	
	/**
	 * Used to transfer money from one player's bank account to another's.
	 */
	public void transferMoney() {
		
	}
	
	/**
	 * Used to calculate the amount of money the player should get from interest.
	 */
	public void calcMoneyInterest() {
		
	}
}