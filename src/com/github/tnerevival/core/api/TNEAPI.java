package com.github.tnerevival.core.api;

import java.util.UUID;

import org.bukkit.OfflinePlayer;

import com.github.tnerevival.TNE;
import com.github.tnerevival.account.Account;
import com.github.tnerevival.utils.AccountUtils;
import com.github.tnerevival.utils.MISCUtils;

public class TNEAPI {
	
	public TNE plugin;
	
	public TNEAPI(TNE plugin) {
		this.plugin = plugin;
	}
	
	@Deprecated
	public Boolean accountExists(String username) {
		UUID id = getPlayerID(username);
		return AccountUtils.getAccount(id) != null;
	}

	@Deprecated
	public void createAccount(String username) {
		UUID id = getPlayerID(username);
		AccountUtils.createAccount(id);
	}

	@Deprecated
	public void fundsAdd(String username, Double amount) {
		UUID id = getPlayerID(username);
		AccountUtils.addFunds(id, amount);
	}

	@Deprecated
	public void fundsAdd(String username, String world, Double amount) {
		UUID id = getPlayerID(username);
		AccountUtils.addFunds(id, amount);
	}

	@Deprecated
	public Boolean fundsHas(String username, Double amount) {
		UUID id = getPlayerID(username);
		return AccountUtils.hasFunds(id, amount);
	}

	@Deprecated
	public Boolean fundsHas(String username, String world, Double amount) {
		UUID id = getPlayerID(username);
		return AccountUtils.hasFunds(id, world, amount);
	}

	@Deprecated
	public void fundsRemove(String username, Double amount) {
		UUID id = getPlayerID(username);
		AccountUtils.removeFunds(id, amount);
	}

	@Deprecated
	public void fundsRemove(String username, String world, Double amount) {
		UUID id = getPlayerID(username);
		AccountUtils.removeFunds(id, world, amount);
	}

	@Deprecated
	public Account getAccount(String username) {
		UUID id = getPlayerID(username);
		Account account = AccountUtils.getAccount(id);
		return account;
	}

	@Deprecated
	public Double getBalance(String username) {
		UUID id = getPlayerID(username);
		return AccountUtils.getFunds(id);
	}

	@Deprecated
	public Double getBalance(String username, String world) {
		UUID id = getPlayerID(username);
		return AccountUtils.getFunds(id, world);
	}
	
	public Boolean accountExists(OfflinePlayer player) {
		return getAccount(player) != null;
	}

	public void createAccount(OfflinePlayer player) {
		AccountUtils.createAccount(MISCUtils.getID(player));
	}

	public void fundsAdd(OfflinePlayer player, Double amount) {
		AccountUtils.addFunds(MISCUtils.getID(player), amount);
	}

	public void fundsAdd(OfflinePlayer player, String world, Double amount) {
		AccountUtils.addFunds(MISCUtils.getID(player), amount);
	}

	public Boolean fundsHas(OfflinePlayer player, Double amount) {
		return AccountUtils.hasFunds(MISCUtils.getID(player), amount);
	}

	public Boolean fundsHas(OfflinePlayer player, String world, Double amount) {
		return AccountUtils.hasFunds(MISCUtils.getID(player), world, amount);
	}

	public void fundsRemove(OfflinePlayer player, Double amount) {
		AccountUtils.removeFunds(MISCUtils.getID(player), amount);
	}

	public void fundsRemove(OfflinePlayer player, String world, Double amount) {
		AccountUtils.removeFunds(MISCUtils.getID(player), world, amount);
	}

	public Account getAccount(OfflinePlayer player) {
		return AccountUtils.getAccount(MISCUtils.getID(player));
	}

	public Double getBalance(OfflinePlayer player) {
		return AccountUtils.getFunds(MISCUtils.getID(player));
	}

	public Double getBalance(OfflinePlayer player, String world) {
		return AccountUtils.getFunds(MISCUtils.getID(player), world);
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
	
	public UUID getPlayerID(String username) {
		if(username.contains("faction-")) {
			return UUID.fromString(username.substring(8, username.length() - 1));
		}
		return MISCUtils.getID(username);
	}
}