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
import com.github.tnerevival.utils.AccountUtils;

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
	
	private HashMap<String, HashMap<String, Long>> credits = new HashMap<String, HashMap<String, Long>>();
	
	private HashMap<String, Integer> commands = new HashMap<String, Integer>();
	

	private List<SerializableItemStack> overflow = new ArrayList<SerializableItemStack>();
	
	private String joined;
	
	/**
	 * The account number for this account.
	 * This number is unique to the account.
	 */
	private int accountNumber = 0;
	
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
	//TODO: Make use of account statuses
	private AccountStatus status;
	
	//TODO: Make use of the pin
	private String pin;
	
	public Account(UUID uid) {
		this(uid, TNE.instance.manager.accounts.size() + 1);
	}
	
	public Account(UUID uid, int accountNumber) {
		this.uid = uid;
		this.joined = new String(TNE.instance.dateFormat.format(new Date()));
		this.accountNumber = accountNumber;
		this.company = "TNENOSTRINGVALUE";
		this.status = AccountStatus.NORMAL;
		this.pin = "TNENOSTRINGVALUE";
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
		String[] b = from.split("\\:");
		
		for(String s : b) {
			String[] balance = s.split("\\,");
			balances.put(balance[0], Double.valueOf(balance[1]));
		}
	}
	
	/*
	 * Inventory Time Credits
	 */
	public HashMap<String, Long> getTimes(String inventory) {
		if(credits.get(inventory) != null) {
			return credits.get(inventory);
		}
		return new HashMap<String, Long>();
	}
	
	public void addTime(String world, String inventory, Long time) {
		setTime(world, inventory, getTimeLeft(world, inventory) + time);
	}
	
	public Long getTimeLeft(String world, String inventory) {
		if(credits.get(inventory) != null) {
			return (credits.get(inventory).get(world) != null)? credits.get(inventory).get(world) : 0;
		}
		return 0L;
	}
	
	public void setTime(String world, String inventory, long time) {
		HashMap<String, Long> inventoryCredits = (credits.get(inventory) != null) ? credits.get(inventory) : new HashMap<String, Long>();
		inventoryCredits.put(world, time);
		credits.put(inventory, inventoryCredits);
	}
	
	/*
	 * Command Credits
	 */
	public void addCredit(String command) {
		if(commands.containsKey(command)) {
			commands.put(command, commands.get(command) + 1);
		}
	}
	
	public void removeCredit(String command) {
		if(commands.containsKey(command)) {
			commands.put(command, commands.get(command) - 1);
		}
	}
	
	public Boolean hasCredit(String command) {
		return (commands.containsKey(command) && commands.get(command) > 0);
	}
	
	public HashMap<String, Integer> getCredits() {
		return commands;
	}
	
	/*
	 * MISC Methods/Getters & Setters
	 */
	public String overflowToString() {
		if(!overflow.isEmpty()) {
			String toReturn = "";
			
			int count = 0;
			for(SerializableItemStack item : overflow) {
				if(count != 0) {
					toReturn += "*";
				}
				toReturn += item.toString();
				count++;
			}
			return toReturn;
		}
		return "TNENOSTRINGVALUE";
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
	public AccountStatus getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = AccountStatus.fromName(status);
	}

	public String getPin() {
		return pin;
	}

	public void setPin(String pin) {
		this.pin = pin;
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
		this.balances.put(world, AccountUtils.round(balance));
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