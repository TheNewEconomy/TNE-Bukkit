package com.github.tnerevival.core.transaction;

import org.bukkit.entity.Player;

import com.github.tnerevival.account.Account;
import com.github.tnerevival.utils.AccountUtils;
import com.github.tnerevival.utils.MISCUtils;

public class Transaction {
	
	private double amount;
	private Player to;
	private Player from;
	
	public Transaction(double amount, Player to, Player from) {
		this.amount = amount;
		this.to = to;
		this.from = from;
	}
	
	public void complete() {
		pay(to, amount);
		charge(from, amount);
	}
	
	private boolean pay(Player p, double amount) {
		if(p != null) {
			Account acc = AccountUtils.getAccount(MISCUtils.getID(p));
			if(amount >= 0) {
				acc.setBalance(MISCUtils.getWorld(p), acc.getBalance(MISCUtils.getWorld(p)) + amount);
				return true;
			}
			return charge(p, amount);
		}
		return false;
	}
	
	private boolean charge(Player p, double amount) {
		if(p != null) {
			Account acc = AccountUtils.getAccount(MISCUtils.getID(p));
			if(amount <= 0) {
				acc.setBalance(MISCUtils.getWorld(p), acc.getBalance(MISCUtils.getWorld(p)) - amount);
				return true;
			}
			return pay(p, amount);
		}
		return false;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public Player getTo() {
		return to;
	}

	public void setTo(Player to) {
		this.to = to;
	}

	public Player getFrom() {
		return from;
	}

	public void setFrom(Player from) {
		this.from = from;
	}
}