package com.github.tnerevival.account;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import com.github.tnerevival.TNE;
import com.github.tnerevival.serializable.SerializableItemStack;

public class Account implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * A HashMap of this account's balances from every world that the player has visited.
	 */
	private HashMap<String, Double> balances = new HashMap<String, Double>();
	
	/**
	 * A HashMap of this account's banks from every world that the player has visited.
	 */
	private HashMap<String, Bank> banks = new HashMap<String, Bank>();
	

	private List<SerializableItemStack> overflow = new ArrayList<SerializableItemStack>();
	
	private String joined;
	
	/**
	 * The account number for this account.
	 * This number is unique to the account.
	 */
	private int accountNumber = 0;
	
	/**
	 * The name of the player who this account belongs to.
	 */
	private String owner;
	
	private UUID uid;
	
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
		this.joined = new String(TNE.instance.dateFormat.format(new Date()));
		this.accountNumber = TNE.instance.manager.accounts.size() + 1;
		this.owner = username;
		this.company = "none";
		this.status = "normal";
		setBalance(TNE.instance.defaultWorld, 0.0);
	}
	
	public String balancesToString() {
		Iterator<java.util.Map.Entry<String, Double>> balanceIterator = balances.entrySet().iterator();
		
		int count = 0;
		String toReturn = "";
		while(balanceIterator.hasNext()) {
			java.util.Map.Entry<String, Double> balanceEntry = balanceIterator.next();
			if(count > 0) {
				toReturn += ":";
			}
			toReturn += balanceEntry.getKey() + "," + balanceEntry.getValue();
			count++;
		}
		return toReturn;
	}
	
	public void balancesFromString(String from) {
		String[] b = from.split(":");
		
		for(String s : b) {
			String[] balance = s.split(",");
			balances.put(balance[0], Double.valueOf(balance[1]));
		}
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

	public UUID getUid() {
		return uid;
	}

	public void setUid(UUID uid) {
		this.uid = uid;
	}

	/**
	 * @return the joined
	 */
	public String getJoined() {
		return joined;
	}

	/**
	 * @param joined the joined to set
	 */
	public void setJoined(String joined) {
		this.joined = joined;
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

	public HashMap<String, Double> getBalances() {
		return balances;
	}

	public void setBalances(HashMap<String, Double> balances) {
		this.balances = balances;
	}
	
	public Double getBalance(String world) {
		return balances.get(world);
	}
	
	public void setBalance(String world, Double balance) {
		this.balances.put(world, balance);
	}

	public HashMap<String, Bank> getBanks() {
		return banks;
	}

	public void setBanks(HashMap<String, Bank> banks) {
		this.banks = banks;
	}
	
	public void setBank(String world, Bank bank) {
		this.banks.put(world, bank);
	}
	
	public Bank getBank(String world) {
		return this.banks.get(world);
	}

	public List<SerializableItemStack> getOverflow() {
		return overflow;
	}

	public void setOverflow(List<SerializableItemStack> overflow) {
		this.overflow = overflow;
	}
}