package com.github.tnerevival.core.api;

import com.github.tnerevival.TNE;
import com.github.tnerevival.account.Account;
import com.github.tnerevival.utils.AccountUtils;
import com.github.tnerevival.utils.MISCUtils;

public class TNEAPI {
	
	public TNE plugin;
	
	public TNEAPI(TNE plugin) {
		this.plugin = plugin;
	}
	
	public Boolean accountExists(String username) {
		return getAccount(username) != null;
	}
	
	public void createAccount(String username) {
		AccountUtils.createAccount(username);
	}
	
	public String format(Double amount) {
		return MISCUtils.formatBalance(plugin.defaultWorld, amount);
	}
	
	public String format(String world, Double amount) {
		return MISCUtils.formatBalance(world, amount);
	}
	
	public String format(String world, Double amount, Boolean shorten) {
		return MISCUtils.formatBalance(world, amount, shorten);
	}
	
	public void fundsAdd(String username, Double amount) {
		AccountUtils.addFunds(username, amount);
	}
	
	public void fundsAdd(String username, String world, Double amount) {
		AccountUtils.addFunds(username, amount);
	}
	
	public Boolean fundsHas(String username, Double amount) {
		return AccountUtils.hasFunds(username, amount);
	}
	
	public Boolean fundsHas(String username, String world, Double amount) {
		return AccountUtils.hasFunds(username, world, amount);
	}
	
	public void fundsRemove(String username, Double amount) {
		AccountUtils.removeFunds(username, amount);
	}
	
	public void fundsRemove(String username, String world, Double amount) {
		AccountUtils.removeFunds(username, world, amount);
	}
	
	public Account getAccount(String username) {
		return AccountUtils.getAccount(username);
	}
	
	public Double getBalance(String username) {
		return AccountUtils.getFunds(username);
	}
	
	public Double getBalance(String username, String world) {
		return AccountUtils.getFunds(username, world);
	}
	
	public String getCurrencyName(Boolean major, Boolean singular) {
		return getCurrencyName(major, singular, plugin.defaultWorld);
	}
	
	public String getCurrencyName(Boolean major, Boolean singular, String world) {
		return (major) ? MISCUtils.getMajorCurrencyName(world, singular) : MISCUtils.getMinorCurrencyName(world, singular);
	}
	
	public Boolean getShorten() {
		return getShorten(plugin.defaultWorld);
	}
	
	public Boolean getShorten(String world) {
		return MISCUtils.shorten(world);
	}
}