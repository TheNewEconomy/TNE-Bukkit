package com.github.tnerevival.core.accounts;

import java.io.Serializable;

import com.github.tnerevival.TheNewEconomy;

public class Account implements Serializable {
	private static final long serialVersionUID = 1L;

	int accountNumber = 0;
	String owner;
	String pinCode;
	double balance;
	String status;
	
	public Account(String username) {
		this.accountNumber = TheNewEconomy.instance.config.getInt("highest-accountnumber") + 1;
		this.owner = username;
		this.pinCode = "default";
		this.balance = TheNewEconomy.instance.config.getDouble("starting-balance");
		this.status = "Normal";
		TheNewEconomy.instance.config.set("highest-accountnumber", this.accountNumber);
	}
}