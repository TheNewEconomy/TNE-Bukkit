package com.github.tnerevival.core.accounts;

public class AccountHolder {
	int accountNumber = 0;
	String owner;
	String pinCode;
	double balance;
	String status;
	public static int highestAccountNumber = 0;
	
	//Default Constructor
	public AccountHolder() {
	}
	
	//Constructor with parameters
	public AccountHolder(int aN, String o, String pin, double b, String s)
	{
		highestAccountNumber ++ ;
		accountNumber = aN;
		owner = o;
		pinCode = pin;
		balance = b;
		status = s;
	}
}
