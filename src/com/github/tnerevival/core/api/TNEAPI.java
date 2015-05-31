package com.github.tnerevival.core.api;

import org.bukkit.Bukkit;
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
		return getAccount(username) != null;
	}

	@Deprecated
	public void createAccount(String username) {
		AccountUtils.createAccount(Bukkit.getPlayer(username).getUniqueId());
	}

	@Deprecated
	public void fundsAdd(String username, Double amount) {
		AccountUtils.addFunds(Bukkit.getPlayer(username).getUniqueId(), amount);
	}

	@Deprecated
	public void fundsAdd(String username, String world, Double amount) {
		AccountUtils.addFunds(Bukkit.getPlayer(username).getUniqueId(), amount);
	}

	@Deprecated
	public Boolean fundsHas(String username, Double amount) {
		return AccountUtils.hasFunds(Bukkit.getPlayer(username).getUniqueId(), amount);
	}

	@Deprecated
	public Boolean fundsHas(String username, String world, Double amount) {
		return AccountUtils.hasFunds(Bukkit.getPlayer(username).getUniqueId(), world, amount);
	}

	@Deprecated
	public void fundsRemove(String username, Double amount) {
		AccountUtils.removeFunds(Bukkit.getPlayer(username).getUniqueId(), amount);
	}

	@Deprecated
	public void fundsRemove(String username, String world, Double amount) {
		AccountUtils.removeFunds(Bukkit.getPlayer(username).getUniqueId(), world, amount);
	}

	@Deprecated
	public Account getAccount(String username) {
		return AccountUtils.getAccount(Bukkit.getPlayer(username).getUniqueId());
	}

	@Deprecated
	public Double getBalance(String username) {
		return AccountUtils.getFunds(Bukkit.getPlayer(username).getUniqueId());
	}

	@Deprecated
	public Double getBalance(String username, String world) {
		return AccountUtils.getFunds(Bukkit.getPlayer(username).getUniqueId(), world);
	}
	
	public Boolean accountExists(OfflinePlayer player) {
		return getAccount(player) != null;
	}

	public void createAccount(OfflinePlayer player) {
		AccountUtils.createAccount(player.getUniqueId());
	}

	public void fundsAdd(OfflinePlayer player, Double amount) {
		AccountUtils.addFunds(player.getUniqueId(), amount);
	}

	public void fundsAdd(OfflinePlayer player, String world, Double amount) {
		AccountUtils.addFunds(player.getUniqueId(), amount);
	}

	public Boolean fundsHas(OfflinePlayer player, Double amount) {
		return AccountUtils.hasFunds(player.getUniqueId(), amount);
	}

	public Boolean fundsHas(OfflinePlayer player, String world, Double amount) {
		return AccountUtils.hasFunds(player.getUniqueId(), world, amount);
	}

	public void fundsRemove(OfflinePlayer player, Double amount) {
		AccountUtils.removeFunds(player.getUniqueId(), amount);
	}

	public void fundsRemove(OfflinePlayer player, String world, Double amount) {
		AccountUtils.removeFunds(player.getUniqueId(), world, amount);
	}

	public Account getAccount(OfflinePlayer player) {
		return AccountUtils.getAccount(player.getUniqueId());
	}

	public Double getBalance(OfflinePlayer player) {
		return AccountUtils.getFunds(player.getUniqueId());
	}

	public Double getBalance(OfflinePlayer player, String world) {
		return AccountUtils.getFunds(player.getUniqueId(), world);
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
}