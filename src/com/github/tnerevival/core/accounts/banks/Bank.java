package com.github.tnerevival.core.accounts.banks;

public class Bank {
	
	String owner;
	Integer money;
	BankSlot[] contents;
	
	public Bank(String owner) {
		this.owner = owner;
	}
	
	public Bank(String owner, Integer money) {
		this.owner = owner;
		this.money = money;
	}

	/**
	 * @return the owner
	 */
	public String getOwner() {
		return owner;
	}

	/**
	 * @return the money
	 */
	public Integer getMoney() {
		return money;
	}
}