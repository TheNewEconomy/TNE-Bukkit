package com.github.tnerevival.core;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.OfflinePlayer;

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
		return "TheNewEconomy";
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
	public EconomyResponse createBank(String name, OfflinePlayer player) {
		return new EconomyResponse(0, 0, ResponseType.NOT_IMPLEMENTED, "Banks are not supported in The New Economy!");
	}

	@Override
	public boolean createPlayerAccount(OfflinePlayer player, String world) {
		return createPlayerAccount(player);
	}

	@Override
	public boolean createPlayerAccount(OfflinePlayer player) {
		if(hasAccount(player)) {
			return false;
		}
		api.createAccount(player);
		return true;
	}

	@Override
	public EconomyResponse depositPlayer(OfflinePlayer player, double amount) {
		if(!hasAccount(player)) {
            return new EconomyResponse(0, 0, ResponseType.FAILURE, "That account does not exist!");
		}
		
		if(amount < 0) {
			return new EconomyResponse(0, 0, ResponseType.FAILURE, "Cannot deposit negative amounts.");
		}
		api.fundsAdd(player, amount);
		return new EconomyResponse(amount, getBalance(player), ResponseType.SUCCESS, "");
	}

	@Override
	public EconomyResponse depositPlayer(OfflinePlayer player, String world, double amount) {
		if(!hasAccount(player)) {
            return new EconomyResponse(0, 0, ResponseType.FAILURE, "That account does not exist!");
		}
		
		if(amount < 0) {
			return new EconomyResponse(0, 0, ResponseType.FAILURE, "Cannot deposit negative amounts.");
		}
		api.fundsAdd(player, world, amount);
		return new EconomyResponse(amount, getBalance(player, world), ResponseType.SUCCESS, "");
	}

	@Override
	public double getBalance(OfflinePlayer player, String world) {
		return api.getBalance(player, world);
	}

	@Override
	public double getBalance(OfflinePlayer player) {
		return api.getBalance(player);
	}

	@Override
	public boolean has(OfflinePlayer player, double amount) {
		return api.fundsHas(player, amount);
	}

	@Override
	public boolean has(OfflinePlayer player, String world, double amount) {
		return api.fundsHas(player, world, amount);
	}

	@Override
	public boolean hasAccount(OfflinePlayer player, String world) {
		return hasAccount(player);
	}

	@Override
	public boolean hasAccount(OfflinePlayer player) {
		return api.accountExists(player);
	}

	@Override
	public EconomyResponse isBankMember(String name, OfflinePlayer player) {
		return new EconomyResponse(0, 0, ResponseType.NOT_IMPLEMENTED, "Banks are not supported in The New Economy!");
	}

	@Override
	public EconomyResponse isBankOwner(String name, OfflinePlayer player) {
		return new EconomyResponse(0, 0, ResponseType.NOT_IMPLEMENTED, "Banks are not supported in The New Economy!");
	}

	@Override
	public EconomyResponse withdrawPlayer(OfflinePlayer player, double amount) {
		if(!hasAccount(player)) {
            return new EconomyResponse(0, 0, ResponseType.FAILURE, "That account does not exist!");
		}
		
		if(amount < 0) {
			return new EconomyResponse(0, 0, ResponseType.FAILURE, "Cannot withdraw negative amounts.");
		}
		
		if(!has(player, amount)) {
			return new EconomyResponse(0, 0, ResponseType.FAILURE, "Insufficient funds!");
		}
		api.fundsRemove(player, amount);
		return new EconomyResponse(amount, getBalance(player), ResponseType.SUCCESS, "");
	}

	@Override
	public EconomyResponse withdrawPlayer(OfflinePlayer player, String world, double amount) {
		if(!hasAccount(player)) {
            return new EconomyResponse(0, 0, ResponseType.FAILURE, "That account does not exist!");
		}
		
		if(amount < 0) {
			return new EconomyResponse(0, 0, ResponseType.FAILURE, "Cannot withdraw negative amounts.");
		}
		
		if(!has(player, world, amount)) {
			return new EconomyResponse(0, 0, ResponseType.FAILURE, "Insufficient funds!");
		}
		api.fundsRemove(player, world, amount);
		return new EconomyResponse(amount, getBalance(player, world), ResponseType.SUCCESS, "");
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
	public List<String> getBanks() {
		return new ArrayList<String>();
	}

	@Override
	@Deprecated
	public boolean createPlayerAccount(String username) {
		if(hasAccount(username)) {
			return false;
		}
		api.createAccount(username);
		return true;
	}

	@Override
	@Deprecated
	public boolean createPlayerAccount(String username, String world) {
		return createPlayerAccount(username);
	}

	@Override
	@Deprecated
	public EconomyResponse bankBalance(String username) {
		return new EconomyResponse(0, 0, ResponseType.NOT_IMPLEMENTED, "Banks are not supported in The New Economy!");
	}

	@Override
	@Deprecated
	public EconomyResponse bankDeposit(String username, double amount) {
		return new EconomyResponse(0, 0, ResponseType.NOT_IMPLEMENTED, "Banks are not supported in The New Economy!");
	}

	@Override
	@Deprecated
	public EconomyResponse bankHas(String username, double amount) {
		return new EconomyResponse(0, 0, ResponseType.NOT_IMPLEMENTED, "Banks are not supported in The New Economy!");
	}

	@Override
	@Deprecated
	public EconomyResponse bankWithdraw(String username, double amount) {
		return new EconomyResponse(0, 0, ResponseType.NOT_IMPLEMENTED, "Banks are not supported in The New Economy!");
	}

	@Override
	@Deprecated
	public EconomyResponse createBank(String username, String world) {
		return new EconomyResponse(0, 0, ResponseType.NOT_IMPLEMENTED, "Banks are not supported in The New Economy!");
	}

	@Override
	@Deprecated
	public EconomyResponse deleteBank(String username) {
		return new EconomyResponse(0, 0, ResponseType.NOT_IMPLEMENTED, "Banks are not supported in The New Economy Lite!");
	}

	@Override
	@Deprecated
	public EconomyResponse depositPlayer(String username, double amount) {
		if(!hasAccount(username)) {
            return new EconomyResponse(0, 0, ResponseType.FAILURE, "That account does not exist!");
		}
		
		if(amount < 0) {
			return new EconomyResponse(0, 0, ResponseType.FAILURE, "Cannot deposit negative amounts.");
		}
		api.fundsAdd(username, amount);
		return new EconomyResponse(amount, getBalance(username), ResponseType.SUCCESS, "");
	}

	@Override
	@Deprecated
	public EconomyResponse depositPlayer(String username, String world, double amount) {
		if(!hasAccount(username)) {
            return new EconomyResponse(0, 0, ResponseType.FAILURE, "That account does not exist!");
		}
		
		if(amount < 0) {
			return new EconomyResponse(0, 0, ResponseType.FAILURE, "Cannot deposit negative amounts.");
		}
		api.fundsAdd(username, world, amount);
		return new EconomyResponse(amount, getBalance(username, world), ResponseType.SUCCESS, "");
	}

	@Override
	@Deprecated
	public double getBalance(String username) {
		return api.getBalance(username);
	}

	@Override
	@Deprecated
	public double getBalance(String username, String world) {
		return api.getBalance(username, world);
	}

	@Override
	@Deprecated
	public boolean has(String username, double amount) {
		return api.fundsHas(username, amount);
	}

	@Override
	@Deprecated
	public boolean has(String username, String world, double amount) {
		return api.fundsHas(username, world, amount);
	}

	@Override
	@Deprecated
	public boolean hasAccount(String username) {
		return api.accountExists(username);
	}

	@Override
	@Deprecated
	public boolean hasAccount(String username, String world) {
		return hasAccount(username);
	}

	@Override
	@Deprecated
	public EconomyResponse isBankMember(String name, String username) {
		return new EconomyResponse(0, 0, ResponseType.NOT_IMPLEMENTED, "Banks are not supported in The New Economy!");
	}

	@Override
	@Deprecated
	public EconomyResponse isBankOwner(String name, String username) {
		return new EconomyResponse(0, 0, ResponseType.NOT_IMPLEMENTED, "Banks are not supported in The New Economy!");
	}

	@Override
	@Deprecated
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
	@Deprecated
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