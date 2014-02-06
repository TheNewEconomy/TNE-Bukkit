package com.github.tnerevival.account;

import java.io.Serializable;

import com.github.tnerevival.TNE;

public class Account implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * The account number for this account.
	 * This number is unique to the account.
	 */
	private int accountNumber = 0;
	
	/**
	 * The name of the player who this account belongs to.
	 */
	private String owner;
	
	/**
	 * The name of the Company this player is associated with.
	 */
	private String company;
	
	/**
	 * The account's balance of in-game virtual currency.
	 */
	private double balance;
	
	/**
	 * The status of this account in String form.
	 */
	private String status;
	
	/**
	 * Creates a new Account for the specified Player using their username.
	 * @param username
	 */
	public Account(String username) {
		this.accountNumber = TNE.instance.manager.accounts.size() + 1;
		this.owner = username;
		this.company = "none";
		this.balance = TNE.instance.getConfig().getDouble("Core.Balance");
		this.status = "normal";
	}

	/**
	 * @return the accountNumber
	 */
	public int getAccountNumber() {
		return accountNumber;
	}

	/**
	 * @param accountNumber the accountNumber to set
	 */
	public void setAccountNumber(int accountNumber) {
		this.accountNumber = accountNumber;
	}

	/**
	 * @return the owner
	 */
	public String getOwner() {
		return owner;
	}

	/**
	 * @param owner the owner to set
	 */
	public void setOwner(String owner) {
		this.owner = owner;
	}

	/**
	 * @return the company
	 */
	public String getCompany() {
		return company;
	}

	/**
	 * @param company the company to set
	 */
	public void setCompany(String company) {
		this.company = company;
	}

	/**
	 * @return the balance
	 */
	public double getBalance() {
		return balance;
	}

	/**
	 * @param balance the balance to set
	 */
	public void setBalance(double balance) {
		this.balance = balance;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}
}