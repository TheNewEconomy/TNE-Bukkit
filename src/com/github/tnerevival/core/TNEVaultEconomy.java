package com.github.tnerevival.core;

import java.util.ArrayList;
import java.util.List;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import net.milkbowl.vault.economy.EconomyResponse.ResponseType;

import com.github.tnerevival.TNE;
import com.github.tnerevival.core.api.TNEAPI;

public class TNEVaultEconomy implements Economy {
	
	public TNE plugin = null;
	public TNEAPI api = null;
	
	public TNEVaultEconomy(TNE plugin) {
		this.plugin = plugin;
		this.api = plugin.api;
	}
	
	@Override
	public String getName() {
		return "TNE";
	}
	
	@Override
	public boolean isEnabled() {
		return api != null;
	}
	
	@Override
	public boolean hasBankSupport() {
		return false;
	}

	@Override
	public EconomyResponse bankBalance(String username) {
		return new EconomyResponse(0, 0, ResponseType.NOT_IMPLEMENTED, "Banks are not supported in The New Economy Lite!");
	}

	@Override
	public EconomyResponse bankDeposit(String username, double amount) {
		return new EconomyResponse(0, 0, ResponseType.NOT_IMPLEMENTED, "Banks are not supported in The New Economy Lite!");
	}

	@Override
	public EconomyResponse bankHas(String username, double amount) {
		return new EconomyResponse(0, 0, ResponseType.NOT_IMPLEMENTED, "Banks are not supported in The New Economy Lite!");
	}

	@Override
	public EconomyResponse bankWithdraw(String username, double amount) {
		return new EconomyResponse(0, 0, ResponseType.NOT_IMPLEMENTED, "Banks are not supported in The New Economy Lite!");
	}

	@Override
	public EconomyResponse createBank(String username, String world) {
		return new EconomyResponse(0, 0, ResponseType.NOT_IMPLEMENTED, "Banks are not supported in The New Economy Lite!");
	}

	@Override
	public boolean createPlayerAccount(String username) {
		return createPlayerAccount(username, "");
	}

	@Override
	public boolean createPlayerAccount(String username, String world) {
		if(hasAccount(username)) {
			return false;
		}
		api.createAccount(username);
		return true;
	}

	@Override
	public String currencyNamePlural() {
		return api.getCurrencyName(true, false);
	}

	@Override
	public String currencyNameSingular() {
		return api.getCurrencyName(true, true);
	}

	@Override
	public EconomyResponse deleteBank(String username) {
		return new EconomyResponse(0, 0, ResponseType.NOT_IMPLEMENTED, "Banks are not supported in The New Economy Lite!");
	}

	@Override
	public EconomyResponse depositPlayer(String username, double amount) {
		if(!hasAccount(username)) {
            return new EconomyResponse(0, 0, ResponseType.FAILURE, "That account does not exist!");
		}
		
		if(amount < 0) {
			return new EconomyResponse(0, 0, ResponseType.FAILURE, "Cannot withdraw negative amounts.");
		}
		api.fundsAdd(username, amount);
		return new EconomyResponse(amount, getBalance(username), ResponseType.SUCCESS, "");
	}

	@Override
	public EconomyResponse depositPlayer(String username, String world, double amount) {
		if(!hasAccount(username)) {
            return new EconomyResponse(0, 0, ResponseType.FAILURE, "That account does not exist!");
		}
		
		if(amount < 0) {
			return new EconomyResponse(0, 0, ResponseType.FAILURE, "Cannot withdraw negative amounts.");
		}
		api.fundsAdd(username, world, amount);
		return new EconomyResponse(amount, getBalance(username, world), ResponseType.SUCCESS, "");
	}

	@Override
	public String format(double amount) {
		return api.format(amount);
	}

	@Override
	public int fractionalDigits() {
		if(api.getShorten()) {
			return 0;
		}
		return -1;
	}

	@Override
	public double getBalance(String username) {
		return api.getBalance(username);
	}

	@Override
	public double getBalance(String username, String world) {
		return api.getBalance(username, world);
	}

	@Override
	public List<String> getBanks() {
		return new ArrayList<String>();
	}

	@Override
	public boolean has(String username, double amount) {
		return api.fundsHas(username, amount);
	}

	@Override
	public boolean has(String username, String world, double amount) {
		return api.fundsHas(username, world, amount);
	}

	@Override
	public boolean hasAccount(String username) {
		return hasAccount(username, "");
	}

	@Override
	public boolean hasAccount(String username, String world) {
		return api.accountExists(username);
	}

	@Override
	public EconomyResponse isBankMember(String username, String arg1) {
		return new EconomyResponse(0, 0, ResponseType.NOT_IMPLEMENTED, "Banks are not supported in The New Economy Lite!");
	}

	@Override
	public EconomyResponse isBankOwner(String username, String arg1) {
		return new EconomyResponse(0, 0, ResponseType.NOT_IMPLEMENTED, "Banks are not supported in The New Economy Lite!");
	}

	@Override
	public EconomyResponse withdrawPlayer(String username, double amount) {
		if(!hasAccount(username)) {
            return new EconomyResponse(0, 0, ResponseType.FAILURE, "That account does not exist!");
		}
		
		if(amount < 0) {
			return new EconomyResponse(0, 0, ResponseType.FAILURE, "Cannot withdraw negative amounts.");
		}
		
		if(!has(username, amount)) {
			return new EconomyResponse(0, 0, ResponseType.FAILURE, "Insufficient funds!");
		}
		api.fundsRemove(username, amount);
		return new EconomyResponse(amount, getBalance(username), ResponseType.SUCCESS, "");
	}

	@Override
	public EconomyResponse withdrawPlayer(String username, String world, double amount) {
		if(!hasAccount(username)) {
            return new EconomyResponse(0, 0, ResponseType.FAILURE, "That account does not exist!");
		}
		
		if(amount < 0) {
			return new EconomyResponse(0, 0, ResponseType.FAILURE, "Cannot withdraw negative amounts.");
		}
		
		if(!has(username, world, amount)) {
			return new EconomyResponse(0, 0, ResponseType.FAILURE, "Insufficient funds!");
		}
		api.fundsRemove(username, world, amount);
		return new EconomyResponse(amount, getBalance(username, world), ResponseType.SUCCESS, "");
	}

}